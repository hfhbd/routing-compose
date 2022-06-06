import org.jetbrains.compose.*

plugins {
    kotlin("js")
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
}

dependencies {
    implementation(projects.integrationTest)
}
