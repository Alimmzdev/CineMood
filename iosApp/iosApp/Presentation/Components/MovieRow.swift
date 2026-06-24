import SwiftUI

// MARK: - Shared AsyncImage poster (reusable placeholder logic)

/// Renders a movie poster with loading, error, and placeholder states.
/// Used by all card and row components.
struct MoviePosterView: View {
    let url: URL?
    let placeholderIcon: String
    let cornerRadius: CGFloat

    init(
        url: URL?,
        placeholderIcon: String = "film",
        cornerRadius: CGFloat = 12
    ) {
        self.url = url
        self.placeholderIcon = placeholderIcon
        self.cornerRadius = cornerRadius
    }

    var body: some View {
        AsyncImage(url: url) { phase in
            switch phase {
            case .empty:
                Color.gray.opacity(0.25)
            case .success(let image):
                image.resizable().aspectRatio(contentMode: .fill)
            case .failure:
                Color.gray.opacity(0.15)
                    .overlay(
                        Image(systemName: placeholderIcon)
                            .font(.title3)
                            .foregroundColor(.secondary)
                    )
            @unknown default:
                Color.gray.opacity(0.15)
            }
        }
        .clipShape(RoundedRectangle(cornerRadius: cornerRadius))
    }
}

// MARK: - Shared row used by Search & Favorite lists

/// A compact horizontal row showing a movie poster + title. Used by the
/// Search and Favorite list screens (not the Home grid).
struct MovieRow: View {
    let title: String
    let posterURL: URL?
    let subtitle: String?
    let onTap: () -> Void

    init(
        title: String,
        posterPath: String?,
        subtitle: String? = nil,
        onTap: @escaping () -> Void
    ) {
        self.title = title
        self.posterURL = posterPath.flatMap { URL(string: $0) }
        self.subtitle = subtitle
        self.onTap = onTap
    }

    var body: some View {
        Button(action: onTap) {
            HStack(alignment: .top, spacing: 12) {
                MoviePosterView(url: posterURL, cornerRadius: 8)
                    .frame(width: 60, height: 90)

                VStack(alignment: .leading, spacing: 4) {
                    Text(title)
                        .font(.headline)
                        .foregroundColor(.primary)
                        .lineLimit(2)
                    if let subtitle = subtitle {
                        Text(subtitle)
                            .font(.subheadline)
                            .foregroundColor(.secondary)
                    }
                }
                Spacer(minLength: 0)
            }
            .contentShape(Rectangle())
        }
        .buttonStyle(.plain)
    }
}

// MARK: - Featured Card

struct FeaturedCard: View {
    let title: String
    let posterURL: URL?
    let onTap: () -> Void

    @State private var isPressed = false

    init(title: String, posterPath: String?, onTap: @escaping () -> Void) {
        self.title = title
        self.posterURL = posterPath.flatMap { URL(string: $0) }
        self.onTap = onTap
    }

    var body: some View {
        Button(action: onTap) {
            GeometryReader { geo in
                ZStack(alignment: .bottom) {
                    // Poster fills the card exactly
                    MoviePosterView(
                        url: posterURL,
                        placeholderIcon: "film",
                        cornerRadius: 20
                    )
                    .frame(width: geo.size.width, height: geo.size.height)
                    .clipped()

                    // Cinematic gradient
                    ZStack {
                        LinearGradient(
                            colors: [.clear, .black.opacity(0.3)],
                            startPoint: .center,
                            endPoint: .bottom
                        )
                        LinearGradient(
                            colors: [.clear, .black.opacity(0.75)],
                            startPoint: UnitPoint(x: 0.5, y: 0.55),
                            endPoint: .bottom
                        )
                    }

                    // Text + play button
                    HStack(alignment: .bottom) {
                        VStack(alignment: .leading, spacing: 6) {
                            HStack(spacing: 4) {
                                Image(systemName: "star.fill")
                                    .font(.system(size: 7, weight: .bold))
                                    .foregroundColor(.white)
                                Text("FEATURED")
                                    .font(.system(size: 9, weight: .black))
                                    .foregroundColor(.white)
                                    .kerning(1.2)
                            }
                            .padding(.horizontal, 9)
                            .padding(.vertical, 4)
                            .background(AppColors.primary)
                            .clipShape(Capsule())

                            Text(title)
                                .font(.title3.bold())
                                .foregroundColor(.white)
                                .lineLimit(2)
                                .shadow(color: .black.opacity(0.4), radius: 4, y: 2)

                            HStack(spacing: 4) {
                                Text("Tap to explore")
                                    .font(.caption2)
                                    .foregroundColor(.white.opacity(0.65))
                                Image(systemName: "chevron.right")
                                    .font(.system(size: 8, weight: .semibold))
                                    .foregroundColor(.white.opacity(0.5))
                            }
                        }

                        Spacer()

                        ZStack {
                            Circle()
                                .fill(.ultraThinMaterial)
                                .frame(width: 44, height: 44)
                            Image(systemName: "play.fill")
                                .font(.system(size: 16, weight: .semibold))
                                .foregroundColor(.white)
                                .offset(x: 2)
                        }
                    }
                    .padding(.horizontal, 18)
                    .padding(.bottom, 18)
                }
                .clipShape(RoundedRectangle(cornerRadius: 20))
                .shadow(color: .black.opacity(0.35), radius: 16, x: 0, y: 8)
                .scaleEffect(isPressed ? 0.97 : 1.0)
                .animation(.spring(response: 0.3, dampingFraction: 0.7), value: isPressed)
            }
        }
        .frame(height: 260) // ← critical: give GeometryReader an explicit height
        .buttonStyle(.plain)
    }
}

