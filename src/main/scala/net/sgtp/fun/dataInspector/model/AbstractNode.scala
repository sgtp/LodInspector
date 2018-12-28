package net.sgtp.fun.dataInspector.model

import net.sgtp.fun.dataInspector.body.endpointAnalyzer
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

abstract class AbstractNode(
    val distance:Int,      //The distance from the original search
    val completed:Boolean=false,
    val canBeDeleted:Boolean,
    val uri:String,        //The URI of the node
    val endpoint:String, 
    name:String,
    val nameProp:String="") {
      val nodeName=Future {
        name
      }
      def getCySer:List[String]  
    //def getProfiled(ea:endpointAnalyzer):CommonMatureNode
}