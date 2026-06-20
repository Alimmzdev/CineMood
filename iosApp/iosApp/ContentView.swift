import SwiftUI

struct ContentView: View {
    @State private var selectedTab = 0
    @State private var searchText = ""
    
    var body: some View {
        TabView {
            Tab("Home", systemImage: "house.fill") {
                HomeView()
            }

            Tab("Favorites", systemImage: "heart.fill") {
                Text("Profile View")
                    .foregroundColor(.white)
            }

            Tab("Settings", systemImage: "gearshape.fill") {
                Text("Profile View")
                    .foregroundColor(.white)
            }

            Tab("Search", systemImage: "magnifyingglass", role: .search) {
                NavigationStack {
                    List {
                        
                    }
                    .navigationTitle("Search")
                    .searchable(text: $searchText, placement: .toolbar, prompt: Text("Search ..."))
                }
            }
        }
        .tint(AppColors.primary)
        .background(.ultraThinMaterial)
        .toolbarBackground(.ultraThinMaterial, for: .tabBar)
        .toolbarBackground(.visible, for: .tabBar)
        .accentColor(AppColors.primary)
    }
}
