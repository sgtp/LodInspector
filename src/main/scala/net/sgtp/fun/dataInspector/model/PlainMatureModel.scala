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
  

) extends AbstractNode(distance, completed,canBeDeleted,ep,nodeId) {
   
  def getCySer:List[String]={
    val res=endpoint+"\tplain\t"+uri+"\t"+name
    val res2=endpoint+"\tfocus\t"+uri
    if(distance==0) List(res,res2)
    else List(res)
  } 
  
  def getProfiled(ea:endpointAnalyzer):AbstractNode={this}
}

////
