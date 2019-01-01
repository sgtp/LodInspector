package net.sgtp.fun.dataInspector.model

import java.net.URL
import net.sgtp.fun.dataInspector.analysisForTriplestores.endpointAnalyzer

class InstanceModel(
  val dist:Int,
  val compl:Boolean,
  canBeDeleted:Boolean,
  val ep:String,
  val instanceId:String,
  val instanceName:String,
  val instanceNameProp:String,
  val classIds:List[String],
) extends AbstractNode(dist,compl,canBeDeleted,ep,instanceId) {

  //TODO name as Future?
  def getCySer:List[String]={
    val res=classIds.map(classId=>endpoint+"\tinst\t"+uri+"\t"+classId+"\t"+instanceName)
    val res2=endpoint+"\tset\t"+uri+"\tfocus\t"+1
    if(distance==0) res ++ List(res2)
    else res
  }
  def getProfiled(ea:endpointAnalyzer)={this} //TODO
}

// http://dbpedia.org/sparql	inst	 node5	 node2	 name_node5	name_node2_class
