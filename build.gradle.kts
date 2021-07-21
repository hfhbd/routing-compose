plugins {
    kotlin("multiplatform") version "1.5.21"
    id("org.jetbrains.compose") version "0.5.0-build262"
    `maven-publish`
}

group = "app.softwork"

repositories {
    mavenCentral()
    google()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm()
    js(IR) {
        browser {
            binaries.library()
        }
    }

    explicitApi()

    sourceSets {
        commonMain {
            dependencies {
                api(compose.runtime)
                api("app.softwork:kotlinx-uuid-core:0.0.9")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                api(compose.desktop.common)
            }
        }
        val jsMain by getting {
            dependencies {
                api(compose.web.core)
            }
        }
    }
}

publishing {
    repositories {
        maven(url = "https://maven.pkg.github.com/hfhbd/routing-compose") {
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