// MARK: - Vertical poster card (2-column grid)

/// A tall poster card used in the "Trending Now" grid on the Home screen.
/// Shows the poster with rounded corners, the title, and the first genre.
struct PosterCard: View {
    let title: String
    let posterURL: URL?
    let genre: String?
    let onTap: () -> Void

    init(
        title: String,
        posterPath: String?,
        genre: String? = nil,
        onTap: @escaping () -> Void
    ) {
        self.title = title
        self.posterURL = posterPath.flatMap { URL(string: $0) }
        self.genre = genre
        self.onTap = onTap
    }

    var body: some View {
        Button(action: onTap) {
            VStack(alignment: .leading, spacing: 8) {
                MoviePosterView(
                    url: posterURL,
                    placeholderIcon: "film",
                    cornerRadius: 20
                )
                .aspectRatio(0.68, contentMode: .fit)

                Text(title)
                    .font(.subheadline.bold())
                    .foregroundColor(.primary)
                    .lineLimit(1)

                if let genre = genre {
                    Text(genre)
                        .font(.caption)
                        .foregroundColor(.secondary)
                        .lineLimit(1)
                }
            }
            .contentShape(Rectangle())
        }
        .buttonStyle(.plain)
    }
}

// MARK: - Page indicator dots (carousel)

/// A row of dots that indicate the current page of a carousel.
struct PageIndicator: View {
    let pageCount: Int
    let currentPage: Int

    var body: some View {
        HStack(spacing: 6) {
            ForEach(0..<pageCount, id: \.self) { index in
                Capsule()
                    .fill(index == currentPage
                          ? AppColors.primary
                          : AppColors.primary.opacity(0.3))
                    .frame(width: index == currentPage ? 18 : 6, height: 6)
                    .animation(.easeInOut(duration: 0.25), value: currentPage)
            }
        }
    }
}

// MARK: - Genre filter chips

/// A horizontal row of selectable genre chips.
struct GenreChipRow: View {
    let genres: [String]
    @Binding var selected: String

    var body: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack(spacing: 8) {
                ForEach(genres, id: \.self) { genre in
                    let isSelected = selected == genre
                    Button {
                        selected = genre
                    } label: {
                        Text(genre)
                            .font(.subheadline.weight(.medium))
                            .padding(.horizontal, 14)
                            .padding(.vertical, 8)
                            .foregroundStyle(isSelected ? .white : .primary)
                            .background(
                                Capsule().fill(
                                    isSelected
                                        ? AppColors.primary
                                        : AppColors.primary.opacity(0.12)
                                )
                            )
                    }
                    .buttonStyle(.plain)
                }
            }
            .padding(.horizontal, 20)
        }
    }
}

// MARK: - Shared states

/// Small empty-state view used across list screens.
struct EmptyStateView: View {
    let systemImage: String
    let title: String
    let message: String

    var body: some View {
        VStack(spacing: 12) {
            Image(systemName: systemImage)
                .font(.system(size: 44))
                .foregroundColor(.secondary)
            Text(title)
                .font(.headline)
            Text(message)
                .font(.subheadline)
                .foregroundColor(.secondary)
                .multilineTextAlignment(.center)
        }
        .padding()
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}

/// Error state with an optional retry button.
struct ErrorStateBanner: View {
    let message: String
    let onRetry: () -> Void

    var body: some View {
        VStack(spacing: 12) {
            Text(message)
                .foregroundColor(.red)
                .multilineTextAlignment(.center)
            Button("Retry", action: onRetry)
                .buttonStyle(.borderedProminent)
                .tint(AppColors.primary)
        }
        .padding()
    }
}
