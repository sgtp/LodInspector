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

//Note: adapted from https://doc.akka.io/docs/akka-http/current/routing-dsl/overview.html
object simpleServer extends App {

  override def main(args: Array[String]) {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    
    // needed for the future map/flatmap in the end
    implicit val executionContext = system.dispatcher

    //val addThenDouble: (Int, Int) => Int = (x,y) => {
    val requestHandler: HttpRequest => HttpResponse = hr => {
      hr match {
        case HttpRequest(GET, Uri.Path("/"), _, _, _) =>
          HttpResponse(entity = HttpEntity(
            // Get the file
          ContentTypes.`text/html(UTF-8)`,
          "<html><body>Hi!</body></html>"))

         case HttpRequest(GET, Uri.Path("/params"), _, _, _) =>  {
           val query=hr.uri.query(java.nio.charset.Charset.defaultCharset(), Uri.ParsingMode.Strict)
           query.toMap.foreach(x=>println(x._1+" => "+x._2))
           val dataFile=Random.alphanumeric.take(10).mkString("")
           HttpResponse(entity = dataFile)
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