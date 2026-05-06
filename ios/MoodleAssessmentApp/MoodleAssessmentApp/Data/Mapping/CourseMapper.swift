import Foundation

enum CourseMapper {
    static func map(_ dto: CourseDTO) -> Course {
        let title = dto.displayname ?? dto.fullname ?? "Untitled Course"
        let progress = dto.progress ?? dto.completionprogress
        let imageURL =
            dto.overviewfiles?.first.flatMap { URL(string: $0.fileurl) } ??
            dto.courseimage.flatMap(URL.init(string:))

        return Course(
            id: dto.id,
            name: title,
            progress: progress,
            imageURL: imageURL
        )
    }
}
