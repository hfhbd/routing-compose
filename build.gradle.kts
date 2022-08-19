import org.jetbrains.compose.*
import java.util.*
import io.gitlab.arturbosch.detekt.*

plugins {
    kotlin("multiplatform") version "1.7.10"
    id("org.jetbrains.compose") version "1.2.0-alpha01-dev745"
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
    id("io.gitlab.arturbosch.detekt") version "1.21.0"
    id("app.cash.licensee") version "1.5.0"
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
        browser()
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
                api("app.softwork:kotlinx-uuid-core:0.0.15")
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
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.6.4")
            }
        }
    }
}

licensee {
    allow("Apache-2.0")

    // https://github.com/JetBrains/compose-jb/pull/2195
    allowDependency("org.jetbrains.compose.web", "web-core-js", "1.2.0-alpha01-dev745")
    allowDependency("org.jetbrains.compose.web", "web-core", "1.2.0-alpha01-dev745")
    allowDependency("org.jetbrains.compose.web", "internal-web-core-runtime-js", "1.2.0-alpha01-dev745")
    allowDependency("org.jetbrains.compose.web", "internal-web-core-runtime", "1.2.0-alpha01-dev745")
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

detekt {
    source = files(rootProject.rootDir)
    parallel = true
    buildUponDefaultConfig = true
}

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.21.0")
}

tasks {
    fun SourceTask.config() {
        include("**/*.kt")
        exclude("**/*.kts")
        exclude("**/resources/**")
        exclude("**/generated/**")
        exclude("**/build/**")
    }
    withType<DetektCreateBaselineTask>().configureEach {
        config()
    }
    withType<Detekt>().configureEach {
        config()

        reports {
            sarif.required.set(true)
        }
    }
}
