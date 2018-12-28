package net.sgtp.fun

object Queries {

  def searchQuery(value:String):String={
    val res="PREFIX up:<http://purl.uniprot.org/core/> select distinct ?values where {" +
    "{ ?item ?anyProp \"" + value + "\" . ?item ?properties ?values }" +
  	"union"+
  	"{?g a ?t . ?g ?p \""+value+"\" . ?prot up:encodedBy ?g . ?prot up:recommendedName|up:alternativeName ?name . ?name ?properties ?values }"+
    "values ?properties { <http://www.w3.org/2000/01/rdf-schema#label>  <http://www.w3.org/2004/02/skos/core#prefLabel> <http://www.w3.org/2004/02/skos/core#altLabel> <http://purl.uniprot.org/core/mnemonic>	  <http://purl.uniprot.org/core/fullName> <http://purl.uniprot.org/core/shortName> }}"        
    res
  }
  
  def searchViaSimpleLiteral(value:String)={
    val res="construct{?s ?p ?o} where { ?s ?p2 \""+value+"\" . ?s ?p ?o }"
    res 
  }
  
  val totalEntities="select (count(distinct(?x)) as ?nrow) where {?x ?p ?o}"
  val typedEntities="select (count(distinct(?x)) as ?nrow) where {?x a ?o}"
  val literalProperties="select distinct ?p where {?s ?p ?o . filter (isLiteral(?o)) }"
  def distProperties(value:String)=s"select (count(distinct(?o)) as ?co) where {?s <${value}> ?o}"
  def allProperties(value:String)=s"select (count(?o) as ?co) where {?s <${value}> ?o}"
}
  
  
  