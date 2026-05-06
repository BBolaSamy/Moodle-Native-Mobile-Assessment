import SwiftUI

struct LoadableView<Data, Content: View>: View {
    let state: ViewState<Data>
    let retryAction: () -> Void
    @ViewBuilder let content: (Data) -> Content

    var body: some View {
        switch state {
        case .idle, .loading:
            ProgressView("Loading...")
                .frame(maxWidth: .infinity, maxHeight: .infinity)
        case let .success(data):
            content(data)
        case let .empty(message):
            VStack(spacing: 12) {
                Image(systemName: "tray")
                Text(message)
                    .multilineTextAlignment(.center)
                    .foregroundStyle(.secondary)
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .padding()
        case let .error(message):
            VStack(spacing: 12) {
                Image(systemName: "exclamationmark.triangle")
                Text(message)
                    .multilineTextAlignment(.center)
                    .foregroundStyle(.secondary)
                Button("Retry", action: retryAction)
                    .buttonStyle(.borderedProminent)
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .padding()
        }
    }
}
