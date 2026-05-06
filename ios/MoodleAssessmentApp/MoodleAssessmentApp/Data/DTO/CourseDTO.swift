import Foundation

struct CourseDTO: Decodable {
    let id: Int
    let fullname: String?
    let displayname: String?
    let progress: Int?
    let completionprogress: Int?
    let courseimage: String?
    let overviewfiles: [OverviewFileDTO]?

    init(
        id: Int,
        fullname: String?,
        displayname: String?,
        progress: Int?,
        completionprogress: Int?,
        courseimage: String? = nil,
        overviewfiles: [OverviewFileDTO]?
    ) {
        self.id = id
        self.fullname = fullname
        self.displayname = displayname
        self.progress = progress
        self.completionprogress = completionprogress
        self.courseimage = courseimage
        self.overviewfiles = overviewfiles
    }
}

struct OverviewFileDTO: Decodable, Equatable {
    let fileurl: String
}
