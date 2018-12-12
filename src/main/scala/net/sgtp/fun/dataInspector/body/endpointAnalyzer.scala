package net.sgtp.fun.dataInspector.body

import net.sgtp.fun.dataInspector.model.roughTriplesResult
import org.apache.jena.query._;
import org.apache.jena.rdf.model._
import org.apache.jena.rdf._

object endpointAnalyzer {
  def retrieveRoughResultsAndBroadCast(verbose:Boolean=false,endpoint:String,searchString:String,queryTimeout1:Int,queryTimeout2:Int)={
    val queryString1=queries.searchForValueWithLiteral(searchString)
    val queryString2=queries.searchForResourcesForLiteral(searchString)
    val queryString3=queries.searchForPropertiesForLiteral(searchString)
    
    //if(verbose) println(endpoint+" issuing query 1 for "+searchString)
    val res1=helper.getTriplesPerQuery(verbose,endpoint,queryString1,queryTimeout1,queryTimeout2)
    resultsProcessor.processRoughResults( new roughTriplesResult(endpoint,searchString,"value",res1))
    //if(verbose) println(endpoint+" query 1 done")
    counters.queriesExecuted+=1
    
    //if(verbose) println(endpoint+" issuing query 2 for "+searchString)
    val res2=helper.getTriplesPerQuery(verbose,endpoint,queryString2,queryTimeout1,queryTimeout2)
    resultsProcessor.processRoughResults( new roughTriplesResult(endpoint,searchString,"res",res2))
    //if(verbose) println(endpoint+" query 2 done")
    counters.queriesExecuted+=1
    
    //if(verbose) println(endpoint+" issuing query 3 for "+searchString)
    val res3=helper.getTriplesPerQuery(verbose,endpoint,queryString3,queryTimeout1,queryTimeout2)
    resultsProcessor.processRoughResults( new roughTriplesResult(endpoint,searchString,"prop",res3))
    //if(verbose) println(endpoint+" query 3 done")
    counters.queriesExecuted+=1
    
    
  }
  
  def processRoughResult(res:roughTriplesResult)={
    println("Processing result of size "+res.triples.size)
    res.locationFound match {
      case "value" => {
         println("Found in value")
         if(res.triples.contains(null,ResourceFactory.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),ResourceFactory.createResource("http://www.w3.org/2002/07/owl#Class"))) println("=> Class")
         else if(res.triples.contains(null,ResourceFactory.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),ResourceFactory.createResource("http://www.w3.org/2002/07/owl#ObjectProperty"))) println("=> Relation")
         else if(res.triples.contains(null,ResourceFactory.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),ResourceFactory.createResource("http://www.w3.org/2002/07/owl#DatatypeProperty"))) println("=> Attribute")
         else if(res.triples.contains(null,ResourceFactory.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),null)) println("=> Instance")
         else println("=> Unqualifed")

         //entity could be  res, attr, or prop
      }
      case "res" => {
        println("Found in res")
        //TODO this won't work, need find triples
        if(res.triples.contains(null,ResourceFactory.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),ResourceFactory.createResource("http://www.w3.org/2002/07/owl#Class"))) println("=> Class")
        if(res.triples.contains(null,ResourceFactory.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),ResourceFactory.createResource("http://www.w3.org/2002/07/owl#ObjectProperty"))) println("=> Relation")
        if(res.triples.contains(null,ResourceFactory.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),ResourceFactory.createResource("http://www.w3.org/2002/07/owl#DatatypeProperty"))) println("=> Attribute")
        //entity could be  res, attr, or prop
      }
      case "prop" => {
        println("Found in prop")
        //TODO this won't work, need find triples
        if(res.triples.contains(null,ResourceFactory.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),ResourceFactory.createResource("http://www.w3.org/2002/07/owl#ObjectProperty"))) println("=> Relation")
        if(res.triples.contains(null,ResourceFactory.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),ResourceFactory.createResource("http://www.w3.org/2002/07/owl#DatatypeProperty"))) println("=> Attribute") 
        //entity could be prop or attr
        
      }
    }
  }
}