package net.sgtp.fun.dataInspector.model

abstract class CommonMatureNode(
    val uri:String,
    val endpoint:String, 
    val nodeName:String,
    val nameProp:String="") {
    
    def getCySer:List[String]  
}