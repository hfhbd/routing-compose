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
    js(IR) {
        browser {
            binaries.executable()
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.integrationTest)
            }
        }
    }
}
