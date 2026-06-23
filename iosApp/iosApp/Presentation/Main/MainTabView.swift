import SwiftUI
import Shared

/// Top-level tab destinations. Mirrors the Android `Screen` bottom-nav set
/// (Home, Search, Favorite, Settings). Movie detail is a pushed screen,
/// not a tab.
enum MainTab: Hashable {
    case home
    case search
    case favorite
    case settings
}

/// The single source of truth for iOS navigation. Renders a [TabView] with
/// four feature tabs, each inside its own [NavigationStack] so a movie tap
/// pushes [MovieDetailView] within the active tab.
struct MainTabView: View {
    @State private var selectedTab: MainTab = .home

    var body: some View {
        TabView(selection: $selectedTab) {
            Tab("Home", systemImage: "house.fill", value: .home) {
                NavigationStack {
                    HomeView(onMovieTapped: { movie in
                        openDetail(movieId: movie.id)
                    })
                        .navigationTitle("Home")
                        .navigationBarTitleDisplayMode(.large)
                }
            }

            Tab("Search", systemImage: "magnifyingglass", value: .search, role: .search) {
                NavigationStack {
                    SearchView(onMovieTapped: { movie in
                        openDetail(movieId: movie.id)
                    })
                    .navigationTitle("Search")
                    .navigationBarTitleDisplayMode(.large)
                }
            }

            Tab("Favorites", systemImage: "heart.fill", value: .favorite) {
                NavigationStack {
                    FavoriteView(onMovieTapped: { favorite in
                        openDetail(movieId: favorite.id)
                    })
                }
            }

            Tab("Settings", systemImage: "gearshape.fill", value: .settings) {
                NavigationStack {
                    SettingsView()
                }
            }
        }
        .tint(AppColors.primary)
        .toolbarBackground(.ultraThinMaterial, for: .tabBar)
        .toolbarBackground(.visible, for: .tabBar)
        .sheet(item: $detailMovieId) { movieId in
            NavigationStack {
                MovieDetailView(movieId: movieId.value)
                    .toolbar {
                        ToolbarItem(placement: .topBarTrailing) {
                            Button("Done") { detailMovieId = nil }
                        }
                    }
            }
        }
    }

    // MARK: - Movie detail presentation

    /// Presents the movie detail as a sheet, shared across all tabs. This
    /// keeps navigation state simple while remaining accessible from any tab.
    @State private var detailMovieId: MovieIdBox?

    private func openDetail(movieId: Int32) {
        detailMovieId = MovieIdBox(value: movieId)
    }
}

/// Wrapper so an `Int32` can be used with `.sheet(item:)`.
private struct MovieIdBox: Identifiable {
    let value: Int32
    var id: Int32 { value }
}
