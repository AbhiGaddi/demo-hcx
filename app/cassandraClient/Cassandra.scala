package cassandraClient

import com.datastax.driver.core.{Cluster, Row, Session}
import com.typesafe.config.{Config, ConfigFactory}

import java.util

class Cassandra {

  val conf: Config = ConfigFactory.load()
  val host: String = conf.getString("cassandra.host")
  val port: Int =conf.getInt("cassandra.port")

  def getSession: Session ={
    val cluster = Cluster.builder()
      .addContactPoint(host)
      .withPort(port)
      .build()
    cluster.connect("cassandrademo")
  }

  def read(query: String): util.List[Row] = {
    val result = getSession.execute(query)
    result.all()
  }

  def insert(query: String): Boolean = {
    val result = getSession.execute(query)
    result.wasApplied()
  }
}