package net.sgtp.fun.dataInspector.body

import net.sgtp.fun.dataInspector.body.DataSourceType._
import net.sgtp.fun.dataInspector.model._
import net.sgtp.fun.dataInspector.analysisForTriplestores.datasourceSeederForTriplestores
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import net.sgtp.fun.dataInspector.analysisForTriplestores.InstanceIntraLinkerForTS

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
        val seederStrategies=dsSeeder.getFastNodeSeederStrategies
        val complementarySeederStrategies=dsSeeder.getComplementarySeederStrategies
        
        seederStrategies.foreach(ss=>{
          if(ops.verbose) println("Seeder: issuing search for \""+searchTerm+"\" on "+ds._1+" ("+ds._2+") as "+ss)
          val newNodes=dsSeeder.seedFromSearchTerm(searchTerm,ss)
          newNodes.foreach(x=>profilerWorkingMemory.addIfNew(x))
          //newNodes.foreach(x=>println("New node: "+x.uri+" distance "+x.distanceFromUserFocus)) //TEST
          val newIndirectNodes=newNodes.filter(x=>x.distanceFromUserFocus>0).filter(x=>x.isInstanceOf[ClassModel])
          //newIndirectNodes.foreach(x=>println("New indirect node: "+x.uri)) //TEST
         Future { //TODO removing futures until we implement a randomized delay or something not to overload servers
            newIndirectNodes.foreach(nc=>{
              val demoInstances=dsQueryAnswerer.getInstancesForClass(nc.uri,2)
              demoInstances.par.foreach(di=>{val res=InstanceModel.create(dsQueryAnswerer,2,true,ds._1,di,nc.uri); profilerWorkingMemory.addIfNew(res)})
            })  
          }
          println("swarm test") //TODO swarmer should start after a few classes are there. Should consider classes for distance 0 instances (or directly distance 0 instances)
          if(Feature.isSwarmable(ss)) {
            println("swarmable")
            val swarmer=new InstanceIntraLinkerForTS(dsQueryAnswerer,profilerWorkingMemory)  
            Future { //TODO removing futures until we implement a randomized delay or something not to overload servers
              swarmer.swarm()
            }
          }
          
        })
         complementarySeederStrategies.foreach(ss=>{
         //TODO copy and paste now: but same as above. Move to function and factorize
            if(ops.verbose) println("Seeder: issuing search for \""+searchTerm+"\" on "+ds._1+" ("+ds._2+") as "+ss)
          val newNodes=dsSeeder.seedFromSearchTerm(searchTerm,ss)
          newNodes.foreach(x=>profilerWorkingMemory.addIfNew(x))
          //newNodes.foreach(x=>println("New node: "+x.uri+" distance "+x.distanceFromUserFocus)) //TEST
          val newIndirectNodes=newNodes.filter(x=>x.distanceFromUserFocus>0).filter(x=>x.isInstanceOf[ClassModel])
          //newIndirectNodes.foreach(x=>println("New indirect node: "+x.uri)) //TEST
         Future { //TODO removing futures until we implement a randomized delay or something not to overload servers
            newIndirectNodes.foreach(nc=>{
              val demoInstances=dsQueryAnswerer.getInstancesForClass(nc.uri,2)
              demoInstances.par.foreach(di=>{val res=InstanceModel.create(dsQueryAnswerer,2,true,ds._1,di,nc.uri); profilerWorkingMemory.addIfNew(res)})
            })  
          }
          println("swarm test") //TODO swarmer should start after a few classes are there. Should consider classes for distance 0 instances (or directly distance 0 instances)
          if(Feature.isSwarmable(ss)) { //TODO means nothing now. Never happens
            println("swarmable")
            val swarmer=new InstanceIntraLinkerForTS(dsQueryAnswerer,profilerWorkingMemory)  
            Future { //TODO removing futures until we implement a randomized delay or something not to overload servers
              swarmer.swarm()
            }
          }
        })
        
      }) 
      
  }) 
  }
}