import Foundation

@MainActor
final class GradesViewModel: ObservableObject {
    @Published private(set) var state: ViewState<[GradeItem]> = .idle

    private let repository: CourseRepositoryProtocol

    init(repository: CourseRepositoryProtocol) {
        self.repository = repository
    }

    func load(courseID: Int) async {
        state = .loading

        do {
            let grades = try await repository.getGrades(courseId: courseID)
            if grades.isEmpty {
                state = .empty(message: "No grades found for this course.")
            } else {
                state = .success(grades)
            }
        } catch {
            state = .error(message: error.localizedDescription)
        }
    }
}
