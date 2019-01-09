package net.sgtp.fun.dataInspector.modelForTriplestores

import net.sgtp.fun.dataInspector.analysisForTriplestores.datasourceQueryAnswererForTriplestores
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.collection.JavaConversions._
import net.sgtp.fun.dataInspector.model.ClassModel

object ClassModelTriplestoresImpl {
  def create(ea:datasourceQueryAnswererForTriplestores,distance:Int,canBeDeleted:Boolean,endpoint:String,nodeURI:String,nodeName:String,nodeNameProp:String):ClassModelTriplestoresImpl={
    val name=Future {nodeName}
    new ClassModelTriplestoresImpl(ea,distance,false,canBeDeleted,endpoint,nodeURI,name,nodeNameProp,"","")  
  }
  def create(ea:datasourceQueryAnswererForTriplestores,distance:Int,canBeDeleted:Boolean,endpoint:String,uri:String):ClassModelTriplestoresImpl= {
    val name=Future {ea.getName(uri,"")}
    new ClassModelTriplestoresImpl(ea,distance,false,canBeDeleted,endpoint,uri,name,"","","")
  }
}

class ClassModelTriplestoresImpl  (
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
) extends ClassModel(distanceFromUserFocus,complete,canBeDeleted,dataSource,classURI) {

  val noOfInstances= Future {
    ea.countInstances(uri)
  }
  
  
  
  
  val attributesProfileCount=""
  val attriutesProfleExamples=""
  val relations=""
  val relationsProfileCount=""
  //val focusAttr=""
  //val focusRel=""
  val delay= Duration(1,"millis")
  
  
  //Begins profiling
  
  val attributes =Future {
    println("Profiling "+uri)
    val instances=ea.getInstancesForClass(uri, 200)
    println("No of instances: "+instances.size)
    val resultModel=ea.collectStatementsForInstancesList(instances)
    println("No. of collected statements: "+resultModel.size)
    val literalPredicates=resultModel.listStatements().toList().filter(s=>s.getObject.isLiteral()).map(x=>x.getPredicate)
    val totalNoOfPredInSample=literalPredicates.size
    println("Literal stats. "+totalNoOfPredInSample)
    val frequentProperties=literalPredicates.groupBy(identity).mapValues(_.size).toList.sortBy(_._2).takeRight(5)
    frequentProperties.foreach(x=>println(x._1+" times "+x._2))
    val result=frequentProperties.map(prop=>{
      val instanceCoverage=(prop._2.toDouble/instances.size)*100
      val propCoverage=(prop._2.toDouble/totalNoOfPredInSample)*100
      val example=resultModel.listObjectsOfProperty(prop._1).next().asLiteral().getString
      (prop._1.getLocalName,instanceCoverage,propCoverage,example)
    })
    result.foreach(x=>println(x._1+" -- "+x._2+" -- "+x._3+" -- "+x._4))
    result
  }
  
  
  
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
    val res4=if(attributes.isCompleted) {
      val listofAttr=attributes.value.get.get 
      val additional=listofAttr.map(x=>dataSource+"\tattr\t"+uri+"\t"+x._1+"\t"+x._2+"\t"+x._3+"\t"+x._4+"\n")
      additional
    }
    else res3
    //test
    //println(res3)
    //test
    res4
  }

  
}