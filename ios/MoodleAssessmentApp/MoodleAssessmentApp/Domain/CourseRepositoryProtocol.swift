import Foundation

protocol CourseRepositoryProtocol {
    func getCourses() async throws -> [Course]
    func getCourseSections(courseId: Int) async throws -> [CourseSection]
    func getGrades(courseId: Int) async throws -> [GradeItem]
}
