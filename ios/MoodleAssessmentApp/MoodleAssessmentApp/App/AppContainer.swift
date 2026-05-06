import Foundation

@MainActor
final class AppContainer: ObservableObject {
    let session: AppSession
    let repository: CourseRepositoryProtocol

    init() {
        let session = AppSession()
        let client = MoodleAPIClient(session: session)

        self.session = session
        self.repository = CourseRepository(client: client, session: session)
    }
}
