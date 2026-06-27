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
    android {
        namespace = "tech.nullexdev.cinemood.service.data.local"
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

    sourceSets {
        val webMain by creating {
            dependsOn(commonMain.get())
            dependencies {
                implementation(libs.sqldelight.web.worker.driver)
                implementation(devNpm("sql.js", "1.12.0"))
                implementation(libs.wrappers.browser)
            }
        }

        commonMain {
            dependencies {
                implementation(project(":service:domain"))
                implementation(libs.koin.core)
                implementation(libs.sqldelight.coroutines.extensions)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.androidx.room.runtime)
            }
        }

        iosMain {
            dependencies {
                implementation(libs.sqldelight.ios.driver)
            }
        }

        jvmMain {
            dependencies {
                implementation(libs.androidx.room.runtime)
                implementation(libs.androidx.sqlite.bundled)
            }
        }

        jsMain {
            dependsOn(webMain)
        }

        wasmJsMain {
            dependsOn(webMain)
        }
    }
}

dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspJvm", libs.androidx.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}

sqldelight {
    databases {
        create("CineMoodDatabase") {
            packageName.set("tech.nullexdev.cinemood.service.data.local.db")
        }
    }
}
