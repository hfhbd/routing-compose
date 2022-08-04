pluginManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "routing-compose"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":integrationTest")
include(":integrationTest:hashRouterTest")
include(":integrationTest:browserRouterTest")
