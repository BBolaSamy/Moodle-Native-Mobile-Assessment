import SwiftUI

struct CourseDetailsScreen: View {
    let course: Course
    @StateObject private var viewModel: CourseDetailsViewModel

    init(course: Course, repository: CourseRepositoryProtocol) {
        self.course = course
        _viewModel = StateObject(wrappedValue: CourseDetailsViewModel(repository: repository))
    }

    var body: some View {
        LoadableView(state: viewModel.state, retryAction: reload) { sections in
            List(sections) { section in
                Text(section.title)
            }
            .listStyle(.insetGrouped)
        }
        .navigationTitle(course.name)
        .navigationBarTitleDisplayMode(.inline)
        .task {
            guard case .idle = viewModel.state else { return }
            await viewModel.load(courseID: course.id)
        }
    }

    private func reload() {
        Task {
            await viewModel.load(courseID: course.id)
        }
    }
}
