plugins {
    kotlin("js")
    kotlin("plugin.compose")
    id("org.jetbrains.compose")
}

kotlin {
    js {
        browser {
            binaries.executable()
        }
    }
}

dependencies {
    implementation(projects.integrationTest)
}
