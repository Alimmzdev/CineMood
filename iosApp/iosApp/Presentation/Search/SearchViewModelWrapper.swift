import SwiftUI
import Shared

final class SearchViewModelWrapper: ObservableObject {
    let viewModel: SearchViewModel = KoinHelper.shared.getSearchViewModel()

    @Published var state: SearchUiState = SearchUiState(
        query: "",
        isLoading: false,
        movies: [],
        errorMessage: nil,
        hasSearched: false
    )

    private var watcher: FlowWatcher<SearchUiState>?

    init() {
        let commonFlow = CommonFlowKt.doWatch(viewModel.uiState) { [weak self] state in
            if let state = state as? SearchUiState {
                DispatchQueue.main.async {
                    self?.state = state
                }
            }
        }
        self.watcher = commonFlow as? FlowWatcher<SearchUiState>
    }

    func dispatch(_ action: SearchUiAction) {
        viewModel.onAction(action: action)
    }

    deinit {
        watcher?.stop()
    }
}
