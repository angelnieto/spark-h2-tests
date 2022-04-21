package es.ricardo.database

import com.typesafe.scalalogging.LazyLogging
import java.util.Properties
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}


object Repository extends LazyLogging{
  
  var connectionProperties: Properties = new Properties()
  
  def readTable(dbConf: DatabaseConnectionSettings, tableName: String)(implicit spark: SparkSession) = {
    spark.read
      .format("jdbc")
      .option("url", dbConf.connectionString)
      .option("dbtable", tableName)
      .option("user", dbConf.username)
      .option("password", dbConf.password)
      .option("driver", dbConf.driver)
      .load()
  }
  
  def readTable(tableName: String)(implicit spark: SparkSession) = {
    if(!connectionProperties.isEmpty()){
          spark.read
          .format("jdbc")
            .option("url", connectionProperties.getProperty("url"))
            .option("dbtable", tableName)
            .option("user", connectionProperties.getProperty("username"))
            .option("password", connectionProperties.getProperty("password"))
            .option("driver", connectionProperties.getProperty("driver"))
            .load()
    } else {
        spark.read.table(tableName)
    }
  }
  
  def save(frame: DataFrame, sqlOutputDatabase: DatabaseConnectionSettings, tableName: String) = {
    println(s"Saving data into table $tableName")
    val connectionProperties: Properties = createJdbcProperties(sqlOutputDatabase)
    frame
      .write
      .mode(SaveMode.Append)
      .jdbc(sqlOutputDatabase.connectionString, tableName, connectionProperties)
      
    println(s"Data saved successfully into table $tableName")
  }
  
  def update(frame: DataFrame, sqlOutputDatabase: DatabaseConnectionSettings, tableName: String)(implicit spark: SparkSession) = {
    println(s"Updating data into table $tableName")
    val connectionProperties: Properties = createJdbcProperties(sqlOutputDatabase)
    // Debemos crear una tabla temporal porque spark no permite leer y escribir en la misma tabla
    frame
      .write
      .mode(SaveMode.Overwrite)
      .saveAsTable("temp_table")
    
    spark.sqlContext.table("temp_table").write
      .mode(SaveMode.Overwrite)
      .jdbc(sqlOutputDatabase.connectionString, tableName, connectionProperties)
      
    println(s"Data updated successfully into table $tableName")
  }
  
  def createJdbcProperties(outputDatabase: DatabaseConnectionSettings) = {
    if(connectionProperties.isEmpty()){
      connectionProperties.put("username", outputDatabase.username)
      connectionProperties.put("password", outputDatabase.password)
      connectionProperties.put("driver", outputDatabase.driver)
      connectionProperties.put("url", outputDatabase.connectionString)
    }
    connectionProperties
  }
 


}
