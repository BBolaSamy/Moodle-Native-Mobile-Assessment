import Foundation

struct GradeItemDTO: Decodable {
    let id: Int
    let itemname: String?
    let gradeformatted: String?
    let percentageformatted: String?
    let graderaw: Double?
    let grademin: Double?
    let grademax: Double?

    init(
        id: Int,
        itemname: String?,
        gradeformatted: String?,
        percentageformatted: String?,
        graderaw: Double?,
        grademin: Double?,
        grademax: Double?
    ) {
        self.id = id
        self.itemname = itemname
        self.gradeformatted = gradeformatted
        self.percentageformatted = percentageformatted
        self.graderaw = graderaw
        self.grademin = grademin
        self.grademax = grademax
    }
}
