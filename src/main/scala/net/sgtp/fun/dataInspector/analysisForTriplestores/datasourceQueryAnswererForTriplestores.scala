package net.sgtp.fun.dataInspector.analysisForTriplestores

import net.sgtp.fun.dataInspector.model._
import org.apache.jena.query._
import org.apache.jena.rdf.model._
import org.apache.jena.rdf._
import net.sgtp.fun.dataInspector.body.counters
import net.sgtp.fun.dataInspector.analysis.datasourceQueryAnswerer
import net.sgtp.fun.dataInspector.body.Feature
import net.sgtp.fun.dataInspector.body.Feature._
import net.sgtp.fun.dataInspector.modelForTriplestores.roughTriplesResult

class datasourceQueryAnswererForTriplestores(verbose:Boolean,endpoint:String,queryTimeout1:Int,queryTimeout2:Int,counters:counters) extends datasourceQueryAnswerer {
  
  
  def retrieveRoughResults(location:Feature,searchString:String):roughTriplesResult={
    val queryString=location match {
      case VALUE => queries.searchForValueWithLiteral(searchString)
      case RESOURCE  =>queries.searchForResourcesForLiteral(searchString)
      case PROPERTY =>queries.searchForPropertiesForLiteral(searchString)
    }  
     //println("Query: "+queryString)
     val triples=helper.getTriplesPerQuery(verbose,endpoint,queryString,queryTimeout1,queryTimeout2,counters)
      val rRes=new roughTriplesResult(endpoint,searchString,location,triples)
      rRes
    
    
  }
  
  def countInstances(classURI:String):Int={
   val queryString=queries.countInstancesPerClass(classURI)
   val count=helper.selectSingleValueQuery(verbose,endpoint,queryString,queryTimeout1,queryTimeout2,counters)
   count
  }
  
  def getName(uri:String,nameProp:String):String={
    val labelString=if(nameProp.length>2) nameProp
    else helper.rdfsLabelString
    
    val queryString=queries.getValueForProp(uri, labelString)
    val label=helper.selectSingleLiteral(verbose,endpoint,queryString,queryTimeout1,queryTimeout2,counters)
    label
  }
  
  def getInstancesForClass(uri:String,number:Int):List[String]={
    val queryString=queries.getNoInstancesForClass(uri,number)
    //TEST
    println(queryString)
    val result=helper.selectMultipleResources(verbose,endpoint,queryString,queryTimeout1,queryTimeout2,counters)
    //TEST
    result.foreach(x=>println("I sampling: "+x))
    result
  }

  
  
}