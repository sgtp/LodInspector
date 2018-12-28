package net.sgtp.fun.dataInspector.model

import net.sgtp.fun.dataInspector.body.endpointAnalyzer
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object ClassMatureModel {
  def create(ea:endpointAnalyzer,distance:Int,canBeDeleted:Boolean,endpoint:String,nodeURI:String,nodeName:String,nodeNameProp:String)={
    new ClassMatureModel(ea,distance,false,canBeDeleted,endpoint,nodeURI,nodeName,nodeNameProp,"","",true)  
  }
}

class ClassMatureModel  (
  val ea:endpointAnalyzer,
  distance:Int,
  complete:Boolean,
  canBeDeleted:Boolean,
  endpoint:String,
  classURI:String,
  className:String,
  classNameProp:String,
  val focusRes:String="",
  val focusAttr:String="",
  val isFocus:Boolean=false
) extends AbstractNode(distance,complete,canBeDeleted,classURI,endpoint,className,classNameProp) {

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
  
  
  def getCySer:List[String]={
    val preNodeDisplay=if(!nodeName.isCompleted) uri
    else nodeName.value.get.get
    
    val nodeDisplay=if(noOfInstances.isCompleted) preNodeDisplay+"_("+noOfInstances.value.get.get+")"
    
    val res1=List(endpoint+"\tclass\t"+uri+"\t"+nodeDisplay)
    val res2=if(distance==0) res1++List(endpoint+"\tset\t"+uri+"\tfocus\t"+1)
    else res1
    val res3=if(noOfInstances.isCompleted) res2++List(endpoint+"\tset\t"+uri+"\tsize\t"+noOfInstances.value.get.get)
    else res2
    res3
  }

  
}