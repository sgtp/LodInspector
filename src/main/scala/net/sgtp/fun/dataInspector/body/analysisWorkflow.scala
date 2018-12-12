package net.sgtp.fun.dataInspector.body

import net.sgtp.fun.dataInspector.model._

class analysisWorkflow (verbose:Boolean,endpoint:String,searchString:String,queryTimeout1:Int,queryTimeout2:Int) {
  val eAnalyzer=new endpointAnalyzer(verbose,endpoint,queryTimeout1,queryTimeout2); 
  val queryStrings=List(
       ("value",queries.searchForValueWithLiteral(searchString)),
       ("res", queries.searchForResourcesForLiteral(searchString)),
       ("prop",queries.searchForPropertiesForLiteral(searchString))
   )
   queryStrings.foreach(q=>{
     val roughResult=eAnalyzer.retrieveRoughResults(q._1,q._2)
     if(roughResult.triples.size>0) {
       counters.queriesWithResults+=1
       if(verbose) println("Found triples "+roughResult.triples.size+" in "+roughResult.endpoint+" for "+roughResult.query)
     }
     val matureResult=new MatureResult(roughResult)
    
   })

  
 
}