// @GENERATOR:play-routes-compiler
// @SOURCE:/home/adinsst/IdeaProjects/demo-hcx/conf/routes
// @DATE:Thu Aug 18 15:16:25 IST 2022

package router

import play.core.routing._
import play.core.routing.HandlerInvokerFactory._

import play.api.mvc._

import _root_.controllers.Assets.Asset

class Routes(
  override val errorHandler: play.api.http.HttpErrorHandler, 
  // @LINE:1
  HomeController_0: controllers.HomeController,
  val prefix: String
) extends GeneratedRouter {

   @javax.inject.Inject()
   def this(errorHandler: play.api.http.HttpErrorHandler,
    // @LINE:1
    HomeController_0: controllers.HomeController
  ) = this(errorHandler, HomeController_0, "/")

  def withPrefix(addPrefix: String): Routes = {
    val prefix = play.api.routing.Router.concatPrefix(addPrefix, this.prefix)
    router.RoutesPrefix.setPrefix(prefix)
    new Routes(errorHandler, HomeController_0, prefix)
  }

  private[this] val defaultPrefix: String = {
    if (this.prefix.endsWith("/")) "" else "/"
  }

  def documentation = List(
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """notificationList""", """controllers.HomeController.notificationList"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """subscribe""", """controllers.HomeController.subscribe"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """unsubscribe""", """controllers.HomeController.unsubscribe"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """subscriptionlist""", """controllers.HomeController.subscriptionlist"""),
    Nil
  ).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
    case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
    case l => s ++ l.asInstanceOf[List[(String,String,String)]]
  }}


  // @LINE:1
  private[this] lazy val controllers_HomeController_notificationList0_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("notificationList")))
  )
  private[this] lazy val controllers_HomeController_notificationList0_invoker = createInvoker(
    HomeController_0.notificationList,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "notificationList",
      Nil,
      "GET",
      this.prefix + """notificationList""",
      """""",
      Seq()
    )
  )

  // @LINE:2
  private[this] lazy val controllers_HomeController_subscribe1_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("subscribe")))
  )
  private[this] lazy val controllers_HomeController_subscribe1_invoker = createInvoker(
    HomeController_0.subscribe,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "subscribe",
      Nil,
      "POST",
      this.prefix + """subscribe""",
      """""",
      Seq()
    )
  )

  // @LINE:3
  private[this] lazy val controllers_HomeController_unsubscribe2_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("unsubscribe")))
  )
  private[this] lazy val controllers_HomeController_unsubscribe2_invoker = createInvoker(
    HomeController_0.unsubscribe,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "unsubscribe",
      Nil,
      "POST",
      this.prefix + """unsubscribe""",
      """""",
      Seq()
    )
  )

  // @LINE:4
  private[this] lazy val controllers_HomeController_subscriptionlist3_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("subscriptionlist")))
  )
  private[this] lazy val controllers_HomeController_subscriptionlist3_invoker = createInvoker(
    HomeController_0.subscriptionlist,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "subscriptionlist",
      Nil,
      "POST",
      this.prefix + """subscriptionlist""",
      """""",
      Seq()
    )
  )


  def routes: PartialFunction[RequestHeader, Handler] = {
  
    // @LINE:1
    case controllers_HomeController_notificationList0_route(params@_) =>
      call { 
        controllers_HomeController_notificationList0_invoker.call(HomeController_0.notificationList)
      }
  
    // @LINE:2
    case controllers_HomeController_subscribe1_route(params@_) =>
      call { 
        controllers_HomeController_subscribe1_invoker.call(HomeController_0.subscribe)
      }
  
    // @LINE:3
    case controllers_HomeController_unsubscribe2_route(params@_) =>
      call { 
        controllers_HomeController_unsubscribe2_invoker.call(HomeController_0.unsubscribe)
      }
  
    // @LINE:4
    case controllers_HomeController_subscriptionlist3_route(params@_) =>
      call { 
        controllers_HomeController_subscriptionlist3_invoker.call(HomeController_0.subscriptionlist)
      }
  }
}
