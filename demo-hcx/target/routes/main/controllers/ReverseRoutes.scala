// @GENERATOR:play-routes-compiler
// @SOURCE:/home/adinsst/IdeaProjects/demo-hcx/conf/routes
// @DATE:Thu Aug 18 15:16:25 IST 2022

import play.api.mvc.Call


import _root_.controllers.Assets.Asset

// @LINE:1
package controllers {

  // @LINE:1
  class ReverseHomeController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:3
    def unsubscribe(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "unsubscribe")
    }
  
    // @LINE:1
    def notificationList(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "notificationList")
    }
  
    // @LINE:4
    def subscriptionlist(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "subscriptionlist")
    }
  
    // @LINE:2
    def subscribe(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "subscribe")
    }
  
  }


}
