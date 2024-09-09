import org.gradle.kotlin.dsl.support.KotlinCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerOptions
import org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrCompilation

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
