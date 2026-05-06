import SwiftUI

struct GradesScreen: View {
    let course: Course
    @StateObject private var viewModel: GradesViewModel

    init(course: Course, repository: CourseRepositoryProtocol) {
        self.course = course
        _viewModel = StateObject(wrappedValue: GradesViewModel(repository: repository))
    }

    var body: some View {
        LoadableView(state: viewModel.state, retryAction: reload) { items in
            List(items) { item in
                VStack(alignment: .leading, spacing: 6) {
                    Text(item.itemName)
                        .font(.headline)
                    Text(item.gradeText)
                        .font(.subheadline)
                    if let percentage = item.percentageText {
                        Text(percentage)
                            .font(.caption)
                            .foregroundStyle(.secondary)
                    }
                }
                .padding(.vertical, 4)
            }
            .listStyle(.insetGrouped)
        }
        .navigationTitle("Grades")
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
