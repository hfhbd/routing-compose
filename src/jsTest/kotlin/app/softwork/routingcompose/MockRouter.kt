package app.softwork.routingcompose

class MockRouter : AbstractRouter("") {

    override fun navigate(to: String) {
        require(to.startsWith("/"))
        update(newPath = to)
    }
}
