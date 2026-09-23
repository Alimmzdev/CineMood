# 📁 CineMookKmp — Project Structure

> **CineMood** — *Discover movies by your mood*
> A Kotlin Multiplatform app for browsing Iranian cinema, built with Compose Multiplatform.

Gradle root project: **CineMookKmp** · Base package: `dev.alimmz.cinemood`

---

## 1. Overview

CineMookKmp is a **Kotlin Multiplatform (KMP)** project that ships a single movie‑browsing
experience to **five targets** from one shared codebase:

| Target          | UI technology                                       |
|-----------------|-----------------------------------------------------|
| Android         | Compose Multiplatform (`androidApp`)                |
| iOS             | **Native SwiftUI** (`iosApp`) calling shared KMP    |
| Desktop (JVM)   | Compose Multiplatform (`desktopApp`)                |
| Web (JS)        | Compose Multiplatform via `ComposeViewport` (`webApp`) |
| Web (Wasm)      | Compose Multiplatform via `ComposeViewport` (`webApp`) |

> **Note:** iOS was migrated from Compose Multiplatform to native SwiftUI (see commit `4cb8033`).
> The Kotlin side still builds an `iosArm64` / `iosSimulatorArm64` framework named **`Shared`**,
> whose ViewModels and use cases are consumed from Swift via `KoinHelper` / `MainViewController`.

### Architecture in one line

Multi‑module **Clean Architecture** (domain / data / presentation) with an **MVI** presentation
pattern (`UiState` + `UiAction` → `MviViewModel`), wired together with **Koin** DI and
**Navigation3**.

---

## 2. Top-level layout

```
CineMookKmp/
├── androidApp/          # Android entry point (Compose)
├── iosApp/              # iOS entry point (Xcode + SwiftUI)
├── desktopApp/          # Desktop (JVM) entry point (Compose/Swing)
├── webApp/              # Web entry point (JS + Wasm, Compose)
├── shared/              # App shell: App(), navigation host, Koin init, theme
├── core/                # Cross-cutting reusable layers
│   ├── domain/          # MVI base, entities, repository interfaces, BaseResult
│   ├── data/            # Ktor HttpClient, ThemeRepository, SQLDelight/Room DB
│   ├── navigation/      # Screen routes (Navigation3 NavKey)
│   └── presentation/    # Shared Compose UI components + theme
├── feature/             # Feature modules (screens + ViewModels + DI)
│   ├── home/            # Movie list + Movie detail
│   ├── search/          # Search with pagination
│   ├── favorite/        # Favorites grid
│   └── settings/        # Theme / app preferences
├── service/             # Backend-specific domain & data
│   ├── domain/          # Movie models, use cases, MoviesRepository contract
│   └── data/
│       └── iranianMoviesApi/   # Ktor client for moviesapi.ir
├── build.gradle.kts     # Root build (plugin aliases, apply false)
├── settings.gradle.kts  # Module includes + repository config
├── gradle.properties    # Gradle / Kotlin / Android flags
└── gradle/
    └── libs.versions.toml   # Centralized version catalog
```

---

## 3. Module graph

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
   │      └────service:data:iranianMoviesApi─
   │                 │
   └─────────────────┘  (HTTP client from core:data)
```

`shared` `api`-exposes all `core:*`, `feature:*` and `service:*` modules, so each thin entry‑point
app only depends on `:shared` (and on iOS, on the exported `Shared` framework).

---

## 4. Module-by-module breakdown

### 4.1 Entry-point apps (thin shells)

Each app does only: bootstrap Koin → render the shared `App()` composable (Android/Desktop/Web)
or wire the SwiftUI host (iOS).

| Module        | Key file                                                | Responsibility |
|---------------|---------------------------------------------------------|----------------|
| `androidApp`  | `MainActivity.kt`, `CineMoodApplication.kt`             | Edge-to-edge `ComponentActivity`, `setContent { App() }` |
| `desktopApp`  | `main.kt`                                               | Swing `Window { App() }`, calls `initKoin()` |
| `webApp`      | `main.kt`                                               | `ComposeViewport { App() }` for JS + Wasm, calls `initKoin()` |
| `iosApp`      | `iOSApp.swift`, `MainTabView.swift`, `HomeView.swift`   | SwiftUI app; consumes `Shared` framework via `HomeViewModelWrapper` |

> iOS: Koin is started from Kotlin via `startKoin()` in `MainViewController.kt` /
> `KoinHelper.kt`, then SwiftUI reads shared ViewModels.

### 4.2 `shared` — application shell

Path: `shared/src/commonMain/kotlin/dev/alimmz/cinemood/`

| File | Purpose |
|------|---------|
| `App.kt` | Root composable: `Scaffold`, adaptive bottom bar vs. navigation rail, `NavDisplay` with `SharedTransitionLayout`, declares all `entry<Screen.*>` destinations |
| `di/Koin.kt` | `initKoin()` — aggregates every module (coreData, iranianMoviesApi, domain, presentation, feature ×4) |
| `navigation/MovieDetailNavTransitions.kt` | Shared-element size transform + nav metadata for movie detail |
| `theme/ThemeState.kt` | Mutable theme state (light/dark/system) consumed by `MyKMPAppTheme` |
| `presentation/app/AppViewModel.kt`, `AppUiState.kt`, `AppUiAction.kt` | Top-level MVI: tracks `currentScreen` + `themeMode`, handles `BottomNavSelected` |

iOS-specific: `MainViewController.kt`, `util/KoinHelper.kt`.

### 4.3 `core` — reusable layers

#### `core:domain` — pure Kotlin contracts
`core/domain/src/commonMain/.../core/domain/`

- `repository/` — `BaseRepository`, `ThemeRepository` (interface)
- `entity/` — `Entity`, `DomainModel`, `ThemeMode`
- `common/BaseResult.kt` — `Success` / `Error` sealed result used by repositories
- `usecase/BaseUseCase.kt`, `util/CommonFlow.kt`, `exception/BaseException.kt`

> No Android/Compose dependencies — safe to depend on from any target.

#### `core:data` — infrastructure (multi-platform via `expect/actual`)
`core/data/src/`

- `commonMain`
  - `network/PlatformHttpClient.kt` — `HttpClientFactory.create()` (ContentNegotiation JSON, Logging, 30s timeouts); `expect fun provideEngine()`
  - `network/NetworkConstants.kt`, `network/dto/DomainConvertible.kt`
  - `repository/ThemeRepositoryImpl.kt`
  - `db/DriverFactory.kt` — `expect` SQLDelight driver factory
  - `di/CoreDataModule.kt` — registers `ThemeRepository`, `CineMoodDatabase`, includes `platformModule()`
  - `sqldelight/.../CineMoodDatabase.sq` — favorites schema
- Per-target `actual`s: `androidMain`, `iosMain`, `jvmMain`, `jsMain`, `wasmJsMain`, `webMain` → engines (OkHttp / Darwin / JS / Wasm) and drivers (Android / Native / SQLite / Web Worker)

#### `core:navigation` — routes
`Screen.kt` — `sealed interface Screen : NavKey` with `Home`, `Search`, `Favorite`, `Settings`, and `MovieDetail(movieId, movieTitle, moviePoster, posterCornerRadiusDp)`. All `@Serializable` for type-safe Navigation3.

#### `core:presentation` — shared UI kit
`core/presentation/src/commonMain/.../core/presentation/`

- `mvi/` — `MviViewModel<UiState, UiAction>`, `MviUiState`, `MviUiAction` (the base every feature extends)
- `components/`
  - `MovieCard.kt`, `SharedMoviePoster.kt` (shared-element poster)
  - `CMNavigationBar.kt`, `CMNavigationRail.kt` (adaptive nav chrome)
  - `CMTopAppBar.kt`, `ErrorState.kt`
  - `SharedTransitionAnimations.kt`
  - `SystemAppearance.kt` (`expect`/`actual` per platform — status bar styling)
- `theme/Theme.kt` — Material 3 `MyKMPAppTheme`

### 4.4 `feature` — feature modules

Each feature follows the same MVI triad + a Koin module:

```
feature/<name>/src/commonMain/.../feature/<name>/
├── <Name>Screen.kt            # Compose screen (shared transitions aware)
├── di/<Name>Module.kt         # Koin module: viewModelOf(::<Name>ViewModel)
└── presentation/
    ├── <Name>UiState.kt       # MviUiState
    ├── <Name>UiAction.kt      # MviUiAction (LoadMovies, Refresh, LoadNextPage, ...)
    └── <Name>ViewModel.kt     # extends MviViewModel
```

| Feature      | Screens / ViewModels | Notes |
|--------------|----------------------|-------|
| `home`       | `HomeScreen`, `MovieDetailScreen`, `HomeViewModel`, `MovieDetailViewModel` | Paginated catalog + shared-element detail |
| `search`     | `SearchScreen`, `SearchViewModel` | Query with pagination |
| `favorite`   | `FavoriteScreen`, `FavoriteViewModel`, `model/FavoriteMovieItem` | Favorites grid |
| `settings`   | `SettingsScreen`, `SettingsViewModel` | Light / dark / system theme via `ThemeRepository` |

### 4.5 `service` — moviesapi.ir integration

Split by Clean Architecture layer:

- `service:domain` (`service/domain/.../service/domain/`)
  - `moodel/` *(sic)* — `Movie`, `MovieDetail`, `MoviesPage`
  - `repository/MoviesRepository.kt` — contract returning `Flow<BaseResult<...>>`
  - `usecase/` — `GetMoviesUseCase`, `GetMovieDetailUseCase`, `SearchMoviesUseCase`
- `service:data:iranianMoviesApi` (`service/data/iranianMoviesApi/.../iranianmoviesapi/`)
  - `dto/` — `MovieDto`, `MovieDetailDto`, `MoviesResponseDto`, `MetadataDto`
  - `datasource/` — `MoviesRemoteDataSource` (interface) + `MoviesRemoteDataSourceImpl` (Ktor)
  - `repository/MoviesRepositoryImpl.kt` — maps DTOs → domain, centralizes error messages (`asUiMessage()`)
  - `util/MoviesApiUrlResolver.kt`
  - `di/MoviesApiModule.kt`

---

## 5. Key patterns

### MVI base (`core:presentation`)
```kotlin
abstract class MviViewModel<UiState : MviUiState, UiAction : MviUiAction>(
    initialState: UiState,
) : ViewModel() {
    val uiState: StateFlow<UiState>
    protected fun updateState(reducer: UiState.() -> UiState)
    abstract fun onAction(action: UiAction)
}
```
Features extend it; the UI collects `uiState` with `collectAsStateWithLifecycle()` and dispatches
actions via `onAction(...)`.

### Dependency Injection (Koin 4)
- `initKoin()` in `shared` aggregates **all** modules.
- Platform specifics come from `expect fun platformModule(): Module` (in `core:data`), with
  `actual`s providing engines and DB drivers.
- ViewModels are registered with `viewModelOf { }` / `viewModel { parameters -> }` and resolved in
  Compose via `koinViewModel()`.

### Navigation (Navigation3)
- A single `rememberNavBackStack` of `Screen : NavKey` lives in `App.kt`.
- Bottom navigation (portrait/narrow) ↔ Navigation rail (wide) chosen by `BoxWithConstraints`.
- Movie detail uses a `SharedTransitionLayout` + `sharedNavSizeTransform()` for poster morph.

### Networking (Ktor 3)
- `HttpClientFactory.create()` installs `ContentNegotiation` (kotlinx.serialization JSON),
  `Logging`, `HttpTimeout` (30 s).
- Engine is `expect provideEngine()` → OkHttp (Android/JVM), Darwin (iOS), JS & Wasm engines (Web).

### Local storage
- **SQLDelight** schema in `core:data` (`CineMoodDatabase.sq`) with per-platform drivers.
- **AndroidX Room** also configured (plugin + runtime) for native targets.

---

## 6. Tech stack (from `gradle/libs.versions.toml`)

| Category    | Library | Version |
|-------------|---------|---------|
| Language    | Kotlin | `2.4.0` |
| UI          | Compose Multiplatform | `1.11.1` |
| Material 3  | material3 | `1.10.0-alpha05` |
| DI          | Koin | `4.2.1` |
| Navigation  | Navigation3 (`navigation3-ui`) | `1.1.1` |
| Networking  | Ktor | `3.5.0` |
|             | OkHttp | `5.3.2` |
| Serialization | kotlinx.serialization | `1.11.0` |
| Images      | Coil 3 | `3.4.0` |
| Local DB    | SQLDelight | `2.3.2` |
|             | AndroidX Room | `2.8.4` |
| Async       | kotlinx.coroutines | `1.11.0` |
| Lifecycle   | AndroidX Lifecycle | `2.10.0` |
| Tooling     | AGP | `9.2.1` (compileSdk 37, minSdk 24, targetSdk 36) |

Gradle plugins (all applied `false` at root, aliased per module):
`androidApplication`, `androidMultiplatformLibrary`, `androidLint`, `kotlinMultiplatform`,
`kotlinJvm`, `composeMultiplatform`, `composeCompiler`, `composeHotReload`, `kotlinxSerialization`,
`ksp`, `androidx.room`, `sqldelight`.

---

## 7. Build & run

```bash
# Android
./gradlew :androidApp:installDebug

# Desktop (JVM)
./gradlew :desktopApp:run

# Web — Wasm (recommended)
./gradlew :webApp:wasmJsBrowserDevelopmentRun

# Web — JS (broader browser support)
./gradlew :webApp:jsBrowserDevelopmentRun

# iOS
# Open iosApp/iosApp.xcodeproj in Xcode 15+, then run on simulator/device.
```

Shared framework for iOS is produced by the `:shared` module as a static framework named **`Shared`**
(targets `iosArm64`, `iosSimulatorArm64`).

---

## 8. Conventions

- **Source-set layout:** `commonMain` + per-target `actual`s (`androidMain`, `iosMain`,
  `jvmMain`, `jsMain`, `wasmJsMain`, `webMain`). Tests use `commonTest`, `androidHostTest`,
  `androidDeviceTest`.
- **Package root:** `dev.alimmz.cinemood.*` (a few legacy files still use
  `com.alimmzdev.cinemood.*`).
- **Clean layering:** `domain` never depends on `data`/UI; `data` implements `domain` interfaces;
  features depend on `domain` + `presentation` only.
- **Reusability first:** shared UI components live in `core:presentation`; feature screens stay thin.

---

*Generated from the repository source. If the module set in `settings.gradle.kts` changes, update
this document accordingly.*
