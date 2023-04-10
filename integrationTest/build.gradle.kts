import org.jetbrains.compose.*

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

repositories {
    mavenCentral()
    google()
    jetbrainsCompose()
}

kotlin {
    jvmToolchain(8)
    jvm()
    js(IR) {
        browser()
    }

    sourceSets {
        commonMain {
            dependencies {
                api(projects.routingCompose)
            }
        }
        named("jvmMain") {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
    }
}
