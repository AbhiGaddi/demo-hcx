package controllers
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.{JsValue, Json}
import play.api.test.FakeRequest
import play.api.test.Helpers._

@RunWith(classOf[JUnitRunner])
class HomeControllerSpec extends Specification {

  val app: Application = new GuiceApplicationBuilder().build

  "HomeController" should {
    // Test cases for Subscribe
      "return success response for subscribe API" in {
        val controller=app.injector.instanceOf[controllers.HomeController]
        val json: JsValue = Json.parse("""{"sender_code":"user@25","recipient_code":"user@31","topic_code":"notification-sent-success"}""")
        val fakeRequest = FakeRequest("POST", "/notification/subscribe").withJsonBody(json)
        val result = controller.subscribe()(fakeRequest)
        status(result) must equalTo(OK)
      }
    // Testcases for unsubscribe
  "return success response for unsubscribe API" in {
    val controller=app.injector.instanceOf[controllers.HomeController]
    val json:JsValue=Json.parse("""{"sender_code":"user@25","recipient_code":"user@31","topic_code":"notification-sent-success"}""")
    val fakeRequest=FakeRequest("POST","/notification/unsubscribe").withJsonBody(json)
    val result =controller.unsubscribe()(fakeRequest)
    status(result) must equalTo(OK)
  }
    // Testcases for subscriptionlist
   "return success response for subscriptionlist API " in {
     val controller = app.injector.instanceOf[controllers.HomeController]
     val json: JsValue = Json.parse("""{"recipient_code":"user@2"}""")
     val fakeRequest = FakeRequest("POST", "/notification/subscription/list").withJsonBody(json)
     val result = controller.subscriptionList()(fakeRequest)
     status(result) must equalTo(OK)
   }
    // Testcases for Notificationtopiclist
     "return success response for notification topic list API" in {
       val controller=app.injector.instanceOf[controllers.HomeController]
       val fakeRequest=FakeRequest("GET","/notification/topic/list")
       val result=controller.notificationList()(fakeRequest)
       status(result) mustEqual(OK)

     }
  }
  // Bad Requests
   "Home controller with invalid request " should {

     "return Badrequest response for subscribe  API" in {
       val controller = app.injector.instanceOf[controllers.HomeController]
       val json:JsValue=Json.parse("""{"sender_code":"","recipient_code":"","topic_code":""}""")
       val fakeRequest = FakeRequest("POST", "/notification/subscribe").withJsonBody(json)
       val result = controller.subscribe()(fakeRequest)
       status(result) must equalTo(BAD_REQUEST)
     }
     "return Badrequest response for unsubscribe  API" in {
       val controller = app.injector.instanceOf[controllers.HomeController]
       val json: JsValue = Json.parse("""{"sender_code":" ","recipient_code":" ","topic_code":""}""")
       val fakeRequest = FakeRequest("POST", "/notification/unsubscribe").withJsonBody(json)
       val result = controller.unsubscribe()(fakeRequest)
       status(result) must equalTo(BAD_REQUEST)
     }
     "return error response for subscribeList  API" in {
       val controller = app.injector.instanceOf[controllers.HomeController]
       val json: JsValue = Json.parse("""{ "recipient_code":""}""")
       val fakeRequest = FakeRequest("POST", "/notification/subscription/list").withJsonBody(json)
       val result = controller.subscriptionList()(fakeRequest)
       status(result) must equalTo(BAD_REQUEST)
     }


  }

}




