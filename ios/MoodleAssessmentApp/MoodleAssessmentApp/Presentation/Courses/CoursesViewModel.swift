import Foundation

@MainActor
final class CoursesViewModel: ObservableObject {
    @Published private(set) var state: ViewState<[Course]> = .idle

    private let repository: CourseRepositoryProtocol

    init(repository: CourseRepositoryProtocol) {
        self.repository = repository
    }

    func loadCourses() async {
        state = .loading

        do {
            let courses = try await repository.getCourses()
            if courses.isEmpty {
                state = .empty(message: "No enrolled courses found.")
            } else {
                state = .success(courses)
            }
        } catch {
            state = .error(message: error.localizedDescription)
        }
    }

    func retry() async {
        await loadCourses()
    }
}
