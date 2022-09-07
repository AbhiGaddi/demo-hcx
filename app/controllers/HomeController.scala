package controllers
import cassandraClient.Cassandra
import com.fasterxml.jackson.databind.ObjectMapper
import exception.ClientException
import org.apache.commons.collections.MapUtils
import play.api.libs.json.Format.GenericFormat
import play.api.libs.json.Json
import play.api.libs.json.OFormat.oFormatFromReadsAndOWrites
import play.api.mvc._
import java.util
import javax.inject._
import scala.collection.JavaConverters._
import scala.io.Source
import scala.language.{dynamics, postfixOps}

class HomeController @Inject()(cc: ControllerComponents) (implicit assetsFinder: AssetsFinder) extends AbstractController(cc){

  private val cassandra = new Cassandra
  private val mapper = new ObjectMapper //creating object for mapping
  private val source: String = Source.fromFile("/project.json").mkString
 //val source: String = Source.fromFile(getClass().getResource("/project.json").getFile()).mkString
  /* Notification topic list API */
  def notificationList(): Action[AnyContent] = Action { request: Request[AnyContent] =>
    Ok(source).as("application/json")
  }

  /* Notification for subscribe */
  @throws(classOf[ClientException])
  def subscribe(): Action[AnyContent] = Action { request: Request[AnyContent] =>
    try {
      val requestBody = jsonToMap(request)
      validateRequest(requestBody)
      val senderCode = requestBody.get("sender_code").asInstanceOf[String].trim()
      val recipientCode = requestBody.get("recipient_code").asInstanceOf[String].trim()
      val topicCode = requestBody.get("topic_code").asInstanceOf[String].trim()
      val subscriptionId = senderCode + "-" + topicCode + "-" + recipientCode
      val insertQuery = s"INSERT INTO notifierlist(subscription_id,created_on,recipient_code,sender_code,subscription_status,topic_code,updated_on) VALUES('$subscriptionId',dateof(now()),'$recipientCode','$senderCode','Active','$topicCode', null) IF NOT EXISTS;"
      val result = cassandra.insert(insertQuery)
      if (!result)
        throw new ClientException(s"subscription already exist")
      else
        Ok(jsonToString(Map("subscriptionid" -> subscriptionId, "subscriptionstatus" -> "Active"))).as("application/json")
    }
    catch {
      case ex: ClientException =>
        BadRequest(jsonToString(Map("status" -> "Fail", "Message" -> ex.getMessage))).as("application/json")
    }
  }
  private def jsonToString(map: Map[String,String]) = {
    Json.stringify(Json.toJson(map))
  }
  private def jsonToMap(request: Request[AnyContent]): util.Map[String, Object] = {
    val json = request.body.asJson.getOrElse("{}").toString
    mapper.readValue(json, classOf[util.Map[String, Object]])
  }
/* validation for subscribe & unsubscribe */
  @throws(classOf[ClientException])
  def validateRequest(jsonMap: util.Map[String, Object]): Unit = {
    if(MapUtils.isEmpty(jsonMap))
      throw new ClientException("Invalid request body")
    val validateList: List[String] = List("sender_code", "recipient_code", "topic_code")
    for (property <- validateList) {
      validateProperty(jsonMap, property)
    }
  }

  private def validateProperty(jsonMap: util.Map[String, Object], property: String): Unit = {
    if (!jsonMap.containsKey(property) || jsonMap.getOrDefault(property, "").toString.isEmpty)
      throw new ClientException(s"$property is missing or empty")
  }

  /* Notification for unsubscribe */
  @throws(classOf[ClientException])
  def unsubscribe(): Action[AnyContent] = Action { request: Request[AnyContent] =>
    try {
      val requestBody = jsonToMap(request)
      validateRequest(requestBody)
      val senderCode = requestBody.get("sender_code").asInstanceOf[String].trim()
      val recipientCode = requestBody.get("recipient_code").asInstanceOf[String].trim()
      val topicCode = requestBody.get("topic_code").asInstanceOf[String].trim()
      val subscriptionId = senderCode + "-" + topicCode + "-" + recipientCode
      val updateQuery = s"update notifierlist set subscription_status = 'InActive',updated_on = dateof(now()) where subscription_id = '$subscriptionId' IF EXISTS;"
      val result = cassandra.insert(updateQuery)
      if (!result)
        throw new ClientException(s"subscription with $senderCode and  $recipientCode does not exist to unsubscribe")
      else
        Ok(jsonToString(Map("subscriptionid" -> subscriptionId, "subscriptionstatus" -> "InActive"))).as("application/json")
    }
    catch {
      case ex: ClientException =>
        BadRequest(jsonToString(Map("status" -> "fail", "message" -> ex.getMessage))).as("application/json")
    }
  }
  /* List of both Subscribe & Unsubscribe list */
  def subscriptionList(): Action[AnyContent] = Action { request: Request[AnyContent] =>
    try {
      val jsonMap = jsonToMap(request)
      if(MapUtils.isEmpty(jsonMap))
        throw new ClientException("Json request body is empty")
      validateProperty(jsonMap, "recipient_code")
      val recipientCode = jsonMap.get("recipient_code").asInstanceOf[String].trim()
      val searchQuery = s"select json * from notifierlist where recipient_code= '$recipientCode' ALLOW FILTERING"
      val result = cassandra.read(searchQuery)
      val responseList =new util.ArrayList[Any]()
      val rsList = result.asScala
         for (row <- rsList) {
           responseList.add(row.getString(0))
         }
      Ok(responseList.toString).as("application/json")
    }
    catch {
      case ex: ClientException =>
        BadRequest(jsonToString(Map("status" -> "fail", "message" -> ex.getMessage))).as("application/json")
    }
  }
}













