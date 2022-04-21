package es.ricardo.steps

import io.cucumber.scala.{EN, ScalaDsl}
import org.junit.AfterClass
import org.junit.BeforeClass
import org.apache.spark.sql.{SparkSession, DataFrame}
import es.ricardo.database.{H2RowModifier, H2DatabaseCreator}
import es.ricardo.model.RowModification
import es.ricardo.App

import es.ricardo.model.{Person, Student}
import org.junit.Assert
import scala.collection.mutable.Map
import es.ricardo.database.H2RowModifier
import org.apache.spark.sql.functions.col

class StepDefinitions extends ScalaDsl with EN {
  
  implicit val spark = SparkSession.builder.appName("test").master("local").getOrCreate
  import spark.implicits._
  
  var lawyers: DataFrame = _
  
  Given("""an {string} operation in table {string} to insert a new student with the value {string} in the field {string} and the value {string} in the field {string}""") { 
    (operation: String, table: String, value1: String, field1: String, value2: String, field2: String) =>
     var fields = Map[String, Any]() 
     fields += (field1 -> value1)
     fields += (field2 -> value2)
     
     val rowModification: RowModification = RowModification(operation, table, fields, None)
     new H2RowModifier().saveOrUpdate(H2DatabaseCreator.outputDbConf, rowModification)
     
  }
  
  And("""an {string} operation in table {string} we set the value {string} in the field {string} for the student with field {string} with value {int}""") { 
    (operation: String, table: String, value1: String, field1: String, wField: String, wValue: Int) =>
     var fields = Map[String, Any]() 
     fields += (field1 -> value1)
     var wFields = Map[String, Any]()
     wFields += (wField -> wValue)
     
     val rowModification: RowModification = RowModification(operation, table, fields, Some(wFields))
     new H2RowModifier().saveOrUpdate(H2DatabaseCreator.outputDbConf, rowModification)
  }
  
  When("""the {string} data is collected""") { (table: String) =>
    lawyers = new App().readTable(table)
    println("lawyers")
    lawyers.show()
  }
  
  Then("""the student in the table {string} with field {string} with value {int} changed the field {string} to the value {string}""") {
    (table: String, wField1: String, wValue1: Int, field1: String, value1: String) =>
    Assert.assertEquals(value1, lawyers.filter(col(wField1).===(wValue1)).first().getAs(field1.toUpperCase()))
  }
  
  Then("""the student in the table {string} with field {string} with value {string} has the value {string} in the field {string}""") { 
    (table: String, field1: String, value1: String, value2: String, field2: String) =>
    Assert.assertEquals(value2, lawyers.filter(col(field1).===(value1)).first().getAs(field2.toUpperCase()))
  }
  
}