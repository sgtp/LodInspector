package net.sgtp.fun.server

import akka.http.scaladsl.model._
import akka.http.scaladsl.Http

object SockServer2 extends App{
   System.out.println("Starting explorer server")
   
   //val bindingFuture = Http().bindAndHandle(null, "localhost", 8080)
/*
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
 */
   
   
}