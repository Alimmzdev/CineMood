import SwiftUI
import Shared

struct HomeView: View {
    @StateObject private var wrapper = HomeViewModelWrapper()

    /// Called when the user taps a movie row. Wired up by [MainTabView]
    /// so the detail screen is presented from the active tab.
    let onMovieTapped: (Movie) -> Void

    var body: some View {
        Group {
            if wrapper.state.isLoading && wrapper.state.movies.isEmpty {
                ProgressView("Loading movies...")
            } else if let error = wrapper.state.errorMessage,
                      wrapper.state.movies.isEmpty {
                ErrorStateBanner(message: error) {
                    wrapper.dispatch(HomeUiActionLoadMovies.shared)
                }
            } else {
                List {
                    ForEach(wrapper.state.movies, id: \.id) { movie in
                        MovieRow(
                            title: movie.title,
                            posterPath: movie.poster,
                            subtitle: Self.genreSubtitle(movie.genres)
                        ) {
                            onMovieTapped(movie)
                        }
                        .listRowSeparator(.hidden)
                        .listRowBackground(Color.clear)
                        .padding(.vertical, 2)
                    }

                    if wrapper.state.isLoading {
                        HStack { Spacer(); ProgressView(); Spacer() }
                            .listRowSeparator(.hidden)
                            .listRowBackground(Color.clear)
                    }

                    if wrapper.state.hasNextPage && !wrapper.state.movies.isEmpty {
                        Color.clear
                            .onAppear {
                                wrapper.dispatch(HomeUiActionLoadNextPage.shared)
                            }
                            .listRowSeparator(.hidden)
                            .listRowBackground(Color.clear)
                    }
                }
                .listStyle(.plain)
                .scrollContentBackground(.hidden)
                .refreshable {
                    wrapper.dispatch(HomeUiActionRefresh.shared)
                }
            }
        }
        .onAppear {
            if wrapper.state.movies.isEmpty {
                wrapper.dispatch(HomeUiActionLoadMovies.shared)
            }
        }
    }

    /// Kotlin `List<String>` maps to `NSArray` over the bridge, so coerce
    /// to `[String]` before joining.
    private static func genreSubtitle(_ genres: [String]) -> String {
        genres.compactMap { $0 as? String }.joined(separator: ", ")
    }
}
