package net.sgtp.fun.dataInspector.body

import net.sgtp.fun.dataInspector.model.roughTriplesResult
object resultsProcessor {
  var hitsCounter=0
  def processRoughResults(res:roughTriplesResult)={
    if(res.triples.size>0) {
      println("Found triples "+res.triples.size+" in "+res.endpoint+" for "+res.query)
      hitsCounter+=1
      println("No. of hits: "+hitsCounter)
      val res2=endpointAnalyzer.processRoughResult(res)
    }
  }
}