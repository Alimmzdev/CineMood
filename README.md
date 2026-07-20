<div align="center">

# 🎬 CineMood

**Discover movies by your mood**

A **Kotlin Multiplatform (KMP)** movie browsing app for Iranian cinema — targeting Android, iOS, Desktop, and Web from a single shared codebase.

<br/>

| Platform | UI Technology | Status |
|:--------:|:-------------:|:------:|
| Android | Compose Multiplatform | ✅ |
| iOS | **Native SwiftUI** | ✅ |
| Desktop (JVM) | Compose Multiplatform | ✅ |
| Web (JS) | Compose Multiplatform | ✅ |
| Web (Wasm) | Compose Multiplatform | ✅ |

<br/>

<img src="https://img.shields.io/badge/Kotlin-2.4.0-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin"/>
<img src="https://img.shields.io/badge/SwiftUI-iOS_Native-FF6B35?style=for-the-badge&logo=swift&logoColor=white" alt="SwiftUI"/>
<img src="https://img.shields.io/badge/Compose_Multiplatform-1.11.1-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" alt="Compose"/>
<img src="https://img.shields.io/badge/Koin-4.2.1-6B4C9A?style=for-the-badge" alt="Koin"/>
<img src="https://img.shields.io/badge/License-MIT-green?style=for-the-badge" alt="License"/>

</div>

---

## 📖 About

CineMood is a **portfolio & learning project** built to push my boundaries as an Android developer:

- **Cross-platform mastery** — The shared Kotlin business logic (domain, data, presentation layers) powers **all five targets** through KMP, while the iOS app uses **native SwiftUI** consuming shared ViewModels via the `Shared` framework bridge.
- **SwiftUI through Agentic AI Development** — As an Android developer, the entire iOS SwiftUI layer (views, navigation, theming, ViewModel wrappers) was built using **AI-assisted (agentic) development** to rapidly learn and apply SwiftUI patterns. This project serves as a practical exercise in **Agentic AI Development** — leveraging AI agents to write, iterate, and ship production-quality SwiftUI code while deepening my understanding of the Apple ecosystem.
- **Clean Architecture** — Multi-module project with strict layer separation (domain → data → presentation), MVI pattern, and Koin DI — the same patterns I use in Android, now spanning platforms.

> 🎯 **Goal:** Improve my **agentic AI development skills** by using AI agents to build an unfamiliar platform (SwiftUI/iOS) while maintaining the architectural discipline of my Android roots.

---

## 🏗️ Architecture

Multi-module **Clean Architecture** with an **MVI** presentation pattern (`UiState` + `UiAction` → `MviViewModel`), wired together with **Koin** DI and **Navigation3**.

```
 androidApp   iosApp   desktopApp   webApp
      │          │         │           │
      └──────────┴────┬────┴───────────┘
                    shared
   ┌─────────────────┼───────────────────────┐
   ▼                 ▼                       ▼
 core:data      core:domain             core:navigation
 core:presentation                      core:presentation
   │                                         │
   │      ┌──────── feature:* ────────┐      │
   │      ▼  feature:home             │      │
   ├──► service:domain                │      │
   │      ▲                           │      │
   │      ├──service:data:iranianMoviesApi  │
   │      └──service:data:local            │
   │              (Room / SQLDelight)       │
   └─────────────────────────────────────────┘
```

### Top-level structure

```
CineMookKmp/
├── androidApp/          # Android entry point (Compose Multiplatform)
├── iosApp/              # iOS entry point (Xcode + SwiftUI)
│   └── iosApp/
│       ├── iOSApp.swift                         # App entry point (theme propagation)
│       ├── Theme/AppColors.swift                # SwiftUI theme colors
│       └── Presentation/
│           ├── Main/MainTabView.swift           # Tab-based navigation host
│           ├── App/
│           │   └── AppViewModelWrapper.swift    # Root theme bridge (KMP → SwiftUI)
│           ├── Home/
│           │   ├── HomeView.swift               # Featured carousel + trending grid
│           │   ├── HomeViewModelWrapper.swift   # KMP → SwiftUI bridge
│           │   ├── MovieDetailView.swift        # Movie detail screen
│           │   └── MovieDetailViewModelWrapper.swift
│           ├── Search/
│           │   ├── SearchView.swift             # Search with live results
│           │   └── SearchViewModelWrapper.swift
│           ├── Favorite/
│           │   ├── FavoriteView.swift           # Favorites list
│           │   └── FavoriteViewModelWrapper.swift
│           ├── Settings/
│           │   ├── SettingsView.swift           # Theme & preferences
│           │   └── SettingsViewModelWrapper.swift
│           └── Components/
│               └── MovieRow.swift               # Shared SwiftUI components
├── desktopApp/          # Desktop (JVM) entry point (Compose)
├── webApp/              # Web entry point (JS + Wasm, Compose)
├── shared/              # App shell: App(), AppViewModel, navigation host, Koin init, theme
├── core/                # Cross-cutting reusable layers
│   ├── domain/          # MVI base, entities, repository interfaces, BaseResult
│   ├── data/            # Ktor HttpClient, ThemeRepository (platform engines)
│   ├── navigation/      # Screen routes (Navigation3 NavKey)
│   └── presentation/    # MVI base, shared Compose UI components, theme
├── feature/             # Feature modules (screens + ViewModels + DI)
│   ├── home/            # Movie list + Movie detail
│   ├── search/          # Search with pagination
│   ├── favorite/        # Favorites grid
│   └── settings/        # Theme / app preferences
├── service/             # Backend-specific domain & data
│   ├── domain/          # Movie models, use cases, repositories (movies + liked videos)
│   └── data/
│       ├── iranianMoviesApi/   # Ktor client for moviesapi.ir
│       └── local/              # Local persistence (Room + SQLDelight)
├── build.gradle.kts     # Root build (plugin aliases, apply false)
├── settings.gradle.kts  # Module includes + repository config
├── gradle.properties    # Gradle / Kotlin / Android flags
└── gradle/
    └── libs.versions.toml   # Centralized version catalog
```

