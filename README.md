# RoutingCompose

Highly experimental routing feature for [Compose Web and Desktop](https://github.com/Jetbrains/compose-jb)

## Install

This package is uploaded to MavenCentral.

````kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("app.softwork:routing-compose:LATEST")
}
````

## Usage

Example with `HashRouter`, `BrowserRouter` and `DesktopRouter` will be implemented in the same manner.

```kotlin
HashRouter(initRoute = "/users") { // or BrowserRouter(initRoute = "/users") {
    route("/users") {
        int { userID ->
            Text("User with $userID") 
        } 
        noMatch {
            Text("User list")
        }
    }
    noMatch {
        Text("Hello World")
    }
}
```

RoutingCompose offers three routing implementations, `HashRouter`, `BrowserRouter` and `DesktopRouter`.

[This article](https://blog.bitsrc.io/using-hashed-vs-nonhashed-url-paths-in-single-page-apps-a66234cefc96) provides a good explanation of the difference between each browser routing strategy.

### HashRouter
`HashRouter` is used for hashed urls (e.g. yoursite.com/#/path). This strategy requires no additional setup to work in a single page compose-web application. Some SaaS providers, like GitHub Pages, do not offer configuration options, so you have to use `HashRouter`.

### BrowserRouter

`BrowserRouter` is used for traditional urls (e.g. yoursite.com/path). Using this strategy will require additional work in a single page compose-web application, requiring you to implement a catch-all strategy to return the same html resource for all paths. This strategy will be different for different server configurations.

#### Development usage:
The `browser` target for Kotlin/JS uses [webpack-dev-server](https://github.com/webpack/webpack-dev-server) as a local development server. We need our webpack config to serve `index.html` (or your primary html file) for all paths to work with `BrowserRouter`. This is done in webpack-dev-server through the webpack config's [devServer.historyApiFallback](https://webpack.js.org/configuration/dev-server/#devserverhistoryapifallback) flag.

The Kotlin webpack DSL currently [does not support the `historyApiFallback` flag](https://github.com/JetBrains/kotlin/blob/master/libraries/tools/kotlin-gradle-plugin/src/main/kotlin/org/jetbrains/kotlin/gradle/targets/js/webpack/KotlinWebpackConfig.kt#L165), but we can add it through additional [webpack configuration files](https://kotlinlang.org/docs/js-project-setup.html#webpack-configuration-file) that will be merged with the auto-generated `webpack.config.js` when building.

##### Instructions
First, create a directory in the top-most project directory named `webpack.config.d`.

Create a new `.js` file containing a `config.devServer` configuration setting `historyApiFallback = true`. You can name this file any name you wish, it will be merged into the project's main `webpack.config.js`.

```javascript
// YourProject/webpack.config.d/devServerConfig.js

config.devServer = {
  ...config.devServer, // Merge with other devServer settings
  "historyApiFallback": true
};
```

Then run your web app, and it should route all paths to a valid route. You can confirm this by refreshing or manually entering a path.

### DesktopRouter
You can use the `DesktopRouter` implementation to add a routing feature to your Compose Desktop application. 
Every Window must have exactly 1 `DesktopRouter`. 
The `DesktopRouter` contains a special `navigateBack` method.

```kotlin
Window {
    DesktopRouter("/") {
        string {
            Column {
                Text("Hello $it")
                val router = Router.current
                Button(onClick = { router.navigateBack() }) {
                    Text("Go back")
                }
            }
        }
        noMatch {
            val router = Router.current
            Button(onClick = { 
                router.navigate(to = "/World")
            }) {
                Text("Navigate to World")
            }
        }
    }
}
```
