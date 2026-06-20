import SwiftUI

struct AppColors {
    static var primary: Color {
        Color(light: hex(0xFFFF0000), dark: hex(0xFFFF0000))
    }
    
    static var background: Color {
        Color(light: hex(0xFFFFFBFF), dark: hex(0xFF1C1C1C))
    }
    
    static var surface: Color {
        Color(light: hex(0xFFFFFBFF), dark: hex(0xFF1C1C1C))
    }
    
    static var onSurface: Color {
        Color(light: hex(0xFF201A19), dark: hex(0xFFEDE0DE))
    }
    
    static var secondary: Color {
        Color(light: hex(0xFF775652), dark: hex(0xFFE7BDB8))
    }

    private static func hex(_ val: UInt) -> Color {
        Color(hex: val)
    }
}

extension Color {
    init(light: Color, dark: Color) {
        self.init(uiColor: UIColor { traitCollection in
            traitCollection.userInterfaceStyle == .dark ? UIColor(dark) : UIColor(light)
        })
    }

    init(hex: UInt, alpha: Double = 1) {
        self.init(
            .sRGB,
            red: Double((hex >> 16) & 0xff) / 255,
            green: Double((hex >> 08) & 0xff) / 255,
            blue: Double((hex >> 00) & 0xff) / 255,
            opacity: alpha
        )
    }
}
