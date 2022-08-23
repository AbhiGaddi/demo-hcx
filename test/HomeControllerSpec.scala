  /* package controllers
import com.typesafe.config.ConfigFactory
import org.junit.runner.RunWith
import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.specs2.matcher.Matchers.equalTo
import org.specs2.runner.JUnitRunner
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.{JsValue, Json}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import org.specs2.mutable.Specification


@RunWith(classOf[JUnitRunner])
class HomeControllerSpec {

  val app = new GuiceApplicationBuilder().build
  val config = ConfigFactory.load();

  "HomeController" should {
      "return success response for subscribe API" in {
        val controller=app.injector.instanceOf[controllers.HomeController]
        val json: JsValue = Json.parse("""{"request": {"subscribe": {"senderCode":"sendercode","recipientCode :"recipientcode" ,"topicCode":"topiccode"}}}""")
        val fakeRequest = FakeRequest("POST", "/notification/subscribe").withJsonBody(json)
        val result = controller.subscribe()(fakeRequest)
        status(result) must equalTo(OK)
      }
  }

}

  */