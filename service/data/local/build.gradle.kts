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
                implementation(npm("@cashapp/sqldelight-sqljs-worker", "2.3.2"))
                implementation(npm("sql.js", "1.14.1"))
                implementation(libs.wrappers.browser)
                implementation(libs.sqldelight.coroutines.extensions)
            }
        }

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
            }
        }
        
        androidMain {
            dependsOn(roomMain)
        }

        val nativeMain by creating {
            dependsOn(roomMain)
        }

        jvmMain {
            dependsOn(roomMain)
            dependencies {
                implementation(libs.androidx.sqlite.bundled)
            }
        }

        jsMain {
            dependsOn(webMain)
            dependencies {
                implementation(libs.sqldelight.web.worker.driver)
            }
        }

        wasmJsMain {
            dependsOn(webMain)
            dependencies {
                implementation(libs.sqldelight.web.worker.driver.wasm.js)
                implementation(libs.wrappers.browser)
            }
        }

        getByName("iosArm64Main") {
            dependsOn(nativeMain)
        }
        getByName("iosSimulatorArm64Main") {
            dependsOn(nativeMain)
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
            packageName.set("tech.nullexdev.cinemood.service.data.local.db")
        }
    }
}
