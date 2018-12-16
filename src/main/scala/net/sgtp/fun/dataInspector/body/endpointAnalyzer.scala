package net.sgtp.fun.dataInspector.body

import net.sgtp.fun.dataInspector.model._
import org.apache.jena.query._;
import org.apache.jena.rdf.model._
import org.apache.jena.rdf._

class endpointAnalyzer(verbose:Boolean,endpoint:String,queryTimeout1:Int,queryTimeout2:Int) {
  def retrieveRoughResults(location:String,searchString:String)={
    val queryString=location match {
      case "value" => queries.searchForValueWithLiteral(searchString)
      case "res"  =>queries.searchForResourcesForLiteral(searchString)
      case "prop" =>queries.searchForPropertiesForLiteral(searchString)
    }  
     //println("Query: "+queryString)
     val triples=helper.getTriplesPerQuery(verbose,endpoint,queryString,queryTimeout1,queryTimeout2)
      val rRes=new roughTriplesResult(endpoint,searchString,location,triples)
      rRes
    
    
  }
  
  def countInstances(classURI:String):Int={
   val queryString=queries.countInstancesPerClass(classURI)
   val count=helper.slectSingleValueQuery(verbose,endpoint,queryString,queryTimeout1,queryTimeout2)
   count
  }
  
  
  
  
  def fillResultItem()={}
  
  def profileResultItem()={}
  
  
}