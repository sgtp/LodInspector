package net.sgtp.fun.dataInspector.body

import net.sgtp.fun.dataInspector.io.NodesMemory
import net.sgtp.fun.dataInspector.model._
import scala.collection.parallel._

class NetworkSeeder(endpoints:List[String],searchStrings:List[String],ops:options,cyOut:NodesMemory) {
  def exec()={
  val forkJoinPool = new java.util.concurrent.ForkJoinPool(ops.parThreads)
  val parExp=endpoints.par
  parExp.tasksupport=new ForkJoinTaskSupport(forkJoinPool)
  parExp.foreach(ep=>{
    val eAnalyzer=new endpointAnalyzer(ops.verbose,ep,ops.queryTimeOut1,ops.queryTimeOut2,cyOut.counters)
    cyOut.counters.endPointOpened+=1;
      searchStrings.par.foreach(
        str=>{
            val queryToProcess=List(("value",str),("res", str),("prop",str)) // Not par here as two are very slow
            queryToProcess.foreach(q=>{
              val roughResult=eAnalyzer.retrieveRoughResults(q._1,q._2)
              if(roughResult.triples.size>0) {
                if(ops.verbose) println("Found triples "+roughResult.triples.size+" in "+roughResult.endpoint+" for "+roughResult.query)
                val matureNodes=NodeFactory.extractFromRoughResults(roughResult);
                matureNodes.foreach(mn=>{val res=cyOut.process(mn); if(res) cyOut.dump()})
                matureNodes.foreach(x=>{val pm=x.getProfiled(eAnalyzer); val res=cyOut.process(pm); if(res) cyOut.dump() })
      } 
        }
      )
     cyOut.counters.endPointTerminated+=1
   }) 
  
  
    
   }) 
  }
}