package net.sgtp.fun.dataInspector.model

import net.sgtp.fun.dataInspector.analysisForTriplestores.datasourceQueryAnswererForTriplestores
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

abstract class AbstractDataElement(
    val distanceFromUserFocus:Int,      //The distance from the original search
    val completed:Boolean=false,
    val canBeDeleted:Boolean,
    val dataSource:String, 
    val uri:String,        //The URI of the node
    ) {
    def getCytoSerialization:List[String]  
    //def getProfiled(ea:endpointAnalyzer):CommonMatureNode
      
   
}