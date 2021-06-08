# RoutingCompose

Highly experimental routing feature for [Compose Web](https://github.com/Jetbrains/compose-jb)

## Install

This package is uploaded
to [GitHub Packages](https://docs.github.com/en/packages/guides/configuring-gradle-for-use-with-github-packages).

````kotlin
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/hfhbd/*")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
            password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {
    implementation("app.softwork:routing-compose:0.0.1")
}
````
