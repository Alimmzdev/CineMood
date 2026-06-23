import SwiftUI
import Shared

final class MovieDetailViewModelWrapper: ObservableObject {
    let viewModel: MovieDetailViewModel

    @Published var state: MovieDetailUiState = MovieDetailUiState(
        isLoading: false,
        movieDetail: nil,
        errorMessage: nil
    )

    private var watcher: FlowWatcher<MovieDetailUiState>?

    init(movieId: Int32) {
        self.viewModel = KoinHelper.shared.getMovieDetailViewModel(movieId: movieId)

        let commonFlow = CommonFlowKt.doWatch(viewModel.uiState) { [weak self] state in
            if let state = state as? MovieDetailUiState {
                DispatchQueue.main.async {
                    self?.state = state
                }
            }
        }
        self.watcher = commonFlow as? FlowWatcher<MovieDetailUiState>
    }

    func dispatch(_ action: MovieDetailUiAction) {
        viewModel.onAction(action: action)
    }

    deinit {
        watcher?.stop()
    }
}
