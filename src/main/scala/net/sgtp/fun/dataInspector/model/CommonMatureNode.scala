package net.sgtp.fun.dataInspector.model

import net.sgtp.fun.dataInspector.body.endpointAnalyzer


abstract class CommonMatureNode(
    val uri:String,
    val endpoint:String, 
    val nodeName:String,
    val nameProp:String="") {
    
    def getCySer:List[String]  
    def getProfiled(ea:endpointAnalyzer):CommonMatureNode
}