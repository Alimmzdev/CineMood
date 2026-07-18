import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
            export(projects.feature.home)
            export(projects.feature.search)
            export(projects.feature.favorite)
            export(projects.feature.settings)
            export(projects.service.domain)
            export(projects.core.domain)
            export(libs.jetbrains.kotlinx.coroutines.core)
        }
    }

    jvm()

    js {
        browser()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    android {
        namespace = "tech.nullexdev.conemood_kmp"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
        androidResources {
            enable = true
        }

        withHostTest {
            isIncludeAndroidResources = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.core.data)
            api(projects.core.domain)
            api(projects.core.navigation)
            api(projects.core.presentation)
            api(projects.service.data.iranianMoviesApi)
            api(projects.service.data.local)
            api(projects.service.domain)
            api(projects.feature.home)
            api(projects.feature.search)
            api(projects.feature.favorite)
            api(projects.feature.settings)

            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.material.icons.extended)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(libs.navigation3.ui)
        }

        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
        }

        jsMain.dependencies {
            implementation(libs.wrappers.browser)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}