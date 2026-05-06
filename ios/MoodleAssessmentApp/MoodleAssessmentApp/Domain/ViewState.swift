import Foundation

enum ViewState<Value> {
    case idle
    case loading
    case success(Value)
    case empty(message: String)
    case error(message: String)
}
