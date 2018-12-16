package net.sgtp.fun.dataInspector.exec

import net.sgtp.fun.dataInspector.io.clParser
import net.sgtp.fun.dataInspector.body.endpointSelector
import net.sgtp.fun.dataInspector.body.endpointAnalyzer
import net.sgtp.fun.dataInspector.body.counters
import net.sgtp.fun.dataInspector.body.analysisWorkflow


import scala.concurrent.{Await, Future}
import scala.collection.parallel._

//import collection.JavaConversions._

/**
 * Starts with arguments on the command line
 * Writes reults to a local file that is periodically read by a web page
 */
object cliViewOnCytoWeb extends App {
  System.out.println("Command line to web Cytoscape view 0.1")
  
  
  // Parse command line
  val initialOptions=clParser.parseCli(args)        // Note: this is immutable, some options (e.g.: endpoints) may change at runtime.
  if(initialOptions.verbose) {
    initialOptions.print() 
    println
  }
  
  // Find the list of endpoints of interest
  if(initialOptions.verbose) {
    print("Endpoints: ")
     if(initialOptions.searchStep) println("Looking for YummyData (score > "+initialOptions.yummyScore+")")
  }
  val availableEndpoints=if(initialOptions.searchStep) endpointSelector.listUpInUmaka(initialOptions.yummyScore)
  else initialOptions.endpoints
  if(initialOptions.verbose) {
    println("Endpoints (after optional Umaka expansion):")
    availableEndpoints.foreach(println)
    println("Total no. "+availableEndpoints.size)
    println
  }
  
  //Search in each endpoints for relevant leads
  if(initialOptions.verbose) println("Searching each endpoint for: "+initialOptions.searchStrings.mkString(" "))
  
  /*
   * scala> val forkJoinPool = new java.util.concurrent.ForkJoinPool(2)
forkJoinPool: java.util.concurrent.ForkJoinPool = java.util.concurrent.ForkJoinPool@6436e181[Running, parallelism = 2, size = 0, active = 0, running = 0, steals = 0, tasks = 0, submissions = 0]

scala> pc.tasksupport = new ForkJoinTaskSupport(forkJoinPool)
pc.tasksupport: scala.collection.parallel.TaskSupport = scala.collection.parallel.ForkJoinTaskSupport@4a5d484a

scala> pc map { _ + 1 }
res0: scala.collection.parallel.mutable.ParArray[Int] = ParArray(2, 3, 4)
   */
  val forkJoinPool = new java.util.concurrent.ForkJoinPool(200)
  val parExp=availableEndpoints.par
  parExp.tasksupport=new ForkJoinTaskSupport(forkJoinPool)
  
  parExp.foreach(ep=>{
      counters.endPointOpened+=1;
      initialOptions.searchStrings.par.foreach(
      str=>{
        
        val aWorkflow=new analysisWorkflow(initialOptions.verbose,ep,str,initialOptions.queryTimeOut1,initialOptions.queryTimeOut2)
        
    
  }
  )
  counters.endPointTerminated+=1
  })
   
      
    
  
  
}