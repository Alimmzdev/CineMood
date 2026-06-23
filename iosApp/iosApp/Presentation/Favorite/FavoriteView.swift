import SwiftUI
import Shared

struct FavoriteView: View {
    @StateObject private var wrapper = FavoriteViewModelWrapper()

    /// Called when the user taps a favorite. Wired up by [MainTabView]
    /// so the detail screen is pushed onto the shared NavigationStack.
    let onMovieTapped: (FavoriteMovieItem) -> Void

    var body: some View {
        Group {
            if wrapper.state.isLoading && wrapper.state.favorites.isEmpty {
                ProgressView("Loading favorites...")
            } else if let error = wrapper.state.errorMessage,
                      wrapper.state.favorites.isEmpty {
                ErrorStateBanner(message: error) {
                    wrapper.dispatch(FavoriteUiActionRefresh.shared)
                }
            } else if wrapper.state.favorites.isEmpty {
                EmptyStateView(
                    systemImage: "heart",
                    title: "No favorites yet",
                    message: "Movies you favorite will show up here."
                )
            } else {
                List {
                    ForEach(wrapper.state.favorites, id: \.id) { favorite in
                        MovieRow(
                            title: favorite.title,
                            posterPath: favorite.poster,
                            subtitle: Self.genreSubtitle(favorite.genres)
                        ) {
                            onMovieTapped(favorite)
                        }
                        .listRowSeparator(.hidden)
                        .listRowBackground(Color.clear)
                        .padding(.vertical, 2)
                    }
                    .onDelete { indexSet in
                        for index in indexSet {
                            let movie = wrapper.state.favorites[index]
                            wrapper.dispatch(
                                FavoriteUiActionRemoveFavorite(movie: movie)
                            )
                        }
                    }
                }
                .listStyle(.plain)
                .scrollContentBackground(.hidden)
            }
        }
        .navigationTitle("Favorites")
        .navigationBarTitleDisplayMode(.large)
        .refreshable {
            wrapper.dispatch(FavoriteUiActionRefresh.shared)
        }
    }

    /// Kotlin `List<String>` maps to `NSArray` over the bridge, so coerce
    /// to `[String]` before joining.
    private static func genreSubtitle(_ genres: [String]) -> String {
        genres.compactMap { $0 as? String }.joined(separator: ", ")
    }
}
