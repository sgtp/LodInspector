package net.sgtp.fun.dataInspector.io

import java.util.concurrent.ConcurrentHashMap
import net.sgtp.fun.dataInspector.model._

import java.io.File
import java.io.PrintWriter

import collection.JavaConversions._

object simpleCyFileOut {
  val nodes: ConcurrentHashMap[String,CommonMatureNode] = new ConcurrentHashMap  
  
  def process(mn:CommonMatureNode)={
    if(!nodes.containsKey(mn.uri)) {
      nodes.put(mn.uri,mn)
      dump()
    }
    else {
      val oldN=nodes.get(mn.uri)
      if(!oldN.equals(mn)) {
         nodes.put(mn.uri,mn) 
         dump()
      }
    }
  }
  
  def dump() {
    (new File("resources/web/outCy.txt")).delete()
    val writer = new PrintWriter(new File("resources/web/outCy.txt"))
    nodes.elements.foreach(el=>{
      val text=el.getCySer
      writer.write(text.mkString("\n")+"\n")
      })
    writer.close()
  }
  
}