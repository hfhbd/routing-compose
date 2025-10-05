plugins {
    kotlin("multiplatform")
    kotlin("plugin.compose")
    id("org.jetbrains.compose")
}

kotlin {
    jvmToolchain(11)

    jvm()
    js {
        browser()
        useEsModules()
        compilerOptions {
            target.set("es2015")
        }
    }
    wasmJs {
        browser()
        binaries.executable()
    }

    applyDefaultHierarchyTemplate {
        common {
            group("jvmWasmShared") {
                withJvm()
                withWasmJs()
            }
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(projects.routingCompose)
                api(libs.kotlinx.coroutines.core)
            }
        }
        named("jvmWasmSharedMain") {
            dependencies {
                implementation(compose.material)
            }
        }
        jvmMain {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
    }
}

plugins.withType<org.jetbrains.kotlin.gradle.targets.wasm.nodejs.WasmNodeJsPlugin> {
    the<org.jetbrains.kotlin.gradle.targets.wasm.nodejs.WasmNodeJsEnvSpec>().downloadBaseUrl = null
}
plugins.withType<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsPlugin> {
    the<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsEnvSpec>().downloadBaseUrl = null
}
the<org.jetbrains.kotlin.gradle.targets.wasm.binaryen.BinaryenEnvSpec>().downloadBaseUrl.set(null)
