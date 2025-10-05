plugins {
    kotlin("multiplatform")
    kotlin("plugin.compose")
    id("org.jetbrains.compose")
}

kotlin {
    js {
        browser {
            binaries.executable()
            useEsModules()
            compilerOptions {
                target.set("es2015")
            }
        }
    }
    sourceSets {
        jsMain {
            dependencies {
                implementation(projects.integrationTest)
            }
        }
    }
}

plugins.withType<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsPlugin> {
    the<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsEnvSpec>().downloadBaseUrl = null
}
