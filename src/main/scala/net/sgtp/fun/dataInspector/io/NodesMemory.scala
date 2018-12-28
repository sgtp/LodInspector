package net.sgtp.fun.dataInspector.io

import java.util.concurrent.ConcurrentHashMap
import net.sgtp.fun.dataInspector.model._
import net.sgtp.fun.dataInspector.body.counters
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
  val nodes: ConcurrentHashMap[String,AbstractNode] = new ConcurrentHashMap  
  //println("Simple writer to: "+whereToWrite)
  dump()
  def process(mn:AbstractNode):Boolean={
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
      val text=el.getCySer
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
                  
     val resBody=nodes.elements.map(el=>el.getCySer.mkString("\n")).mkString("\n")
     resHeader+resBody
  }
  //resources/web/outCy.txt
  
}