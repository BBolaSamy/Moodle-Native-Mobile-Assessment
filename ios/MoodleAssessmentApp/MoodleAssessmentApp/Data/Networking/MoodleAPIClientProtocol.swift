import Foundation

protocol MoodleAPIClientProtocol {
    func fetchUserCourses(userId: Int) async throws -> [CourseDTO]
    func fetchCourseContents(courseId: Int) async throws -> [CourseSectionDTO]
    func fetchGradeItems(userId: Int, courseId: Int) async throws -> [GradeItemDTO]
}
