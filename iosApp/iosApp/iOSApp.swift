import SwiftUI
import Shared

@main
struct iOSApp: App {

    init() {
        MainViewControllerKt.startKoin()
    }

    @StateObject private var appWrapper = AppViewModelWrapper()

    var body: some Scene {
        WindowGroup {
            MainTabView()
                .preferredColorScheme(colorScheme(for: appWrapper.themeMode))
        }
    }

    /// Maps the shared `ThemeMode` to SwiftUI's `ColorScheme`.
    /// `.system` follows the device appearance (returns `nil`).
    private func colorScheme(for mode: ThemeMode) -> ColorScheme? {
        switch mode {
        case .light:  return .light
        case .dark:   return .dark
        case .system: return nil
        default:      return nil
        }
    }
}
