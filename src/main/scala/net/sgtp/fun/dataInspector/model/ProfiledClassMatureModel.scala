package net.sgtp.fun.dataInspector.model

//TODO check inheritance and constructors!!!
class ProfiledClassMatureModel(
  val ep2:String,
  val classId2:String,
  val className2:String,
  val classNameProp2:String,
  val focusRes2:String="",
  val focusAttr2:String="",
  val isFocus2:Boolean=false,
  val size:Int=0,
) extends ClassMatureModel(ep2,classId2,className2,classNameProp2,focusRes2,focusAttr2,isFocus2){

  override def getCySer:List[String]={
    //TODO to refactor with proper inheritance
    val nodeDisplay1=if(nodeName.equals("")) uri
    else nodeName
    val nodeDisplay2=if(size>0) nodeDisplay1+"_("+size+")"
    else nodeDisplay1
    val res=endpoint+"\tclass\t"+uri+"\t"+nodeDisplay2
    val res2=endpoint+"\tset\t"+uri+"\tsize\t"+size
    val res3=endpoint+"\tset\t"+uri+"\tfocus\t"+1
    if(isFocus) List(res,res2,res3)
    else List(res,res2)
    
  }

}