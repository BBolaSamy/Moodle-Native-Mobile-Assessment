import Foundation

final class CourseRepository: CourseRepositoryProtocol {
    private let client: MoodleAPIClientProtocol
    private let session: AppSession

    init(client: MoodleAPIClientProtocol, session: AppSession) {
        self.client = client
        self.session = session
    }

    func getCourses() async throws -> [Course] {
        let courses = try await client.fetchUserCourses(userId: session.userID)
        return courses.map(CourseMapper.map)
    }

    func getCourseSections(courseId: Int) async throws -> [CourseSection] {
        let sections = try await client.fetchCourseContents(courseId: courseId)
        return sections.map(CourseSectionMapper.map)
    }

    func getGrades(courseId: Int) async throws -> [GradeItem] {
        let grades = try await client.fetchGradeItems(userId: session.userID, courseId: courseId)
        return grades.map(GradeMapper.map)
    }
}
