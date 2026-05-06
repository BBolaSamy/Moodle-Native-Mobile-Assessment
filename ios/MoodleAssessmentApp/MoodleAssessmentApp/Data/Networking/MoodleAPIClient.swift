import Foundation

struct MoodleErrorEnvelope: Decodable {
    let exception: String?
    let message: String?
    let error: String?
}

final class MoodleAPIClient: MoodleAPIClientProtocol {
    private let session: TokenProviding
    private let urlSession: URLSession
    private let decoder: JSONDecoder
    private let maxRetryAttempts = 3

    init(session: TokenProviding, urlSession: URLSession = .shared, decoder: JSONDecoder = JSONDecoder()) {
        self.session = session
        self.urlSession = urlSession
        self.decoder = decoder
    }

    func fetchUserCourses(userId: Int) async throws -> [CourseDTO] {
        let request = try makeRESTRequest(
            function: .courses,
            additionalItems: [URLQueryItem(name: "userid", value: "\(userId)")]
        )
        return try await perform(request: request)
    }

    func fetchCourseContents(courseId: Int) async throws -> [CourseSectionDTO] {
        let request = try makeRESTRequest(
            function: .contents,
            additionalItems: [URLQueryItem(name: "courseid", value: "\(courseId)")]
        )
        return try await perform(request: request)
    }

    func fetchGradeItems(userId: Int, courseId: Int) async throws -> [GradeItemDTO] {
        let request = try makeRESTRequest(
            function: .grades,
            additionalItems: [
                URLQueryItem(name: "userid", value: "\(userId)"),
                URLQueryItem(name: "courseid", value: "\(courseId)")
            ]
        )
        let response: GradesResponseDTO = try await perform(request: request)
        return response.usergrades?.first?.gradeitems ?? response.gradeitems ?? []
    }

    func makeRESTRequest(function: MoodleFunction, additionalItems: [URLQueryItem]) throws -> URLRequest {
        guard !session.token.isEmpty else {
            throw APIError.missingToken
        }

        let endpoint = MoodleConfig.baseURL.appendingPathComponent(MoodleConfig.restEndpoint)
        guard var components = URLComponents(url: endpoint, resolvingAgainstBaseURL: false) else {
            throw APIError.invalidURL
        }

        components.queryItems = [
            URLQueryItem(name: "wstoken", value: session.token),
            URLQueryItem(name: "wsfunction", value: function.rawValue),
            URLQueryItem(name: "moodlewsrestformat", value: MoodleConfig.restFormat)
        ] + additionalItems

        guard let url = components.url else {
            throw APIError.invalidURL
        }

        var request = URLRequest(url: url)
        request.httpMethod = "GET"
        request.timeoutInterval = 30
        return request
    }

    private func perform<T: Decodable>(request: URLRequest) async throws -> T {
        var attempt = 0

        while true {
            do {
                return try await performOnce(request: request)
            } catch {
                attempt += 1

                if shouldRetry(error: error, attempt: attempt) {
                    // Small exponential backoff for temporary upstream failures (5xx / connection glitches).
                    let delayNs = UInt64(pow(2.0, Double(attempt - 1)) * 300_000_000)
                    try await Task.sleep(nanoseconds: delayNs)
                    continue
                }

                throw error
            }
        }
    }

    private func performOnce<T: Decodable>(request: URLRequest) async throws -> T {
        let (data, response) = try await urlSession.data(for: request)

        guard let httpResponse = response as? HTTPURLResponse else {
            throw APIError.invalidResponse
        }

        guard (200...299).contains(httpResponse.statusCode) else {
            if let body = String(data: data, encoding: .utf8)?
                .trimmingCharacters(in: .whitespacesAndNewlines),
               !body.isEmpty {
                if body.contains("1033") {
                    throw APIError.apiMessage("Server temporarily unavailable (Cloudflare 1033). Please try again.")
                }
                let compactBody = String(body.prefix(160))
                throw APIError.apiMessage("Server returned HTTP \(httpResponse.statusCode): \(compactBody)")
            }
            throw APIError.httpStatus(httpResponse.statusCode)
        }

        if let envelope = try? decoder.decode(MoodleErrorEnvelope.self, from: data),
           envelope.exception != nil || envelope.error != nil {
            throw APIError.apiMessage(envelope.message ?? envelope.error ?? "Moodle API error")
        }

        do {
            return try decoder.decode(T.self, from: data)
        } catch {
            throw APIError.decoding
        }
    }

    private func shouldRetry(error: Error, attempt: Int) -> Bool {
        guard attempt < maxRetryAttempts else { return false }

        if let apiError = error as? APIError {
            if case let .httpStatus(code) = apiError {
                return code >= 500
            }

            if case let .apiMessage(message) = apiError, message.contains("HTTP 5") || message.contains("1033") {
                return true
            }
        }

        if let urlError = error as? URLError {
            switch urlError.code {
            case .timedOut, .networkConnectionLost, .cannotConnectToHost, .cannotFindHost:
                return true
            default:
                return false
            }
        }

        return false
    }
}
