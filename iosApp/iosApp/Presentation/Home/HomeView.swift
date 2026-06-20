import SwiftUI
import Shared

struct HomeView: View {
    @StateObject private var wrapper = HomeViewModelWrapper()
    
    var body: some View {
        NavigationView {
            Group {
                if wrapper.state.isLoading && wrapper.state.movies.isEmpty {
                    ProgressView("Loading movies...")
                } else if let error = wrapper.state.errorMessage {
                    VStack {
                        Text(error)
                            .foregroundColor(.red)
                        Button("Retry") {
                            wrapper.dispatch(HomeUiActionLoadMovies.shared)
                        }
                    }
                } else {
                    List(wrapper.state.movies, id: \.id) { movie in
                        HStack {
                            AsyncImage(url: URL(string: movie.poster)) { image in
                                image.resizable()
                                    .aspectRatio(contentMode: .fill)
                            } placeholder: {
                                Color.gray
                            }
                            .frame(width: 60, height: 90)
                            .cornerRadius(8)
                            .clipped()
                            
                            VStack(alignment: .leading) {
                                Text(movie.title)
                                    .font(.headline)
                                Text("ID: \(movie.id)")
                                    .font(.subheadline)
                                    .foregroundColor(.secondary)
                            }
                        }
                    }
                    .refreshable {
                        wrapper.dispatch(HomeUiActionRefresh.shared)
                    }
                }
            }
            .navigationTitle("Home")
            .onAppear {
                wrapper.dispatch(HomeUiActionLoadMovies.shared)
            }
        }
    }
}
