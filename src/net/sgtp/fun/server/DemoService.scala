package net.sgtp.fun.server

import scala.concurrent.duration._
import akka.pattern.ask
import akka.util.Timeout
import akka.actor._
import spray.can.Http
import spray.can.server.Stats
import spray.util._
import spray.http._
import HttpMethods._
import MediaTypes._
import spray.can.Http.RegisterChunkHandler

class DemoService extends Actor with ActorLogging {
  implicit val timeout: Timeout = 300.second // for the actor 'asks'
  import context.dispatcher // ExecutionContext for the futures and scheduler

  def receive = {
    // when a new connection comes in we register ourselves as the connection handler
    case _: Http.Connected => sender ! Http.Register(self)

    case HttpRequest(GET, Uri.Path("/"), _, _, _) =>
      sender ! index

    case HttpRequest(GET, Uri.Path("/ping"), _, _, _) =>
      sender ! HttpResponse(entity = "PONG!")
      
    case HttpRequest(GET, Uri.Path("/WordSearch"), _, _, _) =>
      val peer = sender // since the Props creator is executed asyncly we need to save the sender ref
      context actorOf Props(new WordSearch(peer,1))
      
    case _: HttpRequest => sender ! HttpResponse(status = 404, entity = "Unknown resource!")

   
  }

  ////////////// helpers //////////////

  lazy val index = HttpResponse(
    entity = HttpEntity(`text/html`,
      <html>
        <body>
          <h1>Well, we give it a try</h1>
					<div id="serverData">Here is where thing will appear</div>
          <p>Other things around here</p>
          <ul>
            <li><a href="/ping">/ping</a></li>
            <li><a href="/stream">/stream</a></li>
            <li><a href="/timeout">/timeout</a></li>
            <li><a href="/timeout/timeout">/timeout/timeout</a></li>
            <li><a href="/stop">/stop</a></li>
          </ul>
          <p>Test file upload</p>
          <form action ="/WordSearch" enctype="multipart/form-data" method="get">
            <input type="text" name="searchWord" multiple=""></input>
            <br/>
            <input type="submit">Submit</input>
          </form>
        </body>
      </html>.toString()
    )
  )

 

  class WordSearch(client: ActorRef, count: Int) extends Actor with ActorLogging {
    log.debug("Starting streaming response ...")
    import HttpHeaders.{`Cache-Control`, `Connection`}
    import spray.http.{ ChunkedMessageEnd, ChunkedResponseStart, ContentType, HttpEntity, HttpResponse, HttpResponsePart, MediaType, MediaTypes, MessageChunk }

    import spray.http.CacheDirectives.`no-cache`
    import spray.http.HttpCharsets.`UTF-8`
    import spray.http.HttpHeaders.`Cache-Control`
    //import spray.http.HttpHeaders.`Keep-Alive`
    private val `text/event-stream` = MediaTypes.register(MediaType.custom("text/event-stream"))
    
    val responseStart = HttpResponse(
        entity = HttpEntity(ContentType(`text/event-stream`, `UTF-8`), "XXX"),
        headers = List(`Cache-Control`(`no-cache`))
      )
    client ! ChunkedResponseStart(responseStart)
    Thread sleep 10
    
      
    // compose a couple of directives into one neat 
    // directive for event streams
    //respondWithHeader(`Cache-Control`(`no-cache`)) &
    //respondWithHeader(`Connection`("Keep-Alive")) &
    //respondWithMediaType(`text/event-stream`)
    // we use the successful sending of a chunk as trigger for scheduling the next chunk
    client ! ChunkedResponseStart(HttpResponse(entity = "BONG") )

    Thread sleep 100
    
    client ! ChunkedResponseStart(HttpResponse(entity = "SBONG!!!") )
    
    def receive = {
      
      case x: Http.ConnectionClosed =>
        log.info("Canceling response stream due to {} ...", x)
        context.stop(self)
    }

  }
}
