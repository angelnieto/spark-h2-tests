package es.ricardo.database

import es.ricardo.database.{DatabaseConnectionSettings, Repository}
import org.apache.spark.sql.{Dataset, SparkSession}
import es.ricardo.model.{Person, Student, RowModification}
import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.sql.functions.{when, col, lit}
import org.apache.spark.sql.types._

class H2RowModifier {
    
    def saveOrUpdate(outputDb: DatabaseConnectionSettings, row: RowModification)(implicit spark: SparkSession) = {
      import spark.implicits._
      
      row.operation.toUpperCase() match {
          case "UPDATE" => {
              val results: DataFrame = Repository.readTable(outputDb, row.table).cache()
              var resultsFiltered: DataFrame = results
              row.wFields.get.map {case (key, value) =>  resultsFiltered = resultsFiltered.filter(col(key).===(value))}
               
              var resultsToPersist = results.except(resultsFiltered)
              
              println("students tras SELECT")
              results.show()
              
              var resultsUpdated: DataFrame = resultsFiltered
              row.fields map {case (key, value) => ( resultsUpdated = resultsUpdated.withColumn(key.toUpperCase(), when(col(key).notEqual(value), value)) )}
                               
              resultsToPersist = resultsToPersist.union(resultsUpdated)
              
              println("students tras UPDATE")
              resultsToPersist.show()
              
              Repository.update(resultsToPersist, outputDb, row.table)
          }
          case "INSERT" => {
            val results: DataFrame = Repository.readTable(outputDb, row.table).cache()

            var dataTypes: Array[StructField] = Array()
            var values: Array[Any] = Array()
            
            row.fields map {case (key, value) => ({
                  dataTypes = dataTypes :+ StructField(key.toUpperCase(), results.schema(key.toUpperCase()).dataType, true)
                  values = values :+ value
            })}
           
            val rowToAdd = spark.sqlContext.createDataFrame(spark.sparkContext.parallelize(List(Row.fromSeq(values))), StructType(dataTypes.toList))
             
            println("rowToAdd")
            rowToAdd.show()
           
            Repository.save(rowToAdd, outputDb, row.table)  
          }  
        }
    }
}