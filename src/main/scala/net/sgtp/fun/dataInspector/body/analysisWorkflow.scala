package net.sgtp.fun.dataInspector.body

import net.sgtp.fun.dataInspector.io.simpleCyFileOut
import net.sgtp.fun.dataInspector.model._

class analysisWorkflow (verbose:Boolean,endpoint:String,searchString:String,queryTimeout1:Int,queryTimeout2:Int) {
  val serializer=new simpleCyFileOut("resources/web/outCy.txt")
  val eAnalyzer=new endpointAnalyzer(verbose,endpoint,queryTimeout1,queryTimeout2); 
  val queryToProcess=List(
       ("value",searchString),
       ("res", searchString),
       ("prop",searchString)
   )
   queryToProcess.foreach(q=>{
     val roughResult=eAnalyzer.retrieveRoughResults(q._1,q._2)
     if(roughResult.triples.size>0) {
       if(verbose) println("Found triples "+roughResult.triples.size+" in "+roughResult.endpoint+" for "+roughResult.query)
       val matureNodes=NodeFactory.extractFromRoughResults(roughResult);
       matureNodes.foreach(mn=>{serializer.process(mn)})
       matureNodes.foreach(x=>{val pm=x.getProfiled(eAnalyzer); serializer.process(pm) })
      }
     //val matureResult=new MatureResult(roughResult)
     

     // Need to fill
     // Need to profile
   })

  
 
}