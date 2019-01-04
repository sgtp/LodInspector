package net.sgtp.fun.dataInspector.model

import net.sgtp.fun.dataInspector.analysisForTriplestores.datasourceQueryAnswererForTriplestores
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object ClassModel {
  def create(ea:datasourceQueryAnswererForTriplestores,distance:Int,canBeDeleted:Boolean,endpoint:String,nodeURI:String,nodeName:String,nodeNameProp:String):ClassModel={
    val name=Future {nodeName}
    new ClassModel(ea,distance,false,canBeDeleted,endpoint,nodeURI,name,nodeNameProp,"","")  
  }
  def create(ea:datasourceQueryAnswererForTriplestores,distance:Int,canBeDeleted:Boolean,endpoint:String,uri:String):ClassModel= {
    val name=Future {ea.getName(uri,"")}
    new ClassModel(ea,distance,false,canBeDeleted,endpoint,uri,name,"","","")
  }
}

class ClassModel  (
  val ea:datasourceQueryAnswererForTriplestores,
  distanceFromUserFocus:Int,
  complete:Boolean,
  canBeDeleted:Boolean,
  dataSource:String,
  classURI:String,
  nodeName:Future[String],
  classNameProp:String,
  val focusRes:String="",
  val focusAttr:String="",
) extends AbstractDataElement(distanceFromUserFocus,complete,canBeDeleted,dataSource,classURI) {

  val noOfInstances= Future {
    ea.countInstances(uri)
  }
  
  val sampleInstances=""
  
  val attributes=""
  val attributesProfileCount=""
  val attriutesProfleExamples=""
  val relations=""
  val relationsProfileCount=""
  //val focusAttr=""
  //val focusRel=""
  val delay= Duration(1,"millis")
  
  def getCytoSerialization:List[String]={
    val preNodeDisplay1=if(nodeName.isCompleted) nodeName.value.get.get
    else uri
    val preNodeDisplay=if(preNodeDisplay1.equals("")) uri
    else preNodeDisplay1
    val nodeDisplay=if(noOfInstances.isCompleted) preNodeDisplay+"_("+noOfInstances.value.get.get+")"
    else preNodeDisplay
    
    val res1=List(dataSource+"\tclass\t"+uri+"\t"+nodeDisplay)
    val res2=if(distanceFromUserFocus==0) res1++List(dataSource+"\tset\t"+uri+"\tfocus\t"+1)
    else res1
    //println("Distance from User focus in class "+uri+" = "+distanceFromUserFocus) //TEST
    val res3=if(noOfInstances.isCompleted) res2++List(dataSource+"\tset\t"+uri+"\tsize\t"+noOfInstances.value.get.get)
    else res2
    //test
    //println(res3)
    //test
    res3
  }

  
}