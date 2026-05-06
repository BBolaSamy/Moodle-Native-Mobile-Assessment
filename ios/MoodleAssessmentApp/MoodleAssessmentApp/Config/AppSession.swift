import Foundation

protocol TokenProviding {
    var token: String { get }
}

final class AppSession: ObservableObject, TokenProviding {
    @Published var token: String
    @Published var userID: Int

    init(token: String = MoodleConfig.defaultToken, userID: Int = MoodleConfig.defaultUserID) {
        self.token = token
        self.userID = userID
    }
}
