package net.sgtp.fun.dataInspector.analysisForTriplestores

object queries {
  //Simplified query (some endpoints having issues with the longer one
  
  /** Simplified variant
  def searchForValueWithLiteral(value:String)="construct{?s ?p ?o} where { { ?s ?p2 \""+value+"\" . ?s ?p ?o }"
  */             
 
  
  def searchForValueWithLiteral(value:String)="construct{?s ?p ?o} where { { ?s ?p2 \""+value+"\" . ?s ?p ?o }"+
                    "UNION { ?s ?p2 \""+value+"\"^^<http://www.w3.org/2001/XMLSchema#string> . ?s ?p ?o }" +
                    "UNION { ?s ?p2 \""+value+"\"@en . ?s ?p ?o }}"
   
  def searchForPropertiesForLiteral(value:String)="construct {?s ?p ?o} where { ?s ?p ?o . FILTER (strends (str(?s), \""+value+"\"))}"
  def searchForResourcesForLiteral(value:String)="construct {?s ?p ?o} where { ?s ?p ?o . FILTER (strends (str(?p), \""+value+"\"))}"

  def countInstancesPerClass(value:String)="select (count(distinct(?s)) as ?res) where {?s a <"+value+">}"
  
  def getValueForProp(uri:String,prop:String)="select ?res where {<"+uri+"> <http://www.w3.org/2000/01/rdf-schema#label> ?res}"
  def getNoInstancesForClass(uri:String,number:Int)="select ?res where {?res a <"+uri+">} limit "+number
  
  def getAllStatementsForListOfIds(ids:List[String]):String={
    val asURIs=ids.map(x=>"<"+x+">")
    val idString=asURIs.mkString(" ")
    val res="construct {?s ?p ?o} where {?s ?p ?o . values ?s {"+idString+"}}"
    res
  }
  
  def getAllStatementsAndClassesForListOfIds(ids:List[String]):String={
    val asURIs=ids.map(x=>"<"+x+">")
    val idString=asURIs.mkString(" ")
    val res="construct {?s ?p ?o . ?o a ?c} where {?s ?p ?o . ?o a ?c . values ?s {"+idString+"}}"
    res
  }  
  
  
  //val totalEntities="select (count(distinct(?x)) as ?nrow) where {?x ?p ?o}"
  //val typedEntities="select (count(distinct(?x)) as ?nrow) where {?x a ?o}"
  //val literalProperties="select distinct ?p where {?s ?p ?o . filter (isLiteral(?o)) }"
  //def distProperties(value:String)=s"select (count(distinct(?o)) as ?co) where {?s <${value}> ?o}"
  //def allProperties(value:String)=s"select (count(?o) as ?co) where {?s <${value}> ?o}"
}