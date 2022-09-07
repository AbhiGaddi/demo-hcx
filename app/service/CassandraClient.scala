package service

import com.datastax.driver.core.{Cluster, Row, Session}
import com.typesafe.config.{Config, ConfigFactory}

import java.util

class CassandraClient {

  val conf: Config = ConfigFactory.load()
  val host: String = conf.getString("cassandra.host")
  val port: Int =conf.getInt("cassandra.port")
  val keyspaceName: String = conf.getString("cassandra.keyspaceName")

  def getSession: Session ={
    val cluster = Cluster.builder()
      .addContactPoint(host)
      .withPort(port)
      .build()
    cluster.connect(keyspaceName)
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