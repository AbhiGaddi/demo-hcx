// @GENERATOR:play-routes-compiler
// @SOURCE:/home/adinsst/IdeaProjects/demo-hcx/conf/routes
// @DATE:Thu Aug 18 15:16:25 IST 2022

import play.api.routing.JavaScriptReverseRoute


import _root_.controllers.Assets.Asset

// @LINE:1
package controllers.javascript {

  // @LINE:1
  class ReverseHomeController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:3
    def unsubscribe: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.unsubscribe",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "unsubscribe"})
        }
      """
    )
  
    // @LINE:1
    def notificationList: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.notificationList",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "notificationList"})
        }
      """
    )
  
    // @LINE:4
    def subscriptionlist: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.subscriptionlist",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "subscriptionlist"})
        }
      """
    )
  
    // @LINE:2
    def subscribe: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.subscribe",
      """
        function() {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "subscribe"})
        }
      """
    )
  
  }


}
