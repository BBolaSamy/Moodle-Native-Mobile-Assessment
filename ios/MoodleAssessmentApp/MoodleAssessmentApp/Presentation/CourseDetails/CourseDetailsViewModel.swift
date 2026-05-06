import Foundation

@MainActor
final class CourseDetailsViewModel: ObservableObject {
    @Published private(set) var state: ViewState<[CourseSection]> = .idle

    private let repository: CourseRepositoryProtocol

    init(repository: CourseRepositoryProtocol) {
        self.repository = repository
    }

    func load(courseID: Int) async {
        state = .loading

        do {
            let sections = try await repository.getCourseSections(courseId: courseID)
            if sections.isEmpty {
                state = .empty(message: "No sections available for this course.")
            } else {
                state = .success(sections)
            }
        } catch {
            state = .error(message: error.localizedDescription)
        }
    }
}
