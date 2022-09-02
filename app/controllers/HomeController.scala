package controllers                          //Pacakge name
import com.datastax.driver.core.Cluster
import com.fasterxml.jackson.databind.ObjectMapper
import exception.ClientException
import play.api.libs.json.Json
import play.api.mvc._

import java.util
import javax.inject._
import scala.collection.JavaConverters._
import scala.io.Source
import scala.language.{dynamics, postfixOps}

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
      val senderCode = requestBody.get("sender_code").asInstanceOf[String].trim()
      val recipientCode = requestBody.get("recipient_code").asInstanceOf[String].trim()
      val topicCode = requestBody.get("topic_code").asInstanceOf[String].trim()
      val subscriptionId = senderCode + "-" + topicCode + "-" + recipientCode
      val insertQuery = s"INSERT INTO notifierlist(subscription_id,created_on,recipient_code,sender_code,subscription_status,topic_code,updated_on) VALUES('$subscriptionId',dateof(now()),'$recipientCode','$senderCode','Active','$topicCode', null) IF NOT EXISTS;"
      val resultSet = session.execute(insertQuery)
      if (!resultSet.wasApplied())
        throw new ClientException(s"sender_code $senderCode and recipient_code $recipientCode already exist")
      else
        Ok(Json.stringify(Json.toJson(Map("subscriptionid" -> subscriptionId, "subscriptionstatus" -> "Active")))).as("application/json")

    }
    catch {
      case ex: ClientException =>
        BadRequest(Json.stringify(Json.toJson(Map("status" -> "Fail", "Message" -> ex.getMessage)))).as("application/json")
    }
  }

  def jsonToMap(request: Request[AnyContent]): util.Map[String, Object] = {
    val json = request.body.asJson.getOrElse("{}").toString
    mapper.readValue(json, classOf[util.Map[String, Object]])
  }

  @throws(classOf[ClientException])
  def validateRequest(jsonMap: util.Map[String, Object]): Unit = {
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
        val updateQuery = s"update notifierlist set subscription_status = 'InActive',updated_on = dateof(now()),created_on = null where subscription_id = '$subscriptionId' IF EXISTS;"
        val resultSet = session.execute(updateQuery)
        if (!resultSet.wasApplied())
          throw new ClientException(s"subscription with sender_code $senderCode and recipient_code $recipientCode does not exist to unsubscribe")
        else
          Ok(Json.stringify(Json.toJson(Map("subscriptionid" -> subscriptionId, "subscriptionstatus" -> "InActive")))).as("application/json")
      }
      catch {
        case ex: ClientException =>
          BadRequest(Json.stringify(Json.toJson(Map("status" -> "fail", "message" -> ex.getMessage)))).as("application/json")
      }
    }


    /* List of both Subscribe & Unsubscribe list */
    def subscriptionList(): Action[AnyContent] = Action { request: Request[AnyContent] =>
      val jsonMap = jsonToMap(request)
      validateProperty(jsonMap, "recipient_code")
      val recipientCode = jsonMap.get("recipient_code").asInstanceOf[String].trim()
      val searchQuery = s"select json * from notifierlist where recipient_code= '$recipientCode' ALLOW FILTERING"
      val resultSet = session.execute(searchQuery)
      val responseList = new util.ArrayList[Any]()
      val rsList = resultSet.all().asScala
      for (row <- rsList) {
        responseList.add(row.getString(0))
      }
      Ok(responseList.toString).as("application/json")
    }
}















