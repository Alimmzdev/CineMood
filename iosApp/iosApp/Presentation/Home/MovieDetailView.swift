import SwiftUI
import Shared

struct MovieDetailView: View {
    @StateObject private var wrapper: MovieDetailViewModelWrapper

    init(movieId: Int32) {
        _wrapper = StateObject(wrappedValue: MovieDetailViewModelWrapper(movieId: movieId))
    }

    var body: some View {
        Group {
            if let detail = wrapper.state.movieDetail {
                content(for: detail)
            } else if let error = wrapper.state.errorMessage {
                ErrorStateBanner(message: error) {
                    wrapper.dispatch(MovieDetailUiActionRetry.shared)
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity)
            } else {
                ProgressView()
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
            }
        }
        .navigationTitle(wrapper.state.movieDetail?.title ?? "Movie")
        .navigationBarTitleDisplayMode(.inline)
    }

    @ViewBuilder
    private func content(for detail: MovieDetail) -> some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 16) {
                header(for: detail)
                metaChips(for: detail)
                if !detail.plot.isEmpty {
                    infoSection(title: "Overview", text: detail.plot)
                }
                if !detail.director.isEmpty {
                    infoSection(title: "Director", text: detail.director)
                }
                if !detail.actors.isEmpty {
                    infoSection(title: "Cast", text: detail.actors)
                }
            }
            .padding()
        }
    }

    @ViewBuilder
    private func header(for detail: MovieDetail) -> some View {
        HStack(alignment: .top, spacing: 16) {
            AsyncImage(url: URL(string: detail.poster)) { phase in
                switch phase {
                case .empty:
                    Color.gray.opacity(0.3)
                case .success(let image):
                    image.resizable()
                        .aspectRatio(contentMode: .fill)
                case .failure:
                    Color.gray.opacity(0.2)
                        .overlay(Image(systemName: "film").foregroundColor(.secondary))
                @unknown default:
                    Color.gray.opacity(0.2)
                }
            }
            .frame(width: 120, height: 180)
            .clipShape(RoundedRectangle(cornerRadius: 12))

            VStack(alignment: .leading, spacing: 8) {
                Text(detail.title)
                    .font(.title2.bold())
                if !detail.year.isEmpty {
                    Text(detail.year)
                        .font(.subheadline)
                        .foregroundColor(.secondary)
                }
                if !detail.imdbRating.isEmpty {
                    HStack(spacing: 4) {
                        Image(systemName: "star.fill")
                            .foregroundColor(.yellow)
                        Text("IMDb \(detail.imdbRating)")
                            .font(.subheadline)
                    }
                }
            }
            Spacer(minLength: 0)
        }
    }

    @ViewBuilder
    private func metaChips(for detail: MovieDetail) -> some View {
        let chips = ([detail.rated, detail.runtime].filter { !$0.isEmpty })
            + (detail.genres.compactMap { $0 as? String })
        if !chips.isEmpty {
            FlowChips(chips: chips)
        }
    }

    @ViewBuilder
    private func infoSection(title: String, text: String) -> some View {
        VStack(alignment: .leading, spacing: 4) {
            Text(title)
                .font(.headline)
            Text(text)
                .font(.body)
                .foregroundColor(.secondary)
        }
    }
}

/// Simple wrapping layout of "chips" (e.g. rated, runtime, genres).
private struct FlowChips: View {
    let chips: [String]

    var body: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack(spacing: 8) {
                ForEach(chips, id: \.self) { chip in
                    Text(chip)
                        .font(.caption.weight(.medium))
                        .padding(.horizontal, 12)
                        .padding(.vertical, 6)
                        .background(
                            Capsule().fill(AppColors.secondary.opacity(0.2))
                        )
                        .foregroundColor(.primary)
                }
            }
        }
    }
}
