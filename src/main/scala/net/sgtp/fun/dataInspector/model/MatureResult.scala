package net.sgtp.fun.dataInspector.model
import org.apache.jena.rdf.model._

class MatureResult(rRes:roughTriplesResult) {
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
  
}