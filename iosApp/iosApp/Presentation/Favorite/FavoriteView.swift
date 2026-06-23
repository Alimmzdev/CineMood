import SwiftUI
import Shared

struct FavoriteView: View {
    @StateObject private var wrapper = FavoriteViewModelWrapper()
    let onMovieTapped: (FavoriteMovieItem) -> Void

    var body: some View {
        ZStack {
            Color(.systemGroupedBackground).ignoresSafeArea()

            Group {
                if wrapper.state.isLoading && wrapper.state.favorites.isEmpty {
                    loadingView
                } else if let error = wrapper.state.errorMessage,
                          wrapper.state.favorites.isEmpty {
                    errorView(message: error)
                } else if wrapper.state.favorites.isEmpty {
                    emptyView
                } else {
                    favoritesGrid
                }
            }
        }
        .navigationTitle("Favorites")
        .navigationBarTitleDisplayMode(.large)
        .toolbar {
            if !wrapper.state.favorites.isEmpty {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Text("\(wrapper.state.favorites.count) saved")
                        .font(.caption.weight(.medium))
                        .foregroundStyle(.secondary)
                }
            }
        }
        .refreshable {
            wrapper.dispatch(FavoriteUiActionRefresh.shared)
        }
    }

    // MARK: - Favorites Grid

    private var favoritesGrid: some View {
        ScrollView {
            LazyVStack(spacing: 0) {
                ForEach(Array(wrapper.state.favorites.enumerated()), id: \.element.id) { index, favorite in
                    FavoriteRow(
                        favorite: favorite,
                        index: index + 1,
                        onTap: { onMovieTapped(favorite) },
                        onRemove: {
                            wrapper.dispatch(FavoriteUiActionRemoveFavorite(movie: favorite))
                        }
                    )

                    if index < wrapper.state.favorites.count - 1 {
                        Divider()
                            .padding(.leading, 90)
                    }
                }
            }
            .background(Color(.secondarySystemGroupedBackground))
            .clipShape(RoundedRectangle(cornerRadius: 16))
            .padding(.horizontal, 20)
            .padding(.top, 8)
            .padding(.bottom, 32)
        }
    }

    // MARK: - States

    private var loadingView: some View {
        VStack(spacing: 16) {
            ForEach(0..<5, id: \.self) { _ in
                FavoriteRowSkeleton()
            }
            .padding(.horizontal, 20)
            .padding(.top, 8)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
    }

    private func errorView(message: String) -> some View {
        VStack(spacing: 20) {
            ZStack {
                Circle()
                    .fill(Color.red.opacity(0.1))
                    .frame(width: 80, height: 80)
                Image(systemName: "exclamationmark.triangle.fill")
                    .font(.system(size: 32))
                    .foregroundStyle(.red)
            }

            VStack(spacing: 8) {
                Text("Something went wrong")
                    .font(.headline)
                Text(message)
                    .font(.subheadline)
                    .foregroundStyle(.secondary)
                    .multilineTextAlignment(.center)
                    .padding(.horizontal, 32)
            }

            Button {
                wrapper.dispatch(FavoriteUiActionRefresh.shared)
            } label: {
                Label("Try Again", systemImage: "arrow.clockwise")
                    .font(.subheadline.weight(.semibold))
                    .foregroundStyle(.white)
                    .padding(.horizontal, 24)
                    .padding(.vertical, 12)
                    .background(AppColors.primary)
                    .clipShape(Capsule())
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }

    private var emptyView: some View {
        VStack(spacing: 20) {
            ZStack {
                Circle()
                    .fill(AppColors.primary.opacity(0.1))
                    .frame(width: 100, height: 100)
                Image(systemName: "heart.slash.fill")
                    .font(.system(size: 40))
                    .foregroundStyle(AppColors.primary.opacity(0.6))
            }

            VStack(spacing: 8) {
                Text("No favorites yet")
                    .font(.title3.bold())
                Text("Movies you love will appear here.\nStart exploring and save your favorites.")
                    .font(.subheadline)
                    .foregroundStyle(.secondary)
                    .multilineTextAlignment(.center)
                    .padding(.horizontal, 32)
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}

// MARK: - Favorite Row

private struct FavoriteRow: View {
    let favorite: FavoriteMovieItem
    let index: Int
    let onTap: () -> Void
    let onRemove: () -> Void

    @State private var isPressed = false
    @State private var showRemoveConfirm = false

    private var posterURL: URL? {
        URL(string: favorite.poster)
    }

    private var genreText: String {
        favorite.genres.joined(separator: " · ")
    }

    var body: some View {
        Button(action: onTap) {
            HStack(spacing: 14) {
                // Rank number
                Text("\(index)")
                    .font(.system(size: 13, weight: .bold, design: .rounded))
                    .foregroundStyle(.tertiary)
                    .frame(width: 20, alignment: .center)

                // Poster
                MoviePosterView(
                    url: posterURL,
                    placeholderIcon: "film",
                    cornerRadius: 10
                )
                .frame(width: 52, height: 76)
                .clipShape(RoundedRectangle(cornerRadius: 10))
                .shadow(color: Color.black.opacity(0.15), radius: 4, x: 0, y: 2)

                // Info
                VStack(alignment: .leading, spacing: 5) {
                    Text(favorite.title)
                        .font(.system(size: 15, weight: .semibold))
                        .foregroundStyle(.primary)
                        .lineLimit(2)

                    if !genreText.isEmpty {
                        Text(genreText)
                            .font(.caption)
                            .foregroundStyle(.secondary)
                            .lineLimit(1)
                    }

                    // Heart badge
                    HStack(spacing: 4) {
                        Image(systemName: "heart.fill")
                            .font(.system(size: 9))
                            .foregroundStyle(AppColors.primary)
                        Text("Saved")
                            .font(.system(size: 10, weight: .medium))
                            .foregroundStyle(AppColors.primary)
                    }
                    .padding(.horizontal, 7)
                    .padding(.vertical, 3)
                    .background(AppColors.primary.opacity(0.1))
                    .clipShape(Capsule())
                }

                Spacer()

                // Remove button
                Button {
                    showRemoveConfirm = true
                } label: {
                    Image(systemName: "trash")
                        .font(.system(size: 14, weight: .medium))
                        .foregroundStyle(.secondary)
                        .frame(width: 36, height: 36)
                        .background(Color(.tertiarySystemGroupedBackground))
                        .clipShape(Circle())
                }
                .buttonStyle(.plain)
                .confirmationDialog("Remove from favorites?", isPresented: $showRemoveConfirm, titleVisibility: .visible) {
                    Button("Remove", role: .destructive) { onRemove() }
                    Button("Cancel", role: .cancel) {}
                }
            }
            .padding(.horizontal, 16)
            .padding(.vertical, 12)
            .contentShape(Rectangle())
        }
        .buttonStyle(.plain)
        .background(
            isPressed ? Color(.tertiarySystemGroupedBackground) : Color.clear
        )
        .simultaneousGesture(
            DragGesture(minimumDistance: 0)
                .onChanged { _ in isPressed = true }
                .onEnded { _ in isPressed = false }
        )
    }
}

// MARK: - Skeleton

private struct FavoriteRowSkeleton: View {
    @State private var shimmer = false

    var body: some View {
        HStack(spacing: 14) {
            RoundedRectangle(cornerRadius: 6)
                .frame(width: 20, height: 16)

            RoundedRectangle(cornerRadius: 10)
                .frame(width: 52, height: 76)

            VStack(alignment: .leading, spacing: 8) {
                RoundedRectangle(cornerRadius: 4)
                    .frame(height: 14)
                RoundedRectangle(cornerRadius: 4)
                    .frame(width: 100, height: 11)
                RoundedRectangle(cornerRadius: 4)
                    .frame(width: 60, height: 11)
            }

            Spacer()
        }
        .foregroundStyle(Color(.systemGray5))
        .padding(.horizontal, 16)
        .padding(.vertical, 12)
        .background(Color(.secondarySystemGroupedBackground))
        .clipShape(RoundedRectangle(cornerRadius: 16))
        .opacity(shimmer ? 0.5 : 1.0)
        .animation(.easeInOut(duration: 0.9).repeatForever(autoreverses: true), value: shimmer)
        .onAppear { shimmer = true }
    }
}
