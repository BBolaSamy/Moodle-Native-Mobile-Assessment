import SwiftUI

struct CourseRowView: View {
    let course: Course

    var body: some View {
        HStack(spacing: 12) {
            AsyncImage(url: course.imageURL) { phase in
                switch phase {
                case let .success(image):
                    image
                        .resizable()
                        .scaledToFill()
                default:
                    ZStack {
                        Color.gray.opacity(0.15)
                        Image(systemName: "book.closed")
                            .foregroundStyle(.secondary)
                    }
                }
            }
            .frame(width: 64, height: 64)
            .clipShape(RoundedRectangle(cornerRadius: 10))

            VStack(alignment: .leading, spacing: 6) {
                Text(course.name)
                    .font(.headline)
                Text(progressText)
                    .font(.subheadline)
                    .foregroundStyle(.secondary)
            }
        }
        .padding(.vertical, 4)
    }

    private var progressText: String {
        if let progress = course.progress {
            return "Progress: \(progress)%"
        }
        return "Progress: N/A"
    }
}
