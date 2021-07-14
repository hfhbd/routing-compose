plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

repositories {
    mavenCentral()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven(url = "https://maven.pkg.github.com/hfhbd/*") {
        credentials {
            username = System.getProperty("gpr.user") ?: System.getenv("GITHUB_ACTOR")
            password = System.getProperty("gpr.key") ?: System.getenv("GITHUB_TOKEN")
        }
    }
}

kotlin {
    jvm()
    js(IR) {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.routingCompose)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
    }
}
