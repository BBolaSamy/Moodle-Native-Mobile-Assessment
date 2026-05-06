import XCTest
@testable import MoodleAssessmentApp

@MainActor
final class CoursesViewModelTests: XCTestCase {
    func test_loadCourses_successFlow() async {
        let course = Course(id: 1, name: "Physics", progress: 50, imageURL: nil)
        let repo = MockCourseRepository(coursesResult: .success([course]))
        let viewModel = CoursesViewModel(repository: repo)

        await viewModel.loadCourses()

        guard case let .success(items) = viewModel.state else {
            return XCTFail("Expected success state")
        }
        XCTAssertEqual(items.count, 1)
        XCTAssertEqual(items.first?.name, "Physics")
    }

    func test_loadCourses_emptyFlow() async {
        let repo = MockCourseRepository(coursesResult: .success([]))
        let viewModel = CoursesViewModel(repository: repo)

        await viewModel.loadCourses()

        guard case .empty = viewModel.state else {
            return XCTFail("Expected empty state")
        }
    }

    func test_loadCourses_errorThenRetryFlow() async {
        let successCourse = Course(id: 2, name: "Biology", progress: nil, imageURL: nil)
        let repo = MockCourseRepository(
            coursesResult: .failure(MockError.network),
            retryCoursesResult: .success([successCourse])
        )
        let viewModel = CoursesViewModel(repository: repo)

        await viewModel.loadCourses()
        guard case .error = viewModel.state else {
            return XCTFail("Expected error state")
        }

        repo.activateRetryResult()
        await viewModel.retry()

        guard case let .success(items) = viewModel.state else {
            return XCTFail("Expected success state after retry")
        }
        XCTAssertEqual(items.first?.name, "Biology")
    }
}

private enum MockError: Error {
    case network
}

private final class MockCourseRepository: CourseRepositoryProtocol {
    private var currentResult: Result<[Course], Error>
    private let retryResult: Result<[Course], Error>

    init(
        coursesResult: Result<[Course], Error>,
        retryCoursesResult: Result<[Course], Error>? = nil
    ) {
        self.currentResult = coursesResult
        self.retryResult = retryCoursesResult ?? coursesResult
    }

    func activateRetryResult() {
        currentResult = retryResult
    }

    func getCourses() async throws -> [Course] {
        try currentResult.get()
    }

    func getCourseSections(courseId: Int) async throws -> [CourseSection] {
        []
    }

    func getGrades(courseId: Int) async throws -> [GradeItem] {
        []
    }
}
