import SwiftUI
import Shared

struct HomeView: View {
    @StateObject private var wrapper = HomeViewModelWrapper()

    /// Called when the user taps a movie. Wired up by [MainTabView].
    let onMovieTapped: (Movie) -> Void

    // MARK: - Genre filter state

    /// Extracts the unique genres from the loaded movies. Statically
    /// includes "All" as the default filter.
    private static let defaultGenres = ["All", "Action", "Drama", "Sci-Fi", "Comedy", "Horror", "Thriller"]

    @State private var selectedGenre: String = "All"

    // MARK: - Computed data

    /// The first 5 movies for the featured carousel.
    private var featuredMovies: [Movie] {
        let source = selectedGenre == "All"
            ? wrapper.state.movies
            : wrapper.state.movies.filter {
                Self.stringList($0.genres).contains(selectedGenre)
            }
        return Array(source.prefix(5))
    }

    /// Movies shown in the vertical grid (after the featured set).
    private var gridMovies: [Movie] {
        let source = selectedGenre == "All"
            ? wrapper.state.movies
            : wrapper.state.movies.filter {
                Self.stringList($0.genres).contains(selectedGenre)
            }
        let featured = Set(featuredMovies.map { $0.id })
        return source.filter { !featured.contains($0.id) }
    }

    // MARK: - Body

    var body: some View {
        Group {
            if wrapper.state.isLoading && wrapper.state.movies.isEmpty {
                loadingView
            } else if let error = wrapper.state.errorMessage,
                      wrapper.state.movies.isEmpty {
                ErrorStateBanner(message: error) {
                    wrapper.dispatch(HomeUiActionLoadMovies.shared)
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity)
            } else {
                ScrollView {
                    VStack(spacing: 24) {
                        // ── Featured carousel ────────────────────────
                        if !featuredMovies.isEmpty {
                            carousel
                        }

                        // ── Genre chips ───────────────────────────────
                        GenreChipRow(
                            genres: Self.defaultGenres,
                            selected: $selectedGenre
                        )

                        // ── Trending section ──────────────────────────
                        trendingHeader

                        // ── 2-column poster grid ──────────────────────
                        posterGrid

                        // ── Pagination trigger ────────────────────────
                        paginationFooter
                    }
                }
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

    // MARK: - Featured Carousel

    @ViewBuilder
    private var carousel: some View {
        ZStack(alignment: .bottom) {
            TabView(selection: $carouselPage) {
                ForEach(Array(featuredMovies.enumerated()), id: \.element.id) { index, movie in
                    FeaturedCard(
                        title: movie.title,
                        posterPath: movie.poster
                    ) {
                        onMovieTapped(movie)
                    }
                    .tag(index)
                }
            }
            .tabViewStyle(.page(indexDisplayMode: .never))
            .frame(height: 260)
            .padding(.horizontal, 16) // ← move padding HERE, outside TabView

            PageIndicator(pageCount: featuredMovies.count, currentPage: carouselPage)
                .padding(.bottom, 14)
        }
    }

    @State private var carouselPage: Int = 0

    private var trendingHeader: some View {
        HStack {
            Text("Trending Now")
                .font(.title3.bold())
            Spacer()
            Button("See all") { }
                .font(.subheadline)
                .foregroundColor(AppColors.primary)
        }
        .padding(.horizontal, 20)
    }

    private var posterGrid: some View {
        let columns = [
            GridItem(.flexible(), spacing: 16),
            GridItem(.flexible(), spacing: 16),
        ]

        return LazyVGrid(columns: columns, spacing: 20) {
            ForEach(gridMovies, id: \.id) { movie in
                PosterCard(
                    title: movie.title,
                    posterPath: movie.poster,
                    genre: Self.stringList(movie.genres).first
                ) {
                    onMovieTapped(movie)
                }
            }
        }
        .padding(.horizontal, 20)
    }

    private var paginationFooter: some View {
        Group {
            if wrapper.state.hasNextPage {
                VStack(spacing: 16) {
                    if wrapper.state.isLoading {
                        ProgressView()
                    } else {
                        Button("Explore More") {
                            wrapper.dispatch(HomeUiActionLoadNextPage.shared)
                        }
                        .buttonStyle(.borderedProminent)
                        .tint(AppColors.primary)
                        .controlSize(.large)
                        .clipShape(Capsule())
                    }
                }
                .frame(maxWidth: .infinity)
                .padding(.vertical, 24)
            }
        }
        .onAppear {
            // Auto-load next page when the footer scrolls into view.
            if wrapper.state.hasNextPage && !wrapper.state.isLoading {
                wrapper.dispatch(HomeUiActionLoadNextPage.shared)
            }
        }
    }

    private var loadingView: some View {
        VStack(spacing: 16) {
            ProgressView()
            Text("Loading movies…")
                .foregroundColor(.secondary)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }

    // MARK: - Helpers

    /// Kotlin `List<String>` maps to `NSArray` over the Obj-C bridge.
    /// Coerce to `[String]` so Swift type inference is happy.
    private static func stringList(_ genres: [String]) -> [String] {
        genres.compactMap { $0 as? String }
    }
}
