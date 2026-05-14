rootProject.name = "CineMookKmp"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
//        google {
//            mavenContent {
//                includeGroupAndSubgroups("androidx")
//                includeGroupAndSubgroups("com.android")
//                includeGroupAndSubgroups("com.google")
//            }
//        }
//        mavenCentral()
//        gradlePluginPortal()
        maven {
            url = uri("https://maven.myket.ir")
        }
    }
}

dependencyResolutionManagement {
    repositories {
//        google {
//            mavenContent {
//                includeGroupAndSubgroups("androidx")
//                includeGroupAndSubgroups("com.android")
//                includeGroupAndSubgroups("com.google")
//            }
//        }
//        mavenCentral()
        maven {
            url = uri("https://maven.myket.ir")
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":composeApp")
include(":core:domain")
include(":core:data")
include(":service:data")
include(":service:data:iranianMoviesApi")
include(":service:domain")
include(":feature:search")
include(":feature:home")
include(":feature:favorite")
include(":feature:settings")
