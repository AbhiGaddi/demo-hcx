// @GENERATOR:play-routes-compiler
// @SOURCE:/home/adinsst/IdeaProjects/demo-hcx/conf/routes
// @DATE:Thu Aug 18 15:16:25 IST 2022


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
