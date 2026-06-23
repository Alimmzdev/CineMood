import SwiftUI
import Shared

struct SettingsView: View {
    @StateObject private var wrapper = SettingsViewModelWrapper()

    var body: some View {
        Form {
            Section("Appearance") {
                Picker("Theme", selection: themeBinding) {
                    Text("System").tag(ThemeMode.system)
                    Text("Light").tag(ThemeMode.light)
                    Text("Dark").tag(ThemeMode.dark)
                }
                .pickerStyle(.segmented)
            }

            Section("Notifications") {
                Toggle("Enable notifications", isOn: notificationsBinding)
            }

            Section("About") {
                LabeledContent("App", value: "CineMood")
                LabeledContent("Version", value: appVersion)
            }
        }
        .scrollContentBackground(.hidden)
        .navigationTitle("Settings")
        .navigationBarTitleDisplayMode(.large)
    }

    // MARK: - Bindings

    /// Two-way binding that dispatches [SettingsUiActionThemeModeSelected]
    /// whenever the user picks a new theme.
    private var themeBinding: Binding<ThemeMode> {
        Binding(
            get: { wrapper.state.themeMode },
            set: { newMode in
                wrapper.dispatch(SettingsUiActionThemeModeSelected(themeMode: newMode))
            }
        )
    }

    private var notificationsBinding: Binding<Bool> {
        Binding(
            get: { wrapper.state.notificationsEnabled },
            set: { enabled in
                wrapper.dispatch(SettingsUiActionNotificationsToggled(enabled: enabled))
            }
        )
    }

    private var appVersion: String {
        let version = Bundle.main.infoDictionary?["CFBundleShortVersionString"] as? String ?? "1.0"
        let build = Bundle.main.infoDictionary?["CFBundleVersion"] as? String ?? "1"
        return "\(version) (\(build))"
    }
}
