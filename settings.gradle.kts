pluginManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        gradlePluginPortal()
    }
}

rootProject.name = "routing-compose"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":integrationTest")