package net.sgtp.fun.dataInspector.modelForTriplestores

import net.sgtp.fun.dataInspector.model.roughResults
import net.sgtp.fun.dataInspector.body.DataSourceType._
import net.sgtp.fun.dataInspector.body.Feature._
import org.apache.jena.rdf.model._

// val dataSource:String, val dataSourceType:DataSourceType,query:String,featureFound:Feature
class roughTriplesResult(endpoint:String,qString:String,fFound:Feature,model:Model) extends roughResults (endpoint,ENDPOINT,qString,fFound){
  val triples=model
  def resultsSize=triples.size
  def hasResults=triples.size>0
  def resultsSizeMeasure="triples"
  
}