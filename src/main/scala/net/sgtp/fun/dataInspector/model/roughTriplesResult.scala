package net.sgtp.fun.dataInspector.model

import org.apache.jena.query._;
import org.apache.jena.rdf.model._

//TODO could abstract to a more generic interface
class roughTriplesResult(
    val endpoint:String,
    val query:String,
    val locationFound:String, //TODO should be an enum
    val triples:Model
    ) {
  
}