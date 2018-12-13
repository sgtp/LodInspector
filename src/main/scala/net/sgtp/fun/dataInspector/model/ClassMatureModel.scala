package net.sgtp.fun.dataInspector.model

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
    val res=endpoint+"\tclass\t"+uri+"\t"+nodeName
    val res2=endpoint+"\tfocus\t"+uri
    if(isFocus) List(res,res2)
    else List(res)
  }
}