package app.softwork.routingcompose

internal class DelegateRouter(val basePath: String, val router: Router) : Router by router {
    override fun navigate(to: String, hide: Boolean, replace: Boolean) {
        when {
            to.startsWith("/") -> {
                router.navigate(to, hide, replace)
            }

            basePath == "/" -> {
                router.navigate("/$to", hide, replace)
            }

            to.startsWith(".") -> {
                val newPath = router.currentPath.relative(to)
                router.navigate(newPath.path, replace = replace)
            }

            else -> {
                router.navigate("$basePath/$to", hide, replace)
            }
        }
    }
}
