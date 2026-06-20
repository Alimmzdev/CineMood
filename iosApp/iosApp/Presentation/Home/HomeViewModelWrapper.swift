import SwiftUI
import Shared

class HomeViewModelWrapper: ObservableObject {
    let viewModel: HomeViewModel = KoinHelper.shared.getHomeViewModel()
    
    @Published var state: HomeUiState = HomeUiState(
        isLoading: false,
        movies: [],
        errorMessage: nil,
        currentPage: 1,
        hasNextPage: false
    )
    
    private var watcher: FlowWatcher<HomeUiState>?
    
    init() {
        // Watch the state flow from Kotlin
        let commonFlow = CommonFlowKt.doWatch(viewModel.uiState) { [weak self] state in
            if let state = state as? HomeUiState {
                DispatchQueue.main.async {
                    self?.state = state
                }
            }
        }
        self.watcher = commonFlow as? FlowWatcher<HomeUiState>
    }
    
    func dispatch(_ action: HomeUiAction) {
        viewModel.onAction(action: action)
    }
    
    deinit {
        watcher?.stop()
    }
}
