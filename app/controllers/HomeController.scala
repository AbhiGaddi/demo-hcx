package controllers
import com.datastax.driver.core.Cluster
import com.datastax.driver.core.Session
import com.datastax.driver.core.Row
import com.datastax.driver.core.ResultSet
import io.netty.util.concurrent.Future
import play.api.libs.json.{JsDefined, JsError, JsObject, JsResult, JsString, JsSuccess, JsValue, Json, Reads, Writes}
import play.api.mvc._
import play.filters.hosts
import play.filters.hosts.AllowedHostsFilter
import scala.language.{dynamics, postfixOps}
import scala.collection.JavaConverters._
import scala.io.Source
import javax.inject._
import java.util.UUID
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import java.util

class HomeController @Inject()(cc: ControllerComponents) (implicit assetsFinder: AssetsFinder)
  extends AbstractController(cc) {
  val mapper = new ObjectMapper
  val cluster = Cluster.builder()
    .addContactPoint("localhost")
    .withPort(9042)
    .build()
  val session = cluster.connect("cassandrademo")
  val source: String = Source.fromFile("/home/adinsst/IdeaProjects/demo-hcx/conf/project.json").mkString

  /* Notification list API */
  def notificationList(): Action[AnyContent] = Action { request: Request[AnyContent] =>
    Ok(source).as("application/json")
  }

  /* Notification for subscribe */
  def subscribe() = Action { request: Request[AnyContent] =>
    val json = request.body.asJson.getOrElse("{}", null).asInstanceOf[JsObject]
    val subscriptionId = UUID.randomUUID()
    val recipientCode = (json \ "recipientcode").as[String]
    val senderCode = (json \ "sendercode").as[String]
    val subscriptionStatus = "Active"
    val topicCode = (json \ "topiccode").as[String]

    if (json == null) BadRequest("Expecting json results")
    /* else if (){
      BadRequest
    }
     */
    else {
      val query = s"INSERT INTO notifier(subscriptionid,recipientcode,sendercode,subscriptionstatus,topiccode) VALUES($subscriptionId,'$recipientCode', '$senderCode','Active','$topicCode');"
      val resultSet = session.execute(query)
      var responseMap = new java.util.HashMap[String, AnyRef]()
      mymap.put("subscriptionid", subscriptionid)
      mymap.put("subscriptionstatus", "Active")
      val jsonResult = mapper.writeValueAsString(mymap);  //serialization from object to String
      Ok(jsonResult).as("application/json")
    }
  }

  /* Notification for unsubscribe */
  def unsubscribe() = Action { request: Request[AnyContent] =>
    val json = request.body.asJson.getOrElse("{}", null).asInstanceOf[JsObject]
    val subscriptionId = UUID.randomUUID()
    val recipientCode = (json \ "recipientcode").as[String]
    val senderCode = (json \ "sendercode(").as[String]
    val subscriptionStatus = "InActive"
    val topicCode = (json \ "topiccode").as[String]

    if (json == null) BadRequest("Expecting json results")
    else {
      val query2 = s"INSERT INTO notifier(subscriptionid,recipientcode,sendercode,subscriptionstatus,topiccode) VALUES($subscriptionId,'$recipientCode', '$senderCode','InActive','$topicCode');"
      val resultSet = session.execute(query2)
      val mymap1 = new java.util.HashMap[String, AnyRef]()
      mymap1.put("subscriptionid", subscriptionid)
      mymap1.put("subscriptionstatus", subscriptionStatus)
      val jsonResult1 = mapper.writeValueAsString(mymap1)  //serialization from object to String
      Ok(jsonResult1).as("application/json")
    }
  }

  /* List of both Subscribe & Unsubscribe users  */
  def subscriptionlist() = Action { request: Request[AnyContent] =>
    val json = request.body.asJson.getOrElse("{}", null).asInstanceOf[JsObject]
    val recipientCode = (json \ "recipientcode").as[String]
    if (json == null) BadRequest("Expecting json data")
    else {
      val query = s"select json * from notifier where recipientcode= '$recipientCode' ALLOW FILTERING"
      val resultSet = session.execute(query)
      var mylist = new util.ArrayList[Any]()
      val rsList = resultSet.all().asScala
      for (row <- rsList) {
        mylist.add(row.getString(0))
      }
      Ok(mylist.toString).as("application/json")
    }
  }
}









