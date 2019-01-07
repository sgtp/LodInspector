package net.sgtp.fun.dataInspector.exec

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.http.scaladsl.model.HttpEntity.apply
import akka.http.scaladsl.model.HttpMessage.HttpMessageScalaDSLSugar
import akka.http.scaladsl.model.StatusCode.int2StatusCode
import scala.util.Random
import akka.http.scaladsl.server.directives._
import ContentTypeResolver.Default
import akka.stream.scaladsl.Source
import akka.http.scaladsl.model.HttpEntity.{Chunked, ChunkStreamPart}
import java.io.File
import scala.concurrent.Future
import net.sgtp.fun.dataInspector.body.NodesMemory
import net.sgtp.fun.dataInspector.analysisForTriplestores.endpointSelector
import net.sgtp.fun.dataInspector.body.ExecutionEngine
import net.sgtp.fun.dataInspector.body.counters
import scala.collection.parallel._
import net.sgtp.fun.dataInspector.body.Manifest
import java.net.URLDecoder
import net.sgtp.fun.dataInspector.body.DataSourceType._

/** 
 * Very simple server to support the Data Inspector
 * Note: adapted from https://doc.akka.io/docs/akka-http/current/routing-dsl/overview.html
 * TODO this was done in a hurry without really knowing the logic of Akka/Http. 
 * Should double check if what is below is correct: 
 * 1) are all errors handled properly?
 * 2) is there some issue with buffers/memory leaks?
 * 3) should all be based on actors?
 * 4) should 
 */
object simpleServer extends App {
  
  override def main(args: Array[String]) {
    println("Starting "+Manifest.projectName+" v."+Manifest.version+" (web server interface)")
    val sessionMaps=collection.mutable.Map[String, NodesMemory]()
    //TODO however this works...
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    // needed for the future map/flatmap in the end
    implicit val executionContext = system.dispatcher

    val ops=OptionsParser.parseCli(args)
    
    val requestHandler: HttpRequest => HttpResponse = hr => {
      if(ops.verbose) println("URL reuqest:  "+hr.uri.path.toString())
      hr match {
        case HttpRequest(GET, Uri.Path("/params"), _, _, _) =>  {
           val query=hr.uri.query(java.nio.charset.Charset.defaultCharset(), Uri.ParsingMode.Strict)
           val myArgs=query.toMap
           OptionsParser.parseArgsMapAndAdd(myArgs, ops)
           val searchStrings=URLDecoder.decode(myArgs.get("search").get).split(" ").map(x=>x.replaceAll("_", " ")).toList
           val randomDir=Random.alphanumeric.take(10).mkString("")
           val dataFile=ops.webDir+"temp/"+randomDir+"/outCy.txt"
           new java.io.File(ops.webDir+"temp/"+randomDir).mkdirs
           val dataUrl="/temp/"+randomDir+"/outCy.txt"
           val cyOut=new NodesMemory(dataFile)
           sessionMaps.put(randomDir, cyOut)
           if(ops.verbose) {
             println("Search strings:")
             searchStrings.foreach(println)
             ops.print()
           }
           //Asynch block
           Future {             
               
               val availableEndpoints= if(myArgs.contains("e")) myArgs.get("e").get.split(" ").toList
               else endpointSelector.listUpInUmaka(ops.yummyScore).toList
               if(ops.verbose) {
                 println("Endpoints to inspect:")
                 availableEndpoints.foreach(println)
               }
               
               val availableEndpointsTypes=availableEndpoints.map(x=>(x,ENDPOINT))
               val seeder=new ExecutionEngine(availableEndpointsTypes,searchStrings,ops,cyOut)
               seeder.exec()
               
            }
           
           println("writing to: "+dataFile)
           //HttpResponse(entity = dataUrl)
           HttpResponse(entity = randomDir)
          }
         case HttpRequest(GET, Uri.Path("/results"), _, _, _) =>  {
           val query=hr.uri.query(java.nio.charset.Charset.defaultCharset(), Uri.ParsingMode.Strict)
           val myArgs=query.toMap
           val seed=query.get("seed").get
           println(sessionMaps.get(seed).get.dumpToString())
           HttpResponse(entity = sessionMaps.get(seed).get.dumpToString())
    
      } 
        case HttpRequest(GET, _, _, _, _) => {
          val file="resources/web"+hr.uri.path.toString()
          println("HTTP GET: "+file)
         
          
          val fileContent= scala.io.Source.fromFile(file, "UTF8").mkString
          //println(fileContent)
          
          HttpResponse(entity = HttpEntity.fromFile(ContentTypes.`text/html(UTF-8)`, new File(file), 4096))
          
        }
         
       /*
        * https://stackoverflow.com/questions/49011621/how-to-send-a-file-as-a-response-using-akka-http
        * 

        */

      case r: HttpRequest =>
        r.discardEntityBytes() // important to drain incoming HTTP Entity stream
        HttpResponse(404, entity = "Unknown resource!")
    }
    }
    val bindingFuture = Http().bindAndHandleSync(requestHandler, "localhost", ops.port)
    println(s"Server online at http://localhost:8090/\nPress RETURN to stop...")
   

  }
}