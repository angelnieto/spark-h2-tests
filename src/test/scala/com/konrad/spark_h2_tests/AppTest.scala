package com.konrad.spark_h2_tests

import com.konrad.spark_h2_tests.database.Repository
import com.konrad.spark_h2_tests.model.Person
import com.konrad.spark_h2_tests.util.H2DatabaseCreator
import org.apache.spark.sql.SparkSession
import org.scalatest.BeforeAndAfterAll
//import org.scalatest.matchers.should.Matchers
import com.holdenkarau.spark.testing.DataFrameSuiteBase
import org.scalatest.mockito.MockitoSugar
import org.junit.Assert
import org.scalatest.BeforeAndAfterAll

class AppTest extends org.scalatest.FunSuite with BeforeAndAfterAll with MockitoSugar {
  override protected def beforeAll(): Unit = {
    super.beforeAll()
    H2DatabaseCreator.createTables()
    println("beforeAll")
  }

  override protected def afterAll(): Unit = {
    println("afterAll")
    H2DatabaseCreator.dropTables()
    super.afterAll()
  }

  implicit val spark = SparkSession.builder.appName("test").master("local").getOrCreate
  import spark.implicits._

  test("should read students and save persons") {
    //when
    new App().ingest(H2DatabaseCreator.inputDbConf, H2DatabaseCreator.outputDbConf)
    
    //then
    val persons = Repository.readTable(H2DatabaseCreator.outputDbConf, "person").as[Person].collect()
     Assert.assertEquals(Person("Konrad Cala"), persons(0))
     Assert.assertEquals(Person("John Doe"), persons(1))
  }

}
