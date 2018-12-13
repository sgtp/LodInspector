package net.sgtp.fun.dataInspector.model

import net.sgtp.fun.dataInspector.body.endpointAnalyzer


class PlainMatureModel(
   nodeId:String,
   ep:String,
   name:String,
   nameProp:String,
   isFocus:Boolean

) extends CommonMatureNode(nodeId,ep,name,nameProp) {
   
  def getCySer:List[String]={
    val res=endpoint+"\tplain\t"+uri+"\t"+nodeName
    val res2=endpoint+"\tfocus\t"+uri
    if(isFocus) List(res,res2)
    else List(res)
  } 
  
  def getProfiled(ea:endpointAnalyzer):CommonMatureNode={this}
}

