import Foundation

enum CourseSectionMapper {
    static func map(_ dto: CourseSectionDTO) -> CourseSection {
        CourseSection(id: dto.id, title: dto.name ?? "Untitled Section")
    }
}
