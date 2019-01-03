package net.sgtp.fun.dataInspector.body

import net.sgtp.fun.dataInspector.body.DataSourceType._
import net.sgtp.fun.dataInspector.model._
import net.sgtp.fun.dataInspector.analysisForTriplestores.datasourceSeederForTriplestores
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import scala.collection.parallel._
import net.sgtp.fun.dataInspector.analysisForTriplestores.datasourceQueryAnswererForTriplestores
/**
 * Implements the flow of inspection action for a set of data sources and search strings
 */
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
        val seederStrategies=dsSeeder.getSeederStrategies
        seederStrategies.foreach(ss=>{
          val newNodes=dsSeeder.seedFromSearchTerm(searchTerm,ss)
          val newIndirectNodes=newNodes.filter(x=>x.distanceFromUserFocus>0).filter(x=>x.isInstanceOf[ClassModel])
          newIndirectNodes.foreach(x=>println("New node: "+x.uri))
          //Future {
            newIndirectNodes.foreach(nc=>{
              val demoInstances=dsQueryAnswerer.getInstancesForClass(nc.uri,2)
              demoInstances.par.foreach(di=>{val res=InstanceModel.create(dsQueryAnswerer,2,true,ds._1,di,nc.uri); profilerWorkingMemory.addIfNew(res)})
            })  
          //}
        })
       
        
      }) 
  }) 
  }
}