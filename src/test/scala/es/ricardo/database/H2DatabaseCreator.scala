package es.ricardo.database

import java.nio.charset.StandardCharsets
import com.typesafe.scalalogging.LazyLogging
import org.h2.tools.RunScript

object H2DatabaseCreator extends LazyLogging {
  private val inputH2Url = "jdbc:h2:mem:inputDb;MODE=MSSQLServer;USER=sa;DB_CLOSE_DELAY=-1"
  private val outputH2Url = "jdbc:h2:mem:outputDb;MODE=MSSQLServer;USER=sa;DB_CLOSE_DELAY=-1"

  val inputDbConf = DatabaseConnectionSettings(H2DatabaseCreator.inputH2Url, "sa", "", "org.h2.Driver")
  val outputDbConf = DatabaseConnectionSettings(H2DatabaseCreator.outputH2Url, "sa", "", "org.h2.Driver")

  def createTables(): Unit = {
    logger.info("Creating tables in test databases")
    RunScript.execute(outputH2Url, "sa", "", "classpath:createDatabase.sql", StandardCharsets.UTF_8, false)
     RunScript.execute(outputH2Url, "sa", "", "classpath:insertDatabase.sql", StandardCharsets.UTF_8, false)
    logger.info("Create scripts run successfully")
  }

  def dropTables(): Unit = {
    logger.info("Dropping all tables from test databases")
    RunScript.execute(outputH2Url, "sa", "", "classpath:dropTables.sql", StandardCharsets.UTF_8, false)
    logger.info("Tables dropped")
  }
}

