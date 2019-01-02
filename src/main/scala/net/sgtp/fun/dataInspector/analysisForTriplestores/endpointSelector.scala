package net.sgtp.fun.dataInspector.analysisForTriplestores

import org.json4s._
import org.json4s.native.JsonMethods._

object endpointSelector {
  def listUpInUmaka(score:Int):Array[String]={
     val umakaEndpointsQuery="http://d.umaka.dbcls.jp/api/endpoints/search?alive_rate_lower="+score
     val result=scala.io.Source.fromURL(umakaEndpointsQuery).getLines.mkString("\n")
     val jsonParsedResult=parse(result)
     val endpoints=for {
        JObject(endpoint) <- jsonParsedResult
        JField("url", JString(url))  <- endpoint
      } yield url  
      endpoints.toArray[String]
  } 
  
  def filterBySearch(sTerms:Array[String])={
    
  }
  
}