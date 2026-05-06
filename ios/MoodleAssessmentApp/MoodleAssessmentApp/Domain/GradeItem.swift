import Foundation

struct GradeItem: Identifiable, Equatable {
    let id: Int
    let itemName: String
    let gradeText: String
    let percentageText: String?
}
