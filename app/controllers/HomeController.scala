package controllers
import com.datastax.driver.core.Cluster
import com.datastax.driver.core.Session
import com.datastax.driver.core.Row
import com.datastax.driver.core.ResultSet
import play.api.libs.json.{JsDefined, JsError, JsObject, JsResult, JsString, JsSuccess, JsValue, Json, Reads, Writes}
import play.api.mvc._
import play.filters.hosts
import play.filters.hosts.AllowedHostsFilter
import scala.language.{dynamics, postfixOps}
import scala.collection.JavaConverters._
import scala.io.Source
import javax.inject._
import java.util.UUID
import scala.collection.mutable
class HomeController @Inject()(cc: ControllerComponents) (implicit assetsFinder: AssetsFinder)
  extends AbstractController(cc) {

  val cluster = Cluster.builder()
    .addContactPoint("localhost")
    .withPort(9042)
    .build()
  val session = cluster.connect("cassandrademo")
  println("** Testing connection **");
  val source: String = Source.fromFile("/home/adinsst/IdeaProjects/demo-hcx/conf/project.json").mkString
  /* Notification list API */
  def notificationList(): Action[AnyContent] = Action { request: Request[AnyContent] =>
    Ok(source).as("application/json")
  }

 /* Notification for subscribe */
  def subscribe():Action[AnyContent] =Action { request: Request[AnyContent] =>
    val json = request.body.asJson.getOrElse("{}", null).asInstanceOf[JsObject]
    val subscriptionid: UUID = UUID.randomUUID()
    val recipientCode = (json \ "recipientcode").as[String]
    val senderCode = (json \ "sendercode").as[String]
    val subscriptionStatus = "Active"
    val topicCode = (json \ "topiccode").as[String]

    if (json == null) BadRequest("Expecting json results")
    else {
      val query1 = s"INSERT INTO notifier(subscriptionid,recipientcode,sendercode,subscriptionstatus,topiccode) VALUES($subscriptionid,'$recipientCode', '$senderCode','Active','$topicCode');"
      val resultSet = session.execute(query1)
      println(resultSet.all())

      val mymap:Map[String,AnyRef] = Map("subscriptionid" -> subscriptionid ,"subscriptionstatus" ->"Active")
      println(mymap)
      Ok(s"User subscribed for Notification SubscriptionId is : $subscriptionid  and Status is : $subscriptionStatus")

    }
  }
  /* Notification for unsubscribe */
  def unsubscribe(): Action[AnyContent] = Action { request: Request[AnyContent] =>
    val json = request.body.asJson.getOrElse("{}", null).asInstanceOf[JsObject]
    val subscriptionid: UUID = UUID.randomUUID()
    val recipientCode = (json \ "recipientcode").as[String]
    val senderCode = (json \ "sendercode").as[String]
    val subscriptionStatus = "InActive"
    val topicCode = (json \ "topiccode").as[String]

    if (json == null) BadRequest("Expecting json results")
    else {
      val query2 = s"INSERT INTO notifier(subscriptionid,recipientcode,sendercode,subscriptionstatus,topiccode) VALUES($subscriptionid,'$recipientCode', '$senderCode','InActive','$topicCode');"
      val resultSet = session.execute(query2)
      println(resultSet.all())
      Ok(s"User subscribed for Notification SubscriptionId is : $subscriptionid  and Status is : $subscriptionStatus")
      }
    }
      /* List of both Subscribe & Unsubscribe users  */
    def subscriptionlist(): Action[AnyContent] = Action { request: Request[AnyContent] =>
      val json = request.body.asJson.getOrElse("{}", null).asInstanceOf[JsObject]
      val recipientCode = (json \ "recipientcode").as[String]
      if(json == null) BadRequest("Expecting json data")
      else {
        val query3 = s"select * from notifier where recipientcode= '$recipientCode' ALLOW FILTERING"
        val resultSet = session.execute(query3)
        println(resultSet.all())

        Ok("subscriptionlist of all users --!!! and recipientcode is : $recipientCode")
      }
    }


  }


