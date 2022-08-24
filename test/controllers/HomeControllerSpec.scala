package controllers
import com.typesafe.config.ConfigFactory
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.{JsValue, Json}
import play.api.test.FakeRequest
import play.api.test.Helpers._

@RunWith(classOf[JUnitRunner])
class HomeControllerSpec extends Specification {

  val app = new GuiceApplicationBuilder().build
  val config = ConfigFactory.load();

  "HomeController" should {
    // Test cases for Subscribe
      "return success response for subscribe API" in {
        val controller=app.injector.instanceOf[controllers.HomeController]
        val json: JsValue = Json.parse("""{"sendercode":"senderCode","recipientcode":"recipientCode","topiccode":"topicCode"}""")
        val fakeRequest = FakeRequest("POST", "/notification/subscribe").withJsonBody(json)
        val result = controller.subscribe()(fakeRequest)
        status(result) must equalTo(OK)
      }
    // Testcases for unsubscribe
  "return success response for unsubscribe API" in {
    val controller=app.injector.instanceOf[controllers.HomeController]
    val json:JsValue=Json.parse("""{"sendercode":"senderCode","recipientcode":"recipientCode","topiccode":"topicCode"}""")
    val fakeRequest=FakeRequest("POST","notification/unsubscribe").withJsonBody(json)
    val result =controller.unsubscribe()(fakeRequest)
    status(result) must equalTo(OK)
  }
    // Testcases for subscriptionlist
   "return success response for subscriptionlist API " in{
     val controller=app.injector.instanceOf[controllers.HomeController]
     val json:JsValue=Json.parse("""{"recipientcode":"recipientCode"}""")
     val fakeRequest=FakeRequest("POST","/notification/subscription/list").withJsonBody(json)
     val result=controller.subscriptionlist()(fakeRequest)
     status(result) must equalTo(OK)

     "return success response for notification topic list API" in {
       val controller=app.injector.instanceOf[controllers.HomeController]
       val fakeRequest=FakeRequest("GET","/notification/topic/list").withJsonBody(json)
       val result=controller.notificationList()(fakeRequest)
       status(result) mustEqual(OK)

     }

   }

  }


}




