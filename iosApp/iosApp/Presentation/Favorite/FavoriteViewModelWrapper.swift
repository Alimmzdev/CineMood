import SwiftUI
import Shared

final class FavoriteViewModelWrapper: ObservableObject {
    let viewModel: FavoriteViewModel = KoinHelper.shared.getFavoriteViewModel()

    @Published var state: FavoriteUiState = FavoriteUiState(
        isLoading: false,
        favorites: [],
        errorMessage: nil
    )

    private var watcher: FlowWatcher<FavoriteUiState>?

    init() {
        let commonFlow = CommonFlowKt.doWatch(viewModel.uiState) { [weak self] state in
            if let state = state as? FavoriteUiState {
                DispatchQueue.main.async {
                    self?.state = state
                }
            }
        }
        self.watcher = commonFlow as? FlowWatcher<FavoriteUiState>
    }

    func dispatch(_ action: FavoriteUiAction) {
        viewModel.onAction(action: action)
    }

    deinit {
        watcher?.stop()
    }
}
