package net.sgtp.fun.dataInspector.analysis

import net.sgtp.fun.dataInspector.model.AbstractNode

abstract class datasourceAnalyzer {
  def retrieveRoughResults(location:String,searchString:String):List[AbstractNode]
  def countInstances(classURI:String):Int
  def getName(uri:String,nameProp:String):String
}



 