pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("com.gradle.develocity") version "3.17.5"
}

develocity {
    buildScan {
        termsOfUseUrl.set("https://gradle.com/terms-of-service")
        termsOfUseAgree.set("yes")
        publishing {
            val isCI = providers.environmentVariable("CI").isPresent
            onlyIf { isCI }
        }
        tag("CI")
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
    }
}

rootProject.name = "routing-compose"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":integrationTest")
include(":integrationTest:hashRouterTest")
include(":integrationTest:browserRouterTest")
