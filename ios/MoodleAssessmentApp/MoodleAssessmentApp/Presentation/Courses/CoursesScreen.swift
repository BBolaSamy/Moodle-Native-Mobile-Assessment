import SwiftUI

struct CoursesScreen: View {
    @StateObject private var viewModel: CoursesViewModel
    @State private var path = NavigationPath()
    private let repository: CourseRepositoryProtocol

    init(repository: CourseRepositoryProtocol) {
        _viewModel = StateObject(wrappedValue: CoursesViewModel(repository: repository))
        self.repository = repository
    }

    var body: some View {
        NavigationStack(path: $path) {
            LoadableView(state: viewModel.state, retryAction: reload) { courses in
                List(courses) { course in
                    HStack(spacing: 12) {
                        Button {
                            path.append(CourseRoute.details(course))
                        } label: {
                            CourseRowView(course: course)
                                .frame(maxWidth: .infinity, alignment: .leading)
                        }
                        .buttonStyle(.plain)

                        Button {
                            path.append(CourseRoute.grades(course))
                        } label: {
                            Text("Grades")
                                .font(.subheadline.weight(.semibold))
                        }
                        .buttonStyle(.borderedProminent)
                        .tint(.indigo)
                    }
                }
                .listStyle(.insetGrouped)
                .refreshable {
                    await viewModel.loadCourses()
                }
            }
            .navigationTitle("Courses")
            .task {
                guard case .idle = viewModel.state else { return }
                await viewModel.loadCourses()
            }
            .navigationDestination(for: CourseRoute.self) { route in
                switch route {
                case let .details(course):
                    CourseDetailsScreen(course: course, repository: repository)
                case let .grades(course):
                    GradesScreen(course: course, repository: repository)
                }
            }
        }
    }

    private func reload() {
        Task {
            await viewModel.retry()
        }
    }
}

private enum CourseRoute: Hashable {
    case details(Course)
    case grades(Course)
}
