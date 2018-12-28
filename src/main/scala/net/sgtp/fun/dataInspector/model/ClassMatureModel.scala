package net.sgtp.fun.dataInspector.model

import net.sgtp.fun.dataInspector.body.endpointAnalyzer
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object ClassMatureModel {
  def create(ea:endpointAnalyzer,distance:Int,canBeDeleted:Boolean,endpoint:String,nodeURI:String,nodeName:String,nodeNameProp:String):ClassMatureModel={
    val name=Future {nodeName}
    new ClassMatureModel(ea,distance,false,canBeDeleted,endpoint,nodeURI,name,nodeNameProp,"","")  
  }
  def create(ea:endpointAnalyzer,distance:Int,canBeDeleted:Boolean,endpoint:String,uri:String):ClassMatureModel= {
    val name=Future {ea.getName(uri,"")}
    new ClassMatureModel(ea,distance,false,canBeDeleted,endpoint,uri,name,"","","")
  }
}

class ClassMatureModel  (
  val ea:endpointAnalyzer,
  distance:Int,
  complete:Boolean,
  canBeDeleted:Boolean,
  endpoint:String,
  classURI:String,
  nodeName:Future[String],
  classNameProp:String,
  val focusRes:String="",
  val focusAttr:String="",
) extends AbstractNode(distance,complete,canBeDeleted,endpoint,classURI) {

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
  
  def getCySer:List[String]={
    val preNodeDisplay1=if(nodeName.isCompleted) nodeName.value.get.get
    else uri
    val preNodeDisplay=if(preNodeDisplay1.equals("")) uri
    else preNodeDisplay1
    val nodeDisplay=if(noOfInstances.isCompleted) preNodeDisplay+"_("+noOfInstances.value.get.get+")"
    else preNodeDisplay
    
    val res1=List(endpoint+"\tclass\t"+uri+"\t"+nodeDisplay)
    val res2=if(distance==0) res1++List(endpoint+"\tset\t"+uri+"\tfocus\t"+1)
    else res1
    val res3=if(noOfInstances.isCompleted) res2++List(endpoint+"\tset\t"+uri+"\tsize\t"+noOfInstances.value.get.get)
    else res2
    //test
    println(res3)
    //test
    res3
  }

  
}