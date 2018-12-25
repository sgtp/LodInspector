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
import net.sgtp.fun.dataInspector.io.simpleCyFileOut
import net.sgtp.fun.dataInspector.body.endpointSelector
import net.sgtp.fun.dataInspector.body.counters
import net.sgtp.fun.dataInspector.body.analysisWorkflow
import scala.collection.parallel._

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
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    
    // needed for the future map/flatmap in the end
    implicit val executionContext = system.dispatcher

    //val addThenDouble: (Int, Int) => Int = (x,y) => {
    val requestHandler: HttpRequest => HttpResponse = hr => {
      println(hr.uri.path.toString())
      hr match {
        case HttpRequest(GET, Uri.Path("/params"), _, _, _) =>  {
           val query=hr.uri.query(java.nio.charset.Charset.defaultCharset(), Uri.ParsingMode.Strict)
           //query.toMap.foreach(x=>println(x._1+" => "+x._2))
           val searchString=query.toMap.get("search").get
           val searchStrings=searchString.split("_") //TODO uredecode and split
           val randomDir=Random.alphanumeric.take(10).mkString("")
           //TODO here we should start the asynch thing...
           val dataFile="resources/web/temp/"+randomDir+"/outCy.txt"
           new java.io.File("resources/web/temp/"+randomDir).mkdirs
           val dataUrl="/temp/"+randomDir+"/outCy.txt"
           Future {
               ///temp/90CUqsE6kL/outCy.txt
               //resources/web/temp/90CUqsE6kL/outCy.txt
               
               val cyOut=new simpleCyFileOut(dataFile)
               val availableEndpoints= endpointSelector.listUpInUmaka(75)
               availableEndpoints.foreach(println)
                val forkJoinPool = new java.util.concurrent.ForkJoinPool(200)
                val parExp=availableEndpoints.par
                parExp.tasksupport=new ForkJoinTaskSupport(forkJoinPool)
  
                parExp.foreach(ep=>{
                   counters.endPointOpened+=1;
                   searchStrings.par.foreach(
                   str=>{
        
              val aWorkflow=new analysisWorkflow(true,ep,str,6000,6000,cyOut)
        
    
                    }
                )
              counters.endPointTerminated+=1
            })
           }
           println("writing to: "+dataFile)
           HttpResponse(entity = dataUrl)
          }
        case HttpRequest(GET, _, _, _, _) => {
          val file="resources/web"+hr.uri.path.toString()
          println(file)
          //sys.props.foreach(println)
          
          val fileContent= scala.io.Source.fromFile(file, "UTF8").mkString
          println(fileContent)
          //val httpResp : HttpResponse = fileEntityResponse(file, "UTF8")
          //httpResp
          
          HttpResponse(entity = HttpEntity.fromFile(ContentTypes.`text/html(UTF-8)`, new File(file), 4096))
          //val contentNotSureWhy = Source.single(ByteString(fileContent))
          //HttpResponse(entity = HttpEntity.Chunked.fromData(ContentTypes.`application/octet-stream`, contentNotSureWhy))
          //println(fileContent)
          //
          //HttpResponse(404,entity=file)
        }
         
       /*
        * https://stackoverflow.com/questions/49011621/how-to-send-a-file-as-a-response-using-akka-http
        * 
        * 
        * def handleCall(request:HttpRequest):HttpResponse = {
  logger.info("Request is {}",request)
  val uri:String = request.getUri().path()
  if(uri == "/download"){
    val f = new File("/1000.txt")
    logger.info("file download")
    return HttpEntity(
    //What should i put here if i want to return a text file.
    )
}



val str2 = scala.io.Source.fromFile("/tmp/t.log", "UTF8").mkString
val str = Source.single(ByteString(str2))
HttpResponse(entity = HttpEntity.Chunked.fromData(ContentTypes.`application/octet-stream`, str))


        */
        case HttpRequest(GET, Uri.Path("/web"), _, _, _) =>{
          
          HttpResponse(entity = HttpEntity(
            // Get the file
          ContentTypes.`text/html(UTF-8)`,
          "<html><body>Hi!</body></html>"))
          }
			
         

      //case HttpRequest(GET, Uri.Path("/crash"), _, _, _) =>
          //Get the string
       // sys.error("BOOM!")
        
      case r: HttpRequest =>
        r.discardEntityBytes() // important to drain incoming HTTP Entity stream
        HttpResponse(404, entity = "Unknown resource!")
    }
    }
    val bindingFuture = Http().bindAndHandleSync(requestHandler, "localhost", 8090)
    println(s"Server online at http://localhost:8090/\nPress RETURN to stop...")
   

  }
}