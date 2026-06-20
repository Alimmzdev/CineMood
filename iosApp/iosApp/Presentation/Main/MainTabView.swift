import SwiftUI

struct MainTabView: View {
    @State private var selectedTab: Int = 0
    
    var body: some View {
        TabView(selection: $selectedTab) {
            HomeView()
                .tabItem {
                    Label("Home", systemImage: "house.fill")
                }
                .tag(0)
            
            // Placeholder for other screens
            Text("Search Screen")
                .tabItem {
                    Label("Search", systemImage: "magnifyingglass")
                }
                .tag(1)
            
            Text("Favorite Screen")
                .tabItem {
                    Label("Favorite", systemImage: "heart.fill")
                }
                .tag(2)
            
            Text("Settings Screen")
                .tabItem {
                    Label("Settings", systemImage: "gearshape.fill")
                }
                .tag(3)
        }
        .accentColor(AppColors.primary) // Material Primary Color
    }
}
