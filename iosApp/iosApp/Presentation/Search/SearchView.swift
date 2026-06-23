import SwiftUI
import Shared

struct SearchView: View {
    @StateObject private var wrapper = SearchViewModelWrapper()

    /// Called when the user taps a movie row. Wired up by [MainTabView]
    /// so the detail screen is pushed onto the shared NavigationStack.
    let onMovieTapped: (Movie) -> Void

    @State private var query: String = ""

    var body: some View {
        List {
            if wrapper.state.isLoading && wrapper.state.movies.isEmpty {
                HStack { Spacer(); ProgressView(); Spacer() }
            } else if let error = wrapper.state.errorMessage,
                      wrapper.state.movies.isEmpty {
                ErrorStateBanner(message: error) {
                    if !query.isEmpty {
                        wrapper.dispatch(SearchUiActionSearchSubmitted.shared)
                    }
                }
                .listRowSeparator(.hidden)
                .listRowBackground(Color.clear)
            } else if wrapper.state.movies.isEmpty {
                EmptyStateView(
                    systemImage: "magnifyingglass",
                    title: query.isEmpty ? "Search movies" : "No results",
                    message: query.isEmpty
                        ? "Type a title to find Iranian movies."
                        : "Try a different keyword."
                )
                .listRowSeparator(.hidden)
                .listRowBackground(Color.clear)
            } else {
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
            }
        }
        .listStyle(.plain)
        .scrollContentBackground(.hidden)
        .searchable(text: $query, prompt: Text("Search movies..."))
        .onChange(of: query) { _, newValue in
            wrapper.dispatch(SearchUiActionQueryChanged(query: newValue))
        }
        .onSubmit(of: .search) {
            wrapper.dispatch(SearchUiActionSearchSubmitted.shared)
        }
    }

    /// Kotlin `List<String>` maps to `NSArray` over the bridge, so coerce
    /// to `[String]` before joining.
    private static func genreSubtitle(_ genres: [String]) -> String {
        genres.compactMap { $0 as? String }.joined(separator: ", ")
    }
}
