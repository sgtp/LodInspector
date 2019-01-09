package net.sgtp.fun.dataInspector.model

import java.net.URL
import net.sgtp.fun.dataInspector.analysisForTriplestores.datasourceQueryAnswererForTriplestores
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object InstanceModel {
  def create(
      ea:datasourceQueryAnswererForTriplestores,
      distance:Int,
      canBeDeleted:Boolean,
      endpoint:String,
      nodeURI:String,
      nodeName:String,
      nodeNameProp:String,
      classIds:List[String]):InstanceModel={
        val name=Future {nodeName}
        //println("Instance "+nodeURI+" directly created with distance "+distance)
        new InstanceModel(distance,false,canBeDeleted,endpoint,nodeURI,name,nodeNameProp,classIds)  
  }
  def create(
      ea:datasourceQueryAnswererForTriplestores,
      distance:Int,
      canBeDeleted:Boolean,
      endpoint:String,
      nodeURI:String,
      classId:String):InstanceModel= {
        val name=Future {ea.getName(nodeURI,"")}
        new InstanceModel(distance,false,canBeDeleted,endpoint,nodeURI,name,"",List(classId))
  }
  
    //create(0,false,false,endpoint,in._2.getURI,nodeName,nodeNameProp,classesURIs)

  
}
/**
 *  val distanceFromUserFocus:Int,      //The distance from the original search
    val completed:Boolean=false,
    val canBeDeleted:Boolean,
    val dataSource:String, 
    val uri:String,
 */

class InstanceModel(
  val dist:Int,
  val compl:Boolean,
  canBeDeleted:Boolean,
  val ep:String,
  val instanceId:String,
  val instanceName:Future[String],
  val instanceNameProp:String,
  val classIds:List[String],
) extends AbstractDataElement(dist,compl,canBeDeleted,ep,instanceId) {

  
  //TODO name as Future?
  def getCytoSerialization:List[String]={
    val nameString=if(instanceName.isCompleted) instanceName.value.get.get
    else uri
    //println("Distance from User focus in instance "+uri+" = "+distanceFromUserFocus) //TEST
    val res=classIds.map(classId=>dataSource+"\tinst\t"+uri+"\t"+classId+"\t"+nameString)
    val res2=dataSource+"\tset\t"+uri+"\tfocus\t"+1
    if(distanceFromUserFocus==0) res ++ List(res2)
    else res
  }
  def getProfiled(ea:datasourceQueryAnswererForTriplestores)={this} //TODO
}

// http://dbpedia.org/sparql	inst	 node5	 node2	 name_node5	name_node2_class
