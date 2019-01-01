package net.sgtp.fun.dataInspector.analysisForTriplestores

object queries {
  
  def searchForValueWithLiteral(value:String)="construct{?s ?p ?o} where { { ?s ?p2 \""+value+"\" . ?s ?p ?o }"+
                    "UNION { ?s ?p2 \""+value+"\"^^<http://www.w3.org/2001/XMLSchema#String> . ?s ?p ?o }" +
                    "UNION { ?s ?p2 \""+value+"\"@en . ?s ?p ?o }}"
                    
  def searchForPropertiesForLiteral(value:String)="construct {?s ?p ?o} where { ?s ?p ?o . FILTER (strends (str(?s), \""+value+"\"))}"
  def searchForResourcesForLiteral(value:String)="construct {?s ?p ?o} where { ?s ?p ?o . FILTER (strends (str(?p), \""+value+"\"))}"

  def countInstancesPerClass(value:String)="select (count(distinct(?s)) as ?res) where {?s a <"+value+">}"
  
  def getValueForProp(uri:String,prop:String)="select ?o where {<"+uri+"> <http://www.w3.org/2000/01/rdf-schema#label> ?o}"
  //val totalEntities="select (count(distinct(?x)) as ?nrow) where {?x ?p ?o}"
  //val typedEntities="select (count(distinct(?x)) as ?nrow) where {?x a ?o}"
  //val literalProperties="select distinct ?p where {?s ?p ?o . filter (isLiteral(?o)) }"
  //def distProperties(value:String)=s"select (count(distinct(?o)) as ?co) where {?s <${value}> ?o}"
  //def allProperties(value:String)=s"select (count(?o) as ?co) where {?s <${value}> ?o}"
}