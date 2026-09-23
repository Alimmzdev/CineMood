import SwiftUI
import Shared

/// Root-level wrapper around the shared `AppViewModel`.
///
/// Its primary job on iOS is to surface the persisted `themeMode` so the
/// app root can apply it via `.preferredColorScheme(...)`. The theme is
/// stored in `ThemeRepository` (a `StateFlow<ThemeMode>`), so any change
/// made in Settings propagates here automatically.
final class AppViewModelWrapper: ObservableObject {
    let viewModel: AppViewModel = KoinHelper.shared.getAppViewModel()

    @Published var themeMode: ThemeMode = .system
 
    private var watcher: FlowWatcher<AppUiState>?

    init() {
        let commonFlow = CommonFlowKt.doWatch(viewModel.uiState) { [weak self] state in
            if let state = state as? AppUiState {
                DispatchQueue.main.async {
                    self?.themeMode = state.themeMode
                }
            }
        }
        self.watcher = commonFlow as? FlowWatcher<AppUiState>
    }

    deinit {
        watcher?.stop()
    }
}
