import java.util.*
import io.gitlab.arturbosch.detekt.*

plugins {
    kotlin("multiplatform") version "1.9.22"
    id("org.jetbrains.compose") version "1.6.0-dev1409"
    id("maven-publish")
    id("signing")
    id("io.github.gradle-nexus.publish-plugin") version "1.3.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.5"
    id("app.cash.licensee") version "1.9.0"
}

kotlin {
    jvmToolchain(11)
    jvm()
    js(IR) {
        browser()
    }

    explicitApi()
    targets.configureEach {
        compilations.configureEach {
            kotlinOptions.allWarningsAsErrors = true
        }
    }
    sourceSets {
        configureEach {
            languageSettings {
                progressiveMode = true
            }
        }
        commonMain {
            dependencies {
                api(compose.runtime)
                api(libs.kotlinx.uuid)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotlinx.coroutines.core)
            }
        }
        named("jsMain") {
            dependencies {
                api(compose.html.core)
            }
        }
        named("jsTest") {
            dependencies {
                implementation(compose.html.testUtils)
            }
        }

        named("jvmTest") {
            dependencies {
                implementation(compose.desktop.uiTestJUnit4) // there is no non-ui testing
                implementation(compose.desktop.currentOs) // ui-testings needs skiko
                implementation(libs.kotlinx.coroutines.swing)
            }
        }
    }
}

licensee {
    allow("Apache-2.0")
}

val emptyJar by tasks.registering(Jar::class) { }

publishing {
    publications.configureEach {
        this as MavenPublication
        artifact(emptyJar) {
            classifier = "javadoc"
        }
        pom {
            name.set("app.softwork Routing Compose")
            description.set("A multiplatform library for routing to use with JetPack Compose Web, HTML and Desktop")
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

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    signingKey?.let {
        useInMemoryPgpKeys(String(Base64.getDecoder().decode(it)).trim(), signingPassword)
        sign(publishing.publications)
    }
}

// https://youtrack.jetbrains.com/issue/KT-46466
val signingTasks = tasks.withType<Sign>()
tasks.withType<AbstractPublishToMaven>().configureEach {
    dependsOn(signingTasks)
}

tasks.withType<AbstractArchiveTask>().configureEach {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}

nexusPublishing {
    this.repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
}

detekt {
    source.from(files(rootProject.rootDir))
    parallel = true
    autoCorrect = true
    buildUponDefaultConfig = true
}

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:${detekt.toolVersion}")
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
