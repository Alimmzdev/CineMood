import SwiftUI
import Shared

@main
struct iOSApp: App {
    init() {
        MainViewControllerKt.startKoin()
    }

    var body: some Scene {
        WindowGroup {
            MainTabView()
        }
    }
}
