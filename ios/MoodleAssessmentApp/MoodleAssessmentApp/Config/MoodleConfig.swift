import Foundation

enum MoodleConfig {
    static let baseURL = URL(string: "https://moodle.itcorner.qzz.io")!
    static let restEndpoint = "/webservice/rest/server.php"
    static let restFormat = "json"
    static let defaultToken = "c269d73b8ec3265227714bf37f4dd2e4"
    static let defaultUserID = 1003
}
