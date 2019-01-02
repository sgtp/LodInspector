package net.sgtp.fun.dataInspector.model

import net.sgtp.fun.dataInspector.analysisForTriplestores.datasourceQueryAnswererForTriplestores


class PlainNodeModel(
   distanceFromUserFocus:Int,
   completed:Boolean,
   canBeDeleted:Boolean,
   nodeId:String,
   ep:String,
   name:String,
   nameProp:String,
  

) extends AbstractDataElement(distanceFromUserFocus, completed,canBeDeleted,ep,nodeId) {
   
  def getCytoSerialization:List[String]={
    val res=dataSource+"\tplain\t"+uri+"\t"+name
    val res2=dataSource+"\tfocus\t"+uri
    if(distanceFromUserFocus==0) List(res,res2)
    else List(res)
  } 
  
  def getProfiled(ea:datasourceQueryAnswererForTriplestores):AbstractDataElement={this}
}

////
