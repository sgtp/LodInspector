package net.sgtp.fun.dataInspector.model

import org.apache.jena.rdf.model._

object NodeFactory {
  def typeProp=ResourceFactory.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")
  def classRes=ResourceFactory.createResource("http://www.w3.org/2002/07/owl#Class")
  
  def extractFromRoughResults(rRes:roughTriplesResult):List[CommonMatureNode]={
    //Common preps
    val endpoint=rRes.endpoint
    val aboutURI=rRes.locationFound match {
      case "value" => rRes.triples.listSubjects().next().getURI
      case "res"  => rRes.triples.listSubjects().next().getURI
      case "prop" => rRes.triples.listStatements().next().getPredicate().getURI()
    } 
    
    //Can be a class -> ClassId, ClassName
    if((rRes.locationFound.equals("value") || rRes.locationFound.equals("res")) && rRes.triples.contains(null,typeProp,classRes)) {
      println(aboutURI+" =>Class")  
      val className=rRes.query
      val classNameProp=""
      List(new ClassMatureModel(endpoint,aboutURI,className,classNameProp,"","",true))
    }
    else {
      List[CommonMatureNode]()
    }
    //Can be an instance -> NodeId, NodeName, ClassId (fill with ClassName) -> Out class
    
    //Can be plain -> NodeId, NodeName
    //Can be Attr -> ClassId, Attr (fill with class name)
    //Can be a Rel -> ClassId, Rel (fill with class name)
        

    
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