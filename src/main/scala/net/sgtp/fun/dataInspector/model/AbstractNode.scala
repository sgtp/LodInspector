package net.sgtp.fun.dataInspector.model

import net.sgtp.fun.dataInspector.body.endpointAnalyzer
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

abstract class AbstractNode(
    val distance:Int,      //The distance from the original search
    val completed:Boolean=false,
    val canBeDeleted:Boolean,
    val endpoint:String, 
    val uri:String,        //The URI of the node
    ) {
    def getCySer:List[String]  
    //def getProfiled(ea:endpointAnalyzer):CommonMatureNode
      
   
}