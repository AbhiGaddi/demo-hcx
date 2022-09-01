package controllers                          //Pacakge name
import com.datastax.driver.core.Cluster
import com.datastax.driver.core.Session
import com.datastax.driver.core.Row
import com.datastax.driver.core.ResultSet
import com.fasterxml.jackson.core.`type`.TypeReference
import play.api.libs.json.{JsDefined, JsError, JsObject, JsResult, JsString, JsSuccess, JsValue, Json, Reads, Writes}
import play.api.mvc.{request, _}
import play.filters.hosts
import play.filters.hosts.AllowedHostsFilter

import scala.language.{dynamics, postfixOps}
import scala.collection.JavaConverters._
import scala.io.Source
import javax.inject._
import java.util.UUID
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import exception.ClientException

import java.util     //Hashmap

class HomeController @Inject()(cc: ControllerComponents) (implicit assetsFinder: AssetsFinder) extends AbstractController(cc) {
  val cluster = Cluster.builder()
    .addContactPoint("127.0.0.1")
    .withPort(9042)
    .build()
  val session = cluster.connect("cassandrademo")
  val mapper = new ObjectMapper //creating object for mapping
  val source: String = Source.fromFile("/home/adinsst/IdeaProjects/demo-hcx/conf/project.json").mkString

  /* Notification topic list API */
  def notificationList(): Action[AnyContent] = Action { request: Request[AnyContent] =>
    Ok(source).as("application/json")
  }

  /* Notification for subscribe */
  @throws(classOf[ClientException])
  def subscribe() = Action { request: Request[AnyContent] =>
    try {
      val requestBody = jsonToMap(request)
      validateRequest(requestBody)
      val senderCode = requestBody.get("sender_code").toString
      val recipientCode = requestBody.get("recipient_code").toString
      val subscriptionStatus = "Active"
      val topicCode = requestBody.get("topic_code").toString
      val subscriptionId = senderCode + "-" + topicCode + "-" + recipientCode
      val insertQuery = s"INSERT INTO notifierlist(subscription_id,created_on,recipient_code,sender_code,subscription_status,topic_code,updated_on) VALUES('$subscriptionId',dateof(now()),'$recipientCode','$senderCode','Active','$topicCode', null) IF NOT EXISTS;"
      val resultSet = session.execute(insertQuery)
      if (resultSet.wasApplied() == false)
        BadRequest(s" subscription with this $senderCode and $recipientCode already exist -- ! ")
      else {
        // val responseMap = new java.util.HashMap[String, AnyRef]()   // mapping
        // val testMap = Map("subscriptionid" -> subscriptionId ,"subscriptionstatus" -> "Active")
        val resopnseMap = new util.HashMap[String, AnyRef]()
        resopnseMap.put("subscription_id", subscriptionId)
        resopnseMap.put("subscription_status", subscriptionStatus)
        val jsonResult = mapper.writeValueAsString(resopnseMap); //serialization from object to String
        Ok(jsonResult).as("application/json")
      }
    } catch {
      case ex: ClientException =>
        BadRequest(ex.getMessage)
    }
  }


  private def jsonToMap(request: Request[AnyContent]) = {
    val json = request.body.asJson.getOrElse("{}").toString
    val jsonMap = mapper.readValue(json, classOf[util.Map[String, Object]])
    jsonMap
  }
  @throws(classOf[ClientException])
  def validateRequest(jsonMap: util.Map[String, Object]) = {
    val validateList: List[String] = List("sender_code", "recipient_code", "topic_code")
    for (property <- validateList) {
      if (!jsonMap.containsKey(property) || jsonMap.getOrDefault(property, "").toString.isEmpty) {
        throw new ClientException(s"$property is missing or empty")
      }
    }
  }

  /* Notification for unsubscribe */
  @throws(classOf[ClientException])
    def unsubscribe() = Action { request: Request[AnyContent] =>
      val requestBody = jsonToMap(request)
      try{
      validateRequest(requestBody)
      val senderCode = requestBody.get("sender_code").toString
      val recipientCode = requestBody.get("recipient_code").toString
      val subscriptionStatus = "InActive"
      val topicCode = requestBody.get("topic_code").toString
      val subscriptionId = senderCode + "-" + topicCode + "-" + recipientCode
      val updateQuery = s"update notifierlist set subscription_status = 'InActive',updated_on = dateof(now()),created_on = null where subscription_id = '$subscriptionId' IF EXISTS;"
      val resultSet = session.execute(updateQuery)
      if (resultSet.wasApplied() == false) {
      val badresult : String = "subscription with this Combination of Sendercode,receipentcode & topiccode  does not exist"
          val testMap =new util.HashMap[String,AnyRef]()
          testMap.put("message",badresult)
          testMap.put("status","Fail")
        val badResult =mapper.writeValueAsString(testMap)
        BadRequest(badResult).as("application/json")
      }
      else {
        val resopnseMap = new java.util.HashMap[String, AnyRef]()
        resopnseMap.put("subscription_id", subscriptionId)
        resopnseMap.put("subscription_status", subscriptionStatus)
        val jsonResult = mapper.writeValueAsString(resopnseMap) //serialization from object to String
        Ok(jsonResult).as("application/json")
      }
      }
      catch {
        case ex :ClientException =>
          BadRequest(ex.getMessage)
      }
    }


    /* List of both Subscribe & Unsubscribe list */
    def subscriptionlist() = Action { request: Request[AnyContent] =>
      val json = request.body.asJson.getOrElse("{}", null).asInstanceOf[JsObject]
      val recipientCode = (json \ "recipient_code").as[String]
      if (recipientCode.isEmpty) BadRequest("Please Enter Recipient Code --! ")
      else {
        val searchQuery = s"select json * from notifierlist where recipient_code= '$recipientCode' ALLOW FILTERING"
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














