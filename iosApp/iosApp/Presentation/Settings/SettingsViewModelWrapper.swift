import SwiftUI
import Shared

final class SettingsViewModelWrapper: ObservableObject {
    let viewModel: SettingsViewModel = KoinHelper.shared.getSettingsViewModel()

    @Published var state: SettingsUiState = SettingsUiState(
        themeMode: ThemeMode.system,
        notificationsEnabled: true
    )

    private var watcher: FlowWatcher<SettingsUiState>?

    init() {
        let commonFlow = CommonFlowKt.doWatch(viewModel.uiState) { [weak self] state in
            if let state = state as? SettingsUiState {
                DispatchQueue.main.async {
                    self?.state = state
                }
            }
        }
        self.watcher = commonFlow as? FlowWatcher<SettingsUiState>
    }

    func dispatch(_ action: SettingsUiAction) {
        viewModel.onAction(action: action)
    }

    deinit {
        watcher?.stop()
    }
}