### Module responsibilities

| Module | Role |
|--------|------|
| `core/domain` | `BaseRepository`, `ThemeRepository`, entities (`Entity`, `DomainModel`, `ThemeMode`), `BaseResult`, `BaseUseCase`, `BaseException`, `CommonFlow` — **zero platform dependencies** |
| `core/data` | Platform `HttpClient` (expect/actual engines), `ThemeRepository` |
| `core/presentation` | **MVI base** (`MviViewModel`, `MviUiState`, `MviUiAction`), reusable Compose UI components (`MovieCard`, `CMNavigationBar`, `CMNavigationRail`, `CMTopAppBar`, `ErrorState`, `SharedMoviePoster`, `SystemAppearance`), Material 3 theming |
| `core/navigation` | Typed `Screen` destinations (`NavKey`) for Navigation3 |
| `feature/*` | Feature screens (Compose), ViewModels, and Koin modules — each follows the same MVI triad pattern |
| `service/domain` | Movie & liked-video models, use cases (`GetMoviesUseCase`, `SearchMoviesUseCase`, `GetLikedVideosUseCase`, `InsertLikedVideoUseCase`, `DeleteLikedVideoUseCase`), repository contracts |
| `service/data:iranianMoviesApi` | Remote API integration with [moviesapi.ir](https://moviesapi.ir) — DTOs, mappers, Ktor client |
| `service/data:local` | Local persistence — Room (Android, iOS, JVM) and SQLDelight (JS, Wasm) for liked videos |
| `shared` | Root `App()` composable, `AppViewModel` (theme + navigation), bottom bar / navigation rail, Koin `initKoin()`, shared-element transitions |
| `iosApp` | **Native SwiftUI** layer consuming shared KMP ViewModels via `KoinHelper` / `Shared` framework, `AppViewModelWrapper` for system-wide theme propagation |

---

## 🤖 KMP → SwiftUI Bridge

The iOS app uses **native SwiftUI** while sharing all business logic from KMP. The bridge pattern:

```
┌─────────────────────────────────────────────────────────┐
│  SwiftUI View Layer (Swift)                             │
│  HomeView · SearchView · FavoriteView · SettingsView     │
│       │ @StateObject wrapper                            │
│       ▼                                                 │
│  ViewModelWrapper (Swift)                               │
│  AppViewModelWrapper · HomeViewModelWrapper · ...        │
│       │ watches Kotlin StateFlow via FlowWatcher        │
│       ▼                                                 │
│  Shared Framework (Kotlin)                              │
│  MviViewModel<UiState, UiAction>  ← shared with Android │
│       │                                                 │
│       ▼                                                 │
│  KoinHelper (Kotlin iosMain)                             │
│  Resolves ViewModels → consumed from Swift               │
└─────────────────────────────────────────────────────────┘
```

**How it works:**
1. **`KoinHelper.kt`** (Kotlin `iosMain`) exposes factory methods that resolve ViewModels from Koin DI
2. **`ViewModelWrapper`** (Swift) creates the Kotlin ViewModel via `KoinHelper`, watches its `StateFlow` using `FlowWatcher`, and publishes state changes as `@Published` properties
3. **`AppViewModelWrapper`** (Swift) watches the shared `AppViewModel`'s `themeMode` state and applies it via `.preferredColorScheme(...)` at the app root
4. **SwiftUI views** use `@StateObject` to observe the wrapper and dispatch actions via `wrapper.dispatch(action)`

---

## 🛠️ Tech Stack

### Shared Kotlin Layer

| Category | Library / Version |
|----------|-------------------|
| Language | Kotlin **2.4.0** |
| UI (Android/Desktop/Web) | Compose Multiplatform **1.11.1** · Material 3 |
| Architecture | MVI (`UiState` / `UiAction` + `MviViewModel`) |
| DI | Koin **4.2.1** (`koin-compose`, `koin-compose-viewmodel`) |
| Navigation | Navigation3 **1.1.1** (`navigation3-ui`) |
| Networking | Ktor **3.5.0** · Kotlinx Serialization **1.11.0** |
| Images (Compose) | Coil 3 **3.4.0** (`coil-compose`, `coil-network-ktor3`) |
| Local data | SQLDelight **2.3.2** · AndroidX Room **2.8.4** |
| Async | Kotlin Coroutines **1.11.0** · Flow |
| Lifecycle | AndroidX Lifecycle **2.10.0** (`collectAsStateWithLifecycle`) |

### iOS Native Layer

| Category | Technology |
|----------|-----------|
| UI | **SwiftUI** (native) |
| Navigation | `NavigationStack` + `TabView` |
| Images | `AsyncImage` (built-in) |
| Theme | Custom `AppColors` + Material-style gradients |
| State | `@StateObject` / `@Published` / `FlowWatcher` |
| Bridge | `Shared` KMP framework via `KoinHelper` |

---

## ✨ Features

- 🏠 **Home** — Featured movie carousel with page indicators + trending grid with genre filter chips
- 🎞️ **Movie Detail** — Full detail view with poster, metadata, and cinematic gradient overlays
- 🔍 **Search** — Live search with pagination and empty/error states
- ❤️ **Favorites** — Saved-movies list with poster rows and empty state
- 👍 **Like / Unlike** — Persist liked movies locally across all platforms (Room on Android/iOS/JVM, SQLDelight on Web)
- ⚙️ **Settings** — Light / dark / system theme toggle
- 📐 **Adaptive Layout** — Bottom navigation on portrait/narrow; navigation rail on wide screens (Compose targets)
- 🌙 **Material 3 Theming** — Per-platform system bar styling with `SystemAppearance` + iOS `preferredColorScheme`
- ✨ **Shared Element Transitions** — Shared movie poster transitions between list and detail screens via `SharedTransitionLayout`
- 🔄 **KMP → SwiftUI Bridge** — Shared ViewModels consumed by native SwiftUI through `FlowWatcher` + `@Published` pattern

---

## 🤖 Agentic AI Development

> This project is a practical exercise in **Agentic AI Development**.

As an **Android developer**, I used AI agents to:

1. **Learn SwiftUI patterns** — Translating Compose Mental Models (modifiers → SwiftUI modifiers, `LazyColumn` → `List`/`ScrollView`, `collectAsStateWithLifecycle` → `@StateObject` + `FlowWatcher`)
2. **Build the iOS UI layer** — All SwiftUI views (`HomeView`, `SearchView`, `FavoriteView`, `SettingsView`, `MovieDetailView`) and reusable components (`FeaturedCard`, `PosterCard`, `MovieRow`, `PageIndicator`, `GenreChipRow`) were built iteratively with AI
3. **Implement the KMP → SwiftUI bridge** — Designed the `ViewModelWrapper` pattern to watch Kotlin `StateFlow` from Swift and expose it as `@Published` properties
4. **Maintain architectural consistency** — Ensured the SwiftUI layer follows the same MVI pattern (state + actions) as the Compose/Android layer

### Key lessons from agentic development on this project:

- **Context is king** — Providing clear project structure docs (`PROJECT_STRUCTURE.md`) and well-architected Kotlin code helped the AI generate more accurate SwiftUI code
- **Bridge patterns matter** — The `ViewModelWrapper` pattern emerged from iterative AI-assisted design, bridging two very different reactive systems (Kotlin Flow ↔ SwiftUI `@Published`)
- **Iterative refinement** — Starting with basic views, then progressively adding animations, accessibility, error states, and polish through multiple agent iterations
- **Platform idioms over 1:1 translation** — Learning when to use native SwiftUI patterns (like `GeometryReader` for featured cards, `Capsule` chips) vs. trying to replicate Compose patterns literally

---

## 🚀 Getting Started

### Prerequisites

- **JDK 11+**
- **Android Studio** Ladybug or newer with KMP & Compose Multiplatform support
- **Xcode 15+** (for iOS)
- **Node.js** (optional; used by Kotlin/JS and Wasm web toolchains)

### Clone

```bash
git clone https://github.com/Alimmzdev/CineMood.git
cd CineMood   # local folder may be named CineMookKmp
```

### Build & Run

```bash
# Android
./gradlew :androidApp:installDebug

# Desktop (JVM)
./gradlew :desktopApp:run

# Web — Wasm (recommended)
./gradlew :webApp:wasmJsBrowserDevelopmentRun

# Web — JS (broader browser support)
./gradlew :webApp:jsBrowserDevelopmentRun
```

### iOS

Open `iosApp/iosApp.xcodeproj` in Xcode 15+, then run on a simulator or device.

> The iOS target requires the `Shared` framework, which is built automatically by the `:shared` Gradle module (targets `iosArm64`, `iosSimulatorArm64`).

---

## 📄 License

This project is released under the [MIT License](LICENSE). Fork, study, and reuse freely — including in commercial apps — as long as you keep the copyright notice and license text.

Third-party libraries and the [moviesapi.ir](https://moviesapi.ir) API remain subject to their own terms.

---

<div align="center">

**Built with ❤️ by an Android developer, exploring SwiftUI through Agentic AI Development**

</div>
