package net.sgtp.fun.dataInspector.exec

import net.sgtp.fun.dataInspector.analysisForTriplestores.endpointSelector
import net.sgtp.fun.dataInspector.analysisForTriplestores.datasourceQueryAnswererForTriplestores
import net.sgtp.fun.dataInspector.body.counters
import net.sgtp.fun.dataInspector.body.ExecutionEngine
import net.sgtp.fun.dataInspector.body.NodesMemory
import scala.concurrent.{Await, Future}
import scala.collection.parallel._
import net.sgtp.fun.dataInspector.body.DataSourceType._

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
  
  val searchTerms=args.slice(args.indexOf("-search")+1, args.size).map(x=>x.replaceAll("_", " ")).toList
  //Search in each endpoints for relevant leads
  if(ops.verbose) println("Searching each endpoint for: "+searchTerms.mkString(" "))
  
   val cyOut=new NodesMemory("resources/web/outCy.txt")
                 val availableEndpointsTypes=endpoints.map(x=>(x,ENDPOINT))

   val seeder=new ExecutionEngine(availableEndpointsTypes,searchTerms,ops,cyOut)
   seeder.exec()
  
  
  
   
      
    
  
  
}