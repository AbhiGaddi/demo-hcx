// @GENERATOR:play-routes-compiler
// @SOURCE:/home/adinsst/IdeaProjects/demo-hcx/conf/routes
// @DATE:Thu Aug 18 15:16:25 IST 2022

package controllers;

import router.RoutesPrefix;

public class routes {
  
  public static final controllers.ReverseHomeController HomeController = new controllers.ReverseHomeController(RoutesPrefix.byNamePrefix());

  public static class javascript {
    
    public static final controllers.javascript.ReverseHomeController HomeController = new controllers.javascript.ReverseHomeController(RoutesPrefix.byNamePrefix());
  }

}
