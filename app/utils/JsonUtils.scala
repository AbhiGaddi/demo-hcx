package utils

import com.fasterxml.jackson.databind.ObjectMapper
import play.api.libs.json.Json
import play.api.mvc.{AnyContent, Request}
import java.util


object JsonUtils {

  val mapper = new ObjectMapper

  def jsonToString(map: Map[String, String]) = {
    Json.stringify(Json.toJson(map))
  }

  def jsonToMap(request: Request[AnyContent]): util.Map[String, Object] = {
    val json = request.body.asJson.getOrElse("{}").toString
    mapper.readValue(json, classOf[util.Map[String, Object]])
  }

}
