package net.sgtp.fun.dataInspector.body

import org.apache.jena.query._;
import org.apache.jena.rdf.model._

object helper {
  def getTriplesPerQuery(verbose:Boolean=false,endpoint:String,queryString:String,queryTimeout1:Int,queryTimeout2:Int):Model={endpoint
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
  
  def slectSingleValueQuery(verbose:Boolean=false,endpoint:String,queryString:String,queryTimeout1:Int,queryTimeout2:Int):Int={
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
}