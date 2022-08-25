package controllers                          //Pacakge name
import com.datastax.driver.core.Cluster      // Database connection
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
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import java.util     //Hashmap

class HomeController @Inject()(cc: ControllerComponents) (implicit assetsFinder: AssetsFinder) extends AbstractController(cc) {
  val mapper = new ObjectMapper //creating object for mapping
  val cluster = Cluster.builder()
    .addContactPoint("localhost")
    .withPort(9042)
    .build()
  val session = cluster.connect("cassandrademo")
  val source: String = Source.fromFile("/home/adinsst/IdeaProjects/demo-hcx/conf/project.json").mkString
  val users = List("user@1", "user@2", "user@3", "user@4", "user@5")

  /* Notification topic list API */
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
    if (senderCode.isEmpty) BadRequest("Please Enter SenderCode --!")  // Basic Validation
    else {
      if (recipientCode.isEmpty) BadRequest("Please Enter RecipientCode --!")
      else {
        if (topicCode.isEmpty) BadRequest("Please Enter TopicCode --!")
        else {
          val insertQuery = s"INSERT INTO notifier(subscriptionid,recipientcode,sendercode,subscriptionstatus,topiccode) VALUES($subscriptionId,'$recipientCode', '$senderCode','Active','$topicCode');"
          val resultSet = session.execute(insertQuery)
          var responseMap = new java.util.HashMap[String, AnyRef]()   // mapping
          responseMap.put("subscriptionid", subscriptionId)
          responseMap.put("subscriptionstatus", "Active")
          val jsonResult = mapper.writeValueAsString(responseMap); //serialization from object to String
          Ok(jsonResult).as("application/json")
        }
      }
    }
  }

  /* Notification for unsubscribe */
  def unsubscribe() = Action { request: Request[AnyContent] =>
    val json = request.body.asJson.getOrElse("{}", null).asInstanceOf[JsObject]
    val subscriptionId = UUID.randomUUID()
    val recipientCode = (json \ "recipientcode").as[String]
    val senderCode = (json \ "sendercode").as[String]
    val subscriptionStatus = "InActive"
    val topicCode = (json \ "topiccode").as[String]

    if (recipientCode.isEmpty) BadRequest("Expecting json results")
    else {
      val insertQuery = s"INSERT INTO notifier(subscriptionid,recipientcode,sendercode,subscriptionstatus,topiccode) VALUES($subscriptionId,'$recipientCode', '$senderCode','InActive','$topicCode');"
      val resultSet = session.execute(insertQuery)
      val resopnseMap = new java.util.HashMap[String, AnyRef]()
      resopnseMap.put("subscriptionid", subscriptionId)
      resopnseMap.put("subscriptionstatus", subscriptionStatus)
      val jsonResult = mapper.writeValueAsString(resopnseMap) //serialization from object to String
      Ok(jsonResult).as("application/json")
    }
  }

  /* List of both Subscribe & Unsubscribe list */
  def subscriptionlist() = Action { request: Request[AnyContent] =>
    val json = request.body.asJson.getOrElse("{}", null).asInstanceOf[JsObject]
    val recipientCode = (json \ "recipientcode").as[String]
    if (recipientCode.isEmpty) BadRequest("Please Enter Recipient Code --! ")
    else {
      val searchQuery = s"select json * from notifier where recipientcode= '$recipientCode' ALLOW FILTERING"
      val resultSet = session.execute(searchQuery)
      var responseList = new util.ArrayList[Any]()
      val rsList = resultSet.all().asScala
      for (row <- rsList) {
        responseList.add(row.getString(0))
      }
      Ok(responseList.toString).as("application/json")
    }

  }
}








