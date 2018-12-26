package net.sgtp.fun.dataInspector.exec

import net.sgtp.fun.dataInspector.io.OptionsParser
import net.sgtp.fun.dataInspector.body.endpointSelector
import net.sgtp.fun.dataInspector.body.endpointAnalyzer
import net.sgtp.fun.dataInspector.body.counters
import net.sgtp.fun.dataInspector.body.analysisWorkflow
import net.sgtp.fun.dataInspector.io.simpleCyFileOut

import scala.concurrent.{Await, Future}
import scala.collection.parallel._

//import collection.JavaConversions._

/**
 * Starts with arguments on the command line
 * Writes reults to a local file that is periodically read by a web page
 */
object cliViewOnCytoWeb extends App {
  System.out.println("Command line to web Cytoscape view 0.0.2")
  
  // Parse command line
  val ops=OptionsParser.parseCli(args) 
  if(ops.verbose) ops.print()
  
  //TODO could generalise to multiple endpoints
  val endpoints:List[String]=args.contains("-e") match {
    case true =>{
      List(args(args.indexOf("-e")+1))
    }
    case false => {
      endpointSelector.listUpInUmaka(ops.yummyScore).toList
    }
  }
  if(ops.verbose) {
    println("Endpoints (after optional Umaka expansion):")
    endpoints.foreach(println)
    println("Total no. "+endpoints.size)
    println
  }
  
  val searchTerms=args.slice(args.indexOf("-search")+1, args.size).map(x=>x.replaceAll("_", " "))
  //Search in each endpoints for relevant leads
  if(ops.verbose) println("Searching each endpoint for: "+searchTerms.mkString(" "))
  
 
  val forkJoinPool = new java.util.concurrent.ForkJoinPool(200)
  val parExp=endpoints.par
  parExp.tasksupport=new ForkJoinTaskSupport(forkJoinPool)
  
  parExp.foreach(ep=>{
      counters.endPointOpened+=1;
      searchTerms.par.foreach(
      str=>{
        
        val aWorkflow=new analysisWorkflow(ops.verbose,ep,str,ops.queryTimeOut1,ops.queryTimeOut2, new  simpleCyFileOut("resources/web/outCy.txt"))
        
    
  }
  )
  counters.endPointTerminated+=1
  })
   
      
    
  
  
}