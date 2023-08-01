plugins {
    kotlin("js")
    id("org.jetbrains.compose")
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
