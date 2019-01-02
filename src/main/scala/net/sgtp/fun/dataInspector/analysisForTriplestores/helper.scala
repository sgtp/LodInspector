package net.sgtp.fun.dataInspector.analysisForTriplestores

import org.apache.jena.query._
import org.apache.jena.rdf.model._
import net.sgtp.fun.dataInspector.body.counters

object helper {
  def typeProp=ResourceFactory.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")
  def classRes=ResourceFactory.createResource("http://www.w3.org/2002/07/owl#Class")
  def rdfClassRes=ResourceFactory.createResource("http://www.w3.org/2000/01/rdf-schema#Class")
  def statRes=ResourceFactory.createResource("http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement")
  def rdfsLabelString="http://www.w3.org/2000/01/rdf-schema#label"  
  
  def getTriplesPerQuery(verbose:Boolean=false,endpoint:String,queryString:String,queryTimeout1:Int,queryTimeout2:Int,counters:counters):Model={
    counters.recordOpen()
    val m=ModelFactory.createDefaultModel()
    val query=QueryFactory.create(queryString)
    val qexec = QueryExecutionFactory.sparqlService(endpoint, query)
    //qexec.setTimeout(queryTimeout1,queryTimeout2)
    try {
      val innerModel=qexec.execConstruct()
      if(innerModel.size>0) counters.recordSuccessWithResults()
      else counters.recordSucessNoResult()
       m.add(innerModel)
    }
    catch {case e=>{
      if(verbose) {
        println(endpoint+"\t: is not viable for "+queryString)
        println(e.getMessage)
        counters.recordFailue()
      }
      m}}
  }
  
  def selectSingleValueQuery(verbose:Boolean=false,endpoint:String,queryString:String,queryTimeout1:Int,queryTimeout2:Int,counters:counters):Int={
    counters.recordOpen()
    val query=QueryFactory.create(queryString)  
    val qexec = QueryExecutionFactory.sparqlService(endpoint, query)
    try {
      val resSet=qexec.execSelect()
      if(resSet.hasNext()) {
        counters.recordSuccessWithResults()
        resSet.next().getLiteral("res").getInt
      }
      else {
        counters.recordSucessNoResult() 
        0
      }
    }
    catch {
      case e=>{
       if(verbose) {
          println(endpoint+"\t: is not viable for "+queryString)
          counters.recordFailue()
          println(e.getMessage)
      }
      0
      }
    }
   }
  
   def selectSingleLiteral(verbose:Boolean=false,endpoint:String,queryString:String,queryTimeout1:Int,queryTimeout2:Int,counters:counters):String={
    counters.recordOpen()
    val query=QueryFactory.create(queryString)  
    val qexec = QueryExecutionFactory.sparqlService(endpoint, query)
    try {
      val resSet=qexec.execSelect()
      if(resSet.hasNext()) {
        counters.recordSuccessWithResults()
        resSet.next().getLiteral("res").getString
      }
      else {
        counters.recordSucessNoResult() 
        ""
      }
    }
    catch {
      case e=>{
       if(verbose) {
          println(endpoint+"\t: is not viable for "+queryString+" reason: "+e.getMessage)
          counters.recordFailue()
          
      }
      ""
      }
    }
   }
}