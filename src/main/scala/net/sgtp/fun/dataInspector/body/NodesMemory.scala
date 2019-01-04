package net.sgtp.fun.dataInspector.body

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentSkipListSet
import net.sgtp.fun.dataInspector.model._
import java.io.File
import java.io.PrintWriter
import collection.JavaConversions._
/**
 * Simple class based on a synchronized collection.
 * Receives entities to serialize and, if something new is provided, serialize the whole corpus.
 * 
 * 
 */
class NodesMemory(whereToWrite:String) {
  val counters=new counters()
  val nodes: ConcurrentHashMap[String,AbstractDataElement] = new ConcurrentHashMap  
  //val knownURIs: ConcurrentSkipListSet[String]=new ConcurrentSkipListSet
  //println("Simple writer to: "+whereToWrite)
  dump()
  
  def addIfNew(mn:AbstractDataElement):Boolean={
    if(!nodes.containsKey(mn.uri)) {
      nodes.put(mn.uri,mn)
      true
    }
    else {
      //println("Not adding "+mn.uri+" as this alredy existed")
      //val oldNode=nodes.get(mn.uri)
      //println("Kept: "+nodes.get(mn.uri).distanceFromUserFocus)
      false
    }
    
  }
  
  def addAndUpdate(mn:AbstractDataElement):Boolean={
    if(!nodes.containsKey(mn.uri)) {
      nodes.put(mn.uri,mn)
      true
    }
    else {
      val oldN=nodes.get(mn.uri)
      if(!oldN.equals(mn)) {
         nodes.put(mn.uri,mn) 
         true
      }
      else false
    }
  }
    
  def dump() {
    //TODO use the strihg from below and write to file
    (new File(whereToWrite)).delete()
    val writer = new PrintWriter(new File(whereToWrite))  
    writer.write("#\teo\t"+counters.endPointOpened+"\n")
    writer.write("#\tec\t"+counters.endPointTerminated+"\n")
    writer.write("#\tnq\t"+counters.queriesIssued+"\n")
    writer.write("#\tnqr\t"+counters.queriesWithResults+"\n")
    writer.write("#\tnqn\t"+counters.queriesNoResult+"\n")
    writer.write("#\tnqf\t"+counters.queriesFailed+"\n")
    nodes.elements.foreach(el=>{
      val text=el.getCytoSerialization
      writer.write(text.mkString("\n")+"\n")
      })
    writer.close()
    //println(dumpToString)
  }
  
  def dumpToString():String ={
    val resHeader="#\teo\t"+counters.endPointOpened+"\n"+
                  "#\tec\t"+counters.endPointTerminated+"\n"+
                  "#\tnq\t"+counters.queriesIssued+"\n"+
                  "#\tnqr\t"+counters.queriesWithResults+"\n"+
                  "#\tnqn\t"+counters.queriesNoResult+"\n"+
                  "#\tnqf\t"+counters.queriesFailed+"\n"
                  
     val resBody=nodes.elements.map(el=>el.getCytoSerialization.mkString("\n")).mkString("\n")
     resHeader+resBody
  }
  //resources/web/outCy.txt
  def isKnown(uri:String) = nodes.containsKey(uri)
}