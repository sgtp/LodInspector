package net.sgtp.fun.dataInspector.model

import net.sgtp.fun.dataInspector.body.endpointAnalyzer


class PlainMatureModel(
   distance:Int,
   completed:Boolean,
   canBeDeleted:Boolean,
   nodeId:String,
   ep:String,
   name:String,
   nameProp:String,
   isFocus:Boolean

) extends AbstractNode(distance, completed,canBeDeleted,nodeId,ep,name,nameProp) {
   
  def getCySer:List[String]={
    val res=endpoint+"\tplain\t"+uri+"\t"+nodeName
    val res2=endpoint+"\tfocus\t"+uri
    if(isFocus) List(res,res2)
    else List(res)
  } 
  
  def getProfiled(ea:endpointAnalyzer):AbstractNode={this}
}

////
