import SwiftUI

struct RootView: View {
    @EnvironmentObject private var container: AppContainer

    var body: some View {
        CoursesScreen(repository: container.repository)
    }
}
