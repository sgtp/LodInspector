package net.sgtp.fun.dataInspector.body

import org.apache.jena.query._;
import org.apache.jena.rdf.model._

object helper {
  def getTriplesPerQuery(verbose:Boolean=false,endpoint:String,queryString:String,queryTimeout1:Int,queryTimeout2:Int):Model={
    val m=ModelFactory.createDefaultModel()
    val query=QueryFactory.create(queryString)
    val qexec = QueryExecutionFactory.sparqlService(endpoint, query)
    //qexec.setTimeout(queryTimeout1,queryTimeout2)
    try {
      val innerModel=qexec.execConstruct()
       m.add(innerModel)
    }
    catch {case e=>{
      if(verbose) {
        println(endpoint+"\t: is not viable for "+queryString)
        println(e.getMessage)
      }
      m}}
    
    
  
  }
}