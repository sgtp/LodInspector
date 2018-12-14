package net.sgtp.fun.dataInspector.model

import net.sgtp.fun.dataInspector.body.endpointAnalyzer


class ClassMatureModel  (
  val ep:String,
  val classId:String,
  val className:String,
  val classNameProp:String,
  val focusRes:String="",
  val focusAttr:String="",
  val isFocus:Boolean=false
) extends CommonMatureNode(classId,ep,className,classNameProp) {

  
  val sampleInstances=""
  val noOfInstances=""
  val attributes=""
  val attributesProfileCount=""
  val attriutesProfleExamples=""
  val relations=""
  val relationsProfileCount=""
  //val focusAttr=""
  //val focusRel=""
  
  
  def getCySer:List[String]={
    val nodeDisplay=if(nodeName.equals("")) uri
    else nodeName
    val res=endpoint+"\tclass\t"+uri+"\t"+nodeDisplay
    val res2=endpoint+"\tset\t"+uri+"\tfocus\t"+1
    if(isFocus) List(res,res2)
    else List(res)
  }
  
  def getProfiled(ea:endpointAnalyzer)={
    println("PROFILING: "+uri)
    val nOfNodes=ea.countInstances(uri)
    new ProfiledClassMatureModel(ep,uri,nodeName,classNameProp,focusRes,focusAttr,isFocus,nOfNodes);
  }
}