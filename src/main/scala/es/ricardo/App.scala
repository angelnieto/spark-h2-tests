package es.ricardo

import es.ricardo.database.{DatabaseConnectionSettings, Repository}
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.{Dataset, SparkSession, DataFrame}
import es.ricardo.database.Repository

class App(implicit spark: SparkSession) extends LazyLogging {
  
  def readTable(tableName: String): DataFrame = {
    Repository.readTable(tableName)
  }
  
}
