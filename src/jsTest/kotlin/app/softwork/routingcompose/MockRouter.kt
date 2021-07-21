package app.softwork.routingcompose

class MockRouter : Router("") {

    override fun navigate(to: String) {
        require(to.startsWith("/"))
        update(newPath = to)
    }
}
