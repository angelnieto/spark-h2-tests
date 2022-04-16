package es.ricardo

import io.cucumber.junit.{Cucumber, CucumberOptions}
import org.junit.runner.RunWith
import org.junit.{BeforeClass, AfterClass};
import es.ricardo.database.H2DatabaseCreator

@RunWith(classOf[Cucumber])
@CucumberOptions()
object TestRunner {

  @BeforeClass
  def setupClass() {
    H2DatabaseCreator.createTables()
    println("JUnit BeforeClass hook started");
  }
  
  @AfterClass
  def teardownClass() {
    H2DatabaseCreator.dropTables()
    println("JUnit AfterClass hook started");
  }
}