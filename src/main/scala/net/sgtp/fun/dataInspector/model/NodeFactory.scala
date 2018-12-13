package net.sgtp.fun.dataInspector.model

import org.apache.jena.rdf.model._
import collection.JavaConversions._

object NodeFactory {
  def typeProp=ResourceFactory.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")
  def classRes=ResourceFactory.createResource("http://www.w3.org/2002/07/owl#Class")
  def rdfClassRes=ResourceFactory.createResource("http://www.w3.org/2000/01/rdf-schema#Class")
  def statRes=ResourceFactory.createResource("http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement")
  
  def extractFromRoughResults(rRes:roughTriplesResult):List[CommonMatureNode]={
    //Common preps
    val endpoint=rRes.endpoint
    val locationFound=rRes.locationFound
    val triples=reduceTriples(rRes.triples)
    
    val resourceNodesFound=triples.listSubjects().toList
    val resourceNodesWithType=resourceNodesFound.map(sRes=>{
      if(triples.contains(sRes,typeProp,classRes) || triples.contains(sRes,typeProp,rdfClassRes)) ("class",sRes)
      else if(triples.contains(sRes,typeProp,null)) ("inst",sRes)
      else ("plain",sRes)
    }).toList
    
    
    
    //Can be a class -> ClassId, ClassName
    if(locationFound.equals("value")) { 
      val nodeName=rRes.query
      val nodeNameProp=""
      val res=resourceNodesWithType.flatMap(in=>{
        in._1 match {
          case "class" =>List(new ClassMatureModel(endpoint,in._2.getURI,nodeName,nodeNameProp,"","",true))
          case "inst" =>{
            val classes=triples.listObjectsOfProperty(in._2, typeProp).filter(x=>x.isURIResource()).map(y=>y.asResource()).toList
            val classesURIs=classes.map(x=>x.getURI)
            val head=List(new InstMatureModel(endpoint,in._2.getURI,nodeName,nodeNameProp,classesURIs,true))
            val tail=classes.map(cl=>{new ClassMatureModel(endpoint,cl.getURI,"","","","",true)})
            head++tail
          }
          case "plain" =>List(new PlainMatureModel(endpoint,in._2.getURI,nodeName,nodeNameProp,true))
        }
      })
      res
    }
    else {
      List[CommonMatureNode]()
    }
    //Can be an instance -> NodeId, NodeName, ClassId (fill with ClassName) -> Out class
    
    //Can be plain -> NodeId, NodeName
    //Can be Attr -> ClassId, Attr (fill with class name)
    //Can be a Rel -> ClassId, Rel (fill with class name)
        

    
  }
  
  
  def reduceTriples(in:Model)={
    val statementResources=in.listSubjectsWithProperty(typeProp,statRes).toList
    statementResources.foreach(ss=>{
      val res=in.removeAll(ss, null, null);
    })
    //println("After stat removal")
    val objectsClasses=in.listStatements().toList.filter(xx=>xx.getPredicate.equals(typeProp)).map(x=>x.getObject).filter(y=>y.isURIResource()).map(z=>z.asResource()).toSet.toList
    if(objectsClasses.size>0) {
      val sampleSources=objectsClasses.map(oRes=>in.listSubjectsWithProperty(typeProp, oRes).next())  
      val res=ModelFactory.createDefaultModel().add(in.listStatements().toList.filter(x=>sampleSources.contains(x.getSubject)))
      res
    }
    else in;

  }
}


 /*
   
   val name=rRes.locationFound match {
      case "value" => {}
      
   }
   
    rRes.locationFound match {
      case "value" => {
         println("Found in value")
         else if(rRes.triples.contains(null,ResourceFactory.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),ResourceFactory.createResource("http://www.w3.org/2002/07/owl#ObjectProperty"))) println("=> Relation")
         else if(rRes.triples.contains(null,ResourceFactory.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),ResourceFactory.createResource("http://www.w3.org/2002/07/owl#DatatypeProperty"))) println("=> Attribute")
         else if(rRes.triples.contains(null,ResourceFactory.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),null)) println("=> Instance")
         else println(aboutURI+"=> Unqualifed")

         //entity could be  res, attr, or prop
      }
      case "res" => {
        println("Found in res")
        //TODO this won't work, need find triples
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
*/