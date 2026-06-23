import SwiftUI
import Shared

struct SettingsView: View {
    @StateObject private var wrapper = SettingsViewModelWrapper()
    @Namespace private var themeAnimation

    var body: some View {
        ScrollView {
            VStack(spacing: 24) {
                appearanceSection
                notificationsSection
                aboutSection
            }
            .padding(.horizontal, 20)
            .padding(.top, 8)
            .padding(.bottom, 32)
        }
        .background(Color(.systemGroupedBackground).ignoresSafeArea())
        .navigationTitle("Settings")
        .navigationBarTitleDisplayMode(.large)
    }

    // MARK: - Appearance

    private var appearanceSection: some View {
        SettingsSection(title: "Appearance", icon: "paintbrush.fill", iconColor: .purple) {
            VStack(spacing: 0) {
                Text("Theme")
                    .font(.subheadline)
                    .foregroundStyle(.secondary)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.horizontal, 16)
                    .padding(.top, 14)
                    .padding(.bottom, 10)

                HStack(spacing: 10) {
                    ForEach(ThemeOption.allCases) { option in
                        ThemeButton(
                            option: option,
                            isSelected: wrapper.state.themeMode == option.mode
                        ) {
                            wrapper.dispatch(SettingsUiActionThemeModeSelected(themeMode: option.mode))
                        }
                    }
                }
                .padding(.horizontal, 16)
                .padding(.bottom, 16)
            }
        }
    }

    // MARK: - Notifications

    private var notificationsSection: some View {
        SettingsSection(title: "Notifications", icon: "bell.fill", iconColor: .orange) {
            SettingsToggleRow(
                icon: "bell.badge.fill",
                iconColor: .orange,
                title: "Enable Notifications",
                subtitle: "Get updates about new releases",
                isOn: notificationsBinding
            )
        }
    }

    // MARK: - About

    private var aboutSection: some View {
        SettingsSection(title: "About", icon: "info.circle.fill", iconColor: AppColors.primary) {
            VStack(spacing: 0) {
                AboutRow(icon: "🎬", title: "App", value: "CineMood")
                Divider().padding(.leading, 52)
                AboutRow(icon: "🔖", title: "Version", value: appVersion)
                Divider().padding(.leading, 52)
                AboutRow(icon: "👨‍💻", title: "Developer", value: "Nullexdev")
            }
        }
    }

    // MARK: - Bindings

    private var themeBinding: Binding<ThemeMode> {
        Binding(
            get: { wrapper.state.themeMode },
            set: { wrapper.dispatch(SettingsUiActionThemeModeSelected(themeMode: $0)) }
        )
    }

    private var notificationsBinding: Binding<Bool> {
        Binding(
            get: { wrapper.state.notificationsEnabled },
            set: { wrapper.dispatch(SettingsUiActionNotificationsToggled(enabled: $0)) }
        )
    }

    private var appVersion: String {
        let version = Bundle.main.infoDictionary?["CFBundleShortVersionString"] as? String ?? "1.0"
        let build   = Bundle.main.infoDictionary?["CFBundleVersion"] as? String ?? "1"
        return "\(version) (\(build))"
    }
}

// MARK: - Section Container

private struct SettingsSection<Content: View>: View {
    let title: String
    let icon: String
    let iconColor: Color
    @ViewBuilder let content: Content

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            HStack(spacing: 8) {
                Image(systemName: icon)
                    .font(.system(size: 12, weight: .semibold))
                    .foregroundStyle(iconColor)
                Text(title.uppercased())
                    .font(.system(size: 11, weight: .semibold))
                    .foregroundStyle(.secondary)
                    .kerning(0.8)
            }
            .padding(.leading, 4)
            .padding(.bottom, 8)

            content
                .background(Color(.secondarySystemGroupedBackground))
                .clipShape(RoundedRectangle(cornerRadius: 16))
        }
    }
}

// MARK: - Theme Picker Button

private enum ThemeOption: String, CaseIterable, Identifiable {
    case system, light, dark
    var id: String { rawValue }

    var mode: ThemeMode {
        switch self {
        case .system: return .system
        case .light:  return .light
        case .dark:   return .dark
        }
    }
    var icon: String {
        switch self {
        case .system: return "circle.lefthalf.filled"
        case .light:  return "sun.max.fill"
        case .dark:   return "moon.fill"
        }
    }
    var label: String { rawValue.capitalized }
}

private struct ThemeButton: View {
    let option: ThemeOption
    let isSelected: Bool
    let onTap: () -> Void

    var body: some View {
        Button(action: onTap) {
            VStack(spacing: 8) {
                ZStack {
                    RoundedRectangle(cornerRadius: 12)
                        .fill(isSelected ? AppColors.primary : Color(.tertiarySystemGroupedBackground))
                        .frame(height: 52)

                    Image(systemName: option.icon)
                        .font(.system(size: 20, weight: .medium))
                        .foregroundStyle(isSelected ? .white : .secondary)
                }

                Text(option.label)
                    .font(.caption.weight(isSelected ? .semibold : .regular))
                    .foregroundStyle(isSelected ? AppColors.primary : .secondary)
            }
            .frame(maxWidth: .infinity)
            .scaleEffect(isSelected ? 1.03 : 1.0)
            .animation(.spring(response: 0.3, dampingFraction: 0.7), value: isSelected)
        }
        .buttonStyle(.plain)
    }
}

// MARK: - Toggle Row

private struct SettingsToggleRow: View {
    let icon: String
    let iconColor: Color
    let title: String
    let subtitle: String
    @Binding var isOn: Bool

    var body: some View {
        HStack(spacing: 14) {
            ZStack {
                RoundedRectangle(cornerRadius: 8)
                    .fill(iconColor.opacity(0.15))
                    .frame(width: 36, height: 36)
                Image(systemName: icon)
                    .font(.system(size: 15, weight: .semibold))
                    .foregroundStyle(iconColor)
            }

            VStack(alignment: .leading, spacing: 2) {
                Text(title)
                    .font(.body)
                Text(subtitle)
                    .font(.caption)
                    .foregroundStyle(.secondary)
            }

            Spacer()

            Toggle("", isOn: $isOn)
                .labelsHidden()
                .tint(AppColors.primary)
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 12)
    }
}

// MARK: - About Row

private struct AboutRow: View {
    let icon: String
    let title: String
    let value: String

    var body: some View {
        HStack(spacing: 14) {
            Text(icon)
                .font(.system(size: 20))
                .frame(width: 36, height: 36)

            Text(title)
                .font(.body)
                .foregroundStyle(.primary)

            Spacer()

            Text(value)
                .font(.body)
                .foregroundStyle(.secondary)
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 12)
    }
}
