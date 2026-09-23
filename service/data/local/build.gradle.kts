import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.androidLint)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.sqldelight)
}

kotlin {
    compilerOptions {
        // Room's database constructor uses an expect object and generated actual objects.
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    android {
        namespace = "dev.alimmz.cinemood.service.data.local"
        compileSdk = 37
        minSdk = 24
    }

    iosArm64()
    iosSimulatorArm64()
    jvm()
    js {
        browser()
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    applyDefaultHierarchyTemplate()

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":service:domain"))
                implementation(libs.koin.core)
            }
        }

        val roomMain by creating {
            dependsOn(commonMain.get())
            dependencies {
                implementation(libs.androidx.room.runtime)
                implementation(libs.androidx.sqlite.bundled)
            }
        }

        androidMain {
            dependsOn(roomMain)
        }

        iosMain {
            dependsOn(roomMain)
        }

        jvmMain {
            dependsOn(roomMain)
        }

        jsMain {
            dependencies {
                implementation(npm("@cashapp/sqldelight-sqljs-worker", "2.3.2"))
                implementation(npm("sql.js", "1.14.1"))
                implementation(libs.wrappers.browser)
                implementation(libs.sqldelight.coroutines.extensions)
                implementation(libs.sqldelight.web.worker.driver)
            }
        }

        wasmJsMain {
            dependencies {
                implementation(npm("@cashapp/sqldelight-sqljs-worker", "2.3.2"))
                implementation(npm("sql.js", "1.14.1"))
                implementation(libs.wrappers.browser)
                implementation(libs.sqldelight.coroutines.extensions)
                implementation(libs.sqldelight.web.worker.driver.wasm.js)
            }
        }
    }
}

dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspJvm", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}

sqldelight {
    databases {
        create("CineMoodDatabase") {
            packageName.set("dev.alimmz.cinemood.service.data.local.db")
            generateAsync.set(true)
        }
    }
}
