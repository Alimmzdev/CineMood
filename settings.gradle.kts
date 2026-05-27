rootProject.name = "CineMookKmp"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":androidApp")
include(":shared")
include(":webApp")
include(":desktopApp")
include(":core:domain")
include(":core:data")
include(":service:data")
include(":service:data:iranianMoviesApi")
include(":service:domain")
include(":feature:search")
include(":feature:home")
include(":feature:favorite")
include(":feature:settings")
include(":core:navigation")
