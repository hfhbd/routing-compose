plugins {
    kotlin("multiplatform") version "1.5.10"
    id("org.jetbrains.compose") version "0.0.0-web-dev-14.1"
    `maven-publish`
}

group = "app.softwork"

repositories {
    mavenCentral()
    google()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven(url = "https://maven.pkg.github.com/hfhbd/*") {
        credentials {
            username = System.getenv("GITHUB_ACTOR")
            password = System.getenv("GITHUB_TOKEN")
        }
    }
}

kotlin {
    js(IR) {
        browser()
    }

    explicitApi()

    sourceSets {
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jsMain by getting {
            dependencies {
                api("app.softwork:kotlinx-uuid-core:0.0.5")
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
