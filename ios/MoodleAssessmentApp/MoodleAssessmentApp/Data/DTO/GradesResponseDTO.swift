import Foundation

struct GradesResponseDTO: Decodable {
    let usergrades: [UserGradeDTO]?
    let gradeitems: [GradeItemDTO]?
}

struct UserGradeDTO: Decodable {
    let courseid: Int?
    let gradeitems: [GradeItemDTO]?
}
