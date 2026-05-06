import XCTest
@testable import MoodleAssessmentApp

final class APIRequestBuilderTests: XCTestCase {
    func test_makeRESTRequest_containsRequiredMoodleQueryItems() throws {
        let session = AppSession(token: "abc-token", userID: 1003)
        let client = MoodleAPIClient(session: session, urlSession: .shared)

        let request = try client.makeRESTRequest(
            function: .courses,
            additionalItems: [URLQueryItem(name: "userid", value: "1003")]
        )

        let components = try XCTUnwrap(URLComponents(url: try XCTUnwrap(request.url), resolvingAgainstBaseURL: false))
        let queryItems = components.queryItems ?? []

        XCTAssertTrue(queryItems.contains(URLQueryItem(name: "wstoken", value: "abc-token")))
        XCTAssertTrue(queryItems.contains(URLQueryItem(name: "wsfunction", value: "core_enrol_get_users_courses")))
        XCTAssertTrue(queryItems.contains(URLQueryItem(name: "moodlewsrestformat", value: "json")))
        XCTAssertTrue(queryItems.contains(URLQueryItem(name: "userid", value: "1003")))
    }

}
