package es.ricardo.model

import scala.collection.mutable.Map

case class RowModification (
    operation: String, 
    table: String,
    fields: Map[String, Any],
    wFields: Option[Map[String, Any]]
)    