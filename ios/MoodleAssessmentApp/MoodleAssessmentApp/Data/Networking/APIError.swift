import Foundation

enum APIError: LocalizedError {
    case invalidURL
    case invalidResponse
    case httpStatus(Int)
    case decoding
    case apiMessage(String)
    case missingToken

    var errorDescription: String? {
        switch self {
        case .invalidURL:
            return "Invalid request URL."
        case .invalidResponse:
            return "Invalid server response."
        case let .httpStatus(code):
            return "Server returned HTTP \(code)."
        case .decoding:
            return "Failed to parse server response."
        case let .apiMessage(message):
            return message
        case .missingToken:
            return "Missing authentication token."
        }
    }
}
