package net.sgtp.fun.dataInspector.body

import net.sgtp.fun.dataInspector.body.DataSourceType._
import net.sgtp.fun.dataInspector.model._
import net.sgtp.fun.dataInspector.analysisForTriplestores.datasourceSeederForTriplestores

import scala.collection.parallel._
import net.sgtp.fun.dataInspector.analysisForTriplestores.datasourceQueryAnswererForTriplestores

class ExecutionEngine(datasources:List[(String,DataSourceType)],searchStrings:List[String],ops:options,profilerWorkingMemory:NodesMemory) {
  def exec()={
    val forkJoinPool = new java.util.concurrent.ForkJoinPool(ops.parThreads)
    val parExp=datasources.par
    parExp.tasksupport=new ForkJoinTaskSupport(forkJoinPool)
    parExp.foreach(ds=>{
      val dsQueryAnswerer=ds._2 match {
        case ENDPOINT => new datasourceQueryAnswererForTriplestores(ops.verbose,ds._1,ops.queryTimeOut1,ops.queryTimeOut2,profilerWorkingMemory.counters)
      }
      profilerWorkingMemory.counters.endPointOpened+=1;
      val parSearchStrings=searchStrings.par
      val dsSeeder=ds._2 match {
        case ENDPOINT => new datasourceSeederForTriplestores(dsQueryAnswerer,ops,profilerWorkingMemory)
      }  
    
      //TODO rewrite below to get somre results out
      parSearchStrings.foreach(searchTerm=>{
        val nodes=dsSeeder.seedFromSearchTerm(searchTerm)
        
      }) 
  }) 
  }
}