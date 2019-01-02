package net.sgtp.fun.dataInspector.model

import java.net.URL
import net.sgtp.fun.dataInspector.analysisForTriplestores.datasourceQueryAnswererForTriplestores

class InstanceModel(
  val dist:Int,
  val compl:Boolean,
  canBeDeleted:Boolean,
  val ep:String,
  val instanceId:String,
  val instanceName:String,
  val instanceNameProp:String,
  val classIds:List[String],
) extends AbstractDataElement(dist,compl,canBeDeleted,ep,instanceId) {

  //TODO name as Future?
  def getCytoSerialization:List[String]={
    val res=classIds.map(classId=>dataSource+"\tinst\t"+uri+"\t"+classId+"\t"+instanceName)
    val res2=dataSource+"\tset\t"+uri+"\tfocus\t"+1
    if(distanceFromUserFocus==0) res ++ List(res2)
    else res
  }
  def getProfiled(ea:datasourceQueryAnswererForTriplestores)={this} //TODO
}

// http://dbpedia.org/sparql	inst	 node5	 node2	 name_node5	name_node2_class
