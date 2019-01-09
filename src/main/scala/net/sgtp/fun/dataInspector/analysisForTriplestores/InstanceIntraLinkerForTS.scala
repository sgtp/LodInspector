package net.sgtp.fun.dataInspector.analysisForTriplestores

import net.sgtp.fun.dataInspector.analysis.instanceIntraLinker
import net.sgtp.fun.dataInspector.body.NodesMemory
import net.sgtp.fun.dataInspector.model.AbstractDataElement
import java.util.concurrent.ConcurrentSkipListSet
import scala.collection.JavaConversions._
import scala.util.Random

//TODO something should be abstract in the abstract parent
class InstanceIntraLinkerForTS(val qa:datasourceQueryAnswererForTriplestores,val wm:NodesMemory) extends instanceIntraLinker {
  def swarm()={
    var seenNodes=new ConcurrentSkipListSet[String]()
    var pendingEdges=new ConcurrentSkipListSet[(String,String,String,String)]() //TODO extremely bad! We should index via map. But first let's see if the overall idea yields interesting insights
    
    val startingClasses=wm.nodes.values().toList.filter(_.distanceFromUserFocus<2).map(_.uri)
    swarmStep(startingClasses)
    
    def swarmStep(classes:List[String]):Unit={
     classes.par.foreach(cls=>{
      val instances=qa.getInstancesForClass(cls, 100) //TODO should do better sampling
      val instancesToFollow=Random.shuffle(instances).take(10)
      //val statsForInstances=qa.collectStatementsForInstancesList(instances)
      //val objectStatsForInstances=statsForInstances.listStatements().toList.filter(_.getObject.isURIResource()).toList //TODO Note that we filter out going through blank nodes here.... not so ontology friendly!
      //val targets=objectStatsForInstances.map(x=>(x.getPredicate,x.getObject)).filter(_._2.isURIResource()).map(x=>(x._1,x._2.asResource().getURI)).toSet.take(5)
      val nextStepData=qa.getRelationsAndTargetClassesForInstanceList(instancesToFollow)
      val relationsStats=nextStepData.listStatements().filter(x=>instancesToFollow.contains(x.getSubject.asResource.getURI)).filterNot(x=>x.getPredicate.equals(helper.typeProp))
      val relationsAndClasses=relationsStats.map(x=>(x,nextStepData.listObjectsOfProperty(x.getObject.asResource(), helper.typeProp).toList))
      val relationsStruct=relationsAndClasses.flatMap(x=>{x._2.map(y=>(x._1.getSubject.getURI,x._1.getPredicate.getURI,x._1.getObject.asResource.getURI,y.asResource.getURI))})
      val filteredRelationsStruct=relationsStruct.filterNot(x=>x._4.equals(helper.classRes.getURI))
      filteredRelationsStruct.foreach(x=>println(x._1+" -- "+x._2+" -- "+x._3+" -- "+x._4))
      filteredRelationsStruct.foreach(ns=>{
        if(wm.nodes.containsKey(ns._4)) {
          //We reached a node that we know
        }
        else {
          seenNodes.add(ns._4)
          pendingEdges.add(ns)
          println("Next iteration")
          val newInstances=filteredRelationsStruct.map(x=>x._3).toList
          swarmStep(newInstances)
          
        }  
      })
     
    })  
    }
    /*
     * DS: list of nodes that have been seen
     * EP: list of pending edges
    1) --retrieve starting classes from wm
    2) --foreach starting class/par {
        -- 2.1) take X entities
        -- 2.2) extract statements
        --- 2.3) sample (max)Y targets
        -- 2.4) get target classes
        2.5) is target class known?
        		2.5.1) YES => is relation from source to target known?
        				2.5.1.2) => YES do nothing
        				2.5.1.2) => NO add all relation chain in pending edges to WM
        		2.5.2) NO => add node to WM. Add relation to pending edges
    } --
     
     */
  }
  

}