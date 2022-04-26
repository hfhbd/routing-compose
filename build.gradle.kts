import org.jetbrains.compose.*
import java.util.*

plugins {
    kotlin("multiplatform") version "1.6.21"
    id("org.jetbrains.compose") version "1.2.0-alpha01-dev675"
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}

group = "app.softwork"

repositories {
    mavenCentral()
    google()
    jetbrainsCompose()
}

kotlin {
    jvm()
    js(IR) {
        browser {
            binaries.library()
        }
    }

    explicitApi()
    targets.all {
        compilations.all {
            kotlinOptions.allWarningsAsErrors = true
        }
    }
    sourceSets {
        all {
            languageSettings {
                progressiveMode = true
            }
        }
        commonMain {
            dependencies {
                api(compose.runtime)
                api("app.softwork:kotlinx-uuid-core:0.0.14")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jsMain by getting {
            dependencies {
                api(compose.web.core)
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(compose.web.testUtils)
            }
        }

        val jvmTest by getting {
            dependencies {
                @OptIn(ExperimentalComposeLibrary::class)
                implementation(compose.uiTestJUnit4) // there is no non-ui testing
                implementation(compose.desktop.currentOs) // ui-testings needs skiko
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.6.1")
            }
        }
    }
}

val emptyJar by tasks.creating(Jar::class) { }

publishing {
    publications.all {
        this as MavenPublication
        artifact(emptyJar) {
            classifier = "javadoc"
        }
        pom {
            name.set("app.softwork Routing Compose")
            description.set("A multiplatform library for routing to use with JetPack Compose Web and Desktop")
            url.set("https://github.com/hfhbd/routing-compose")
            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            developers {
                developer {
                    id.set("hfhbd")
                    name.set("Philip Wedemann")
                    email.set("mybztg+mavencentral@icloud.com")
                }
            }
            scm {
                connection.set("scm:git://github.com/hfhbd/routing-compose.git")
                developerConnection.set("scm:git://github.com/hfhbd/routing-compose.git")
                url.set("https://github.com/hfhbd/routing-compose")
            }
        }
    }
}

(System.getProperty("signing.privateKey") ?: System.getenv("SIGNING_PRIVATE_KEY"))?.let {
    String(Base64.getDecoder().decode(it)).trim()
}?.let { key ->
    signing {
        val signingPassword = System.getProperty("signing.password") ?: System.getenv("SIGNING_PASSWORD")
        useInMemoryPgpKeys(key, signingPassword)
        sign(publishing.publications)
    }
}

nexusPublishing {
    repositories {
        sonatype {
            username.set(System.getProperty("sonartype.apiKey") ?: System.getenv("SONARTYPE_APIKEY"))
            password.set(System.getProperty("sonartype.apiToken") ?: System.getenv("SONARTYPE_APITOKEN"))
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
}
