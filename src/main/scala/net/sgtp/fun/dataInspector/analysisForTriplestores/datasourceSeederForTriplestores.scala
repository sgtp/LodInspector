package net.sgtp.fun.dataInspector.analysisForTriplestores

import net.sgtp.fun.dataInspector.analysis.datasourceSeeder
import net.sgtp.fun.dataInspector.model.AbstractDataElement
import net.sgtp.fun.dataInspector.body.options
import net.sgtp.fun.dataInspector.modelForTriplestores.ClassModelTriplestoresImpl
import net.sgtp.fun.dataInspector.model.InstanceModel
import net.sgtp.fun.dataInspector.model.PlainNodeModel
import net.sgtp.fun.dataInspector.body.Feature._
import net.sgtp.fun.dataInspector.body.NodesMemory
import org.apache.jena.rdf.model._
import collection.JavaConversions._
import net.sgtp.fun.dataInspector.modelForTriplestores.roughTriplesResult


class datasourceSeederForTriplestores(tsQA:datasourceQueryAnswererForTriplestores,opts:options,wm:NodesMemory) extends datasourceSeeder(tsQA,opts,wm) {
  val getFastNodeSeederStrategies=List(VALUE,PROPERTY)
  val getComplementarySeederStrategies=List(PROPERTY,RESOURCE)
  
  def seedFromSearchTerm(searchTerm:String,strategy:Feature,ops:options=opts):List[AbstractDataElement]={
    
    
      val roughResult=tsQA.retrieveRoughResults(strategy,searchTerm)
        if(roughResult.hasResults) {
          if(opts.verbose) println("Found  "+roughResult.resultsSize+" results ("+roughResult.resultsSizeMeasure+") in "+roughResult.dataSource+" for "+roughResult.query)
          val matureNodes=extractFromRoughResults(roughResult)
          matureNodes.foreach(x=>wm.addIfNew(x))
          matureNodes
         }
        else List[AbstractDataElement]()
     
  }  
 
  
        
   
  def extractFromRoughResults(rRes:roughTriplesResult):List[AbstractDataElement]={
    val endpoint=rRes.dataSource
    val locationFound=rRes.featureFound
    val triples=reduceTriples(rRes.triples)
    
    val resourceNodesFound=triples.listSubjects().toList
    val resourceNodesWithType=resourceNodesFound.map(sRes=>{
      if(triples.contains(sRes,helper.typeProp,helper.classRes) || triples.contains(sRes,helper.typeProp,helper.rdfClassRes)) ("class",sRes)
      else if(triples.contains(sRes,helper.typeProp,null)) ("inst",sRes)
      else ("plain",sRes)
    }).toList
    
    //Can be a class -> ClassId, ClassName
    if(locationFound.equals(VALUE)) { 
      val nodeName=rRes.query
      val nodeNameProp=""
      val res=resourceNodesWithType.flatMap(in=>{
        in._1 match {
          case "class" =>List(ClassModelTriplestoresImpl.create(tsQA,0,false,endpoint,in._2.getURI,nodeName,nodeNameProp))
          case "inst" =>{
            val classes=triples.listObjectsOfProperty(in._2, helper.typeProp).filter(x=>x.isURIResource()).map(y=>y.asResource()).toList
            val classesURIs=classes.map(x=>x.getURI)
            val head=List(InstanceModel.create(tsQA, 0, false, endpoint, in._2.getURI, nodeName, nodeNameProp, classesURIs) )
            val tail=classes.map(cl=>{ClassModelTriplestoresImpl.create(tsQA,1,false,endpoint,cl.getURI)})
            head++tail
          }
          case "plain" =>List(new PlainNodeModel(0,false,false,endpoint,in._2.getURI,nodeName,nodeNameProp))
        }
      })
      res
    }
    else {
      List[AbstractDataElement]()
    }
    
    //Can be an instance -> NodeId, NodeName, ClassId (fill with ClassName) -> Out class
    
    //Can be plain -> NodeId, NodeName
    //Can be Attr -> ClassId, Attr (fill with class name)
    //Can be a Rel -> ClassId, Rel (fill with class name)
        

    
  }     
        
        
        
        
   def reduceTriples(in:Model)={
    val statementResources=in.listSubjectsWithProperty(helper.typeProp,helper.statRes).toList
    statementResources.foreach(ss=>{
      val res=in.removeAll(ss, null, null);
    })
    //println("After stat removal")
    val objectsClasses=in.listStatements().toList.filter(xx=>xx.getPredicate.equals(helper.typeProp)).map(x=>x.getObject).filter(y=>y.isURIResource()).map(z=>z.asResource()).toSet.toList
    if(objectsClasses.size>0) {
      val sampleSources=objectsClasses.map(oRes=>in.listSubjectsWithProperty(helper.typeProp, oRes).next())  
      val res=ModelFactory.createDefaultModel().add(in.listStatements().toList.filter(x=>sampleSources.contains(x.getSubject)))
      res
    }
    else in;

  }
      
}


/*
 Overall logic to keep in mind as reference
 * 
 * class MatureResult(rRes:roughTriplesResult) {
   val aboutURI=rRes.locationFound match {
     case "value" => rRes.triples.listSubjects().next().getURI
     case "res"  => rRes.triples.listSubjects().next().getURI
     case "prop" => rRes.triples.listStatements().next().getPredicate().getURI()
   }
   val endpoint=rRes.endpoint
   val name=rRes.locationFound match {
      case "value" => {}
      
   }
   
    rRes.locationFound match {
      case "value" => {
         println("Found in value")
         if(rRes.triples.contains(null,ResourceFactory.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),ResourceFactory.createResource("http://www.w3.org/2002/07/owl#Class"))) println("=> Class")
         else if(rRes.triples.contains(null,ResourceFactory.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),ResourceFactory.createResource("http://www.w3.org/2002/07/owl#ObjectProperty"))) println("=> Relation")
         else if(rRes.triples.contains(null,ResourceFactory.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),ResourceFactory.createResource("http://www.w3.org/2002/07/owl#DatatypeProperty"))) println("=> Attribute")
         else if(rRes.triples.contains(null,ResourceFactory.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),null)) println("=> Instance")
         else println(aboutURI+"=> Unqualifed")

         //entity could be  res, attr, or prop
      }
      case "res" => {
        println("Found in res")
        //TODO this won't work, need find triples
        if(rRes.triples.contains(null,ResourceFactory.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),ResourceFactory.createResource("http://www.w3.org/2002/07/owl#Class"))) println("=> Class")
        if(rRes.triples.contains(null,ResourceFactory.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),ResourceFactory.createResource("http://www.w3.org/2002/07/owl#ObjectProperty"))) println("=> Relation")
        if(rRes.triples.contains(null,ResourceFactory.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),ResourceFactory.createResource("http://www.w3.org/2002/07/owl#DatatypeProperty"))) println("=> Attribute")
        //entity could be  res, attr, or prop
      }
      case "prop" => {
        println("Found in prop")
        //TODO this won't work, need find triples
        if(rRes.triples.contains(null,ResourceFactory.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),ResourceFactory.createResource("http://www.w3.org/2002/07/owl#ObjectProperty"))) println("=> Relation")
        if(rRes.triples.contains(null,ResourceFactory.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),ResourceFactory.createResource("http://www.w3.org/2002/07/owl#DatatypeProperty"))) println("=> Attribute") 
        //entity could be prop or attr
        
      }
    }  
 */

