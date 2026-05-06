import Foundation

struct Course: Identifiable, Equatable, Hashable {
    let id: Int
    let name: String
    let progress: Int?
    let imageURL: URL?
}
