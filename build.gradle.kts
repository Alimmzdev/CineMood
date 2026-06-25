plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.composeHotReload) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.androidMultiplatformLibrary) apply false
    alias(libs.plugins.androidLint) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinxSerialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.androidx.room) apply false
    alias(libs.plugins.sqldelight) apply false
}

val wasmYarnLockPath: String = "${rootProject.layout.buildDirectory.get().asFile.absolutePath}/wasm/yarn.lock"

tasks.matching { task -> task.name == "kotlinWasmStoreYarnLock" }.configureEach {
    doFirst {
        val wasmYarnLockFile = java.io.File(wasmYarnLockPath)
        if (!wasmYarnLockFile.parentFile.exists()) {
            wasmYarnLockFile.parentFile.mkdirs()
        }
        if (!wasmYarnLockFile.exists()) {
            wasmYarnLockFile.writeText("")
        }
    }
}

subprojects {
    plugins.withId("org.jetbrains.kotlin.plugin.compose") {
        extensions.configure<org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension> {
            stabilityConfigurationFiles.add(
                rootProject.layout.projectDirectory.file("compose_compiler_config.conf")
            )
            reportsDestination = layout.buildDirectory.dir("compose_compiler")
        }
    }
}