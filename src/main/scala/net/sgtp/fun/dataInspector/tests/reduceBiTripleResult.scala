package net.sgtp.fun.dataInspector.tests

import collection.JavaConversions._
import org.apache.jena.rdf.model._
import net.sgtp.fun.dataInspector.analysisForTriplestores.datasourceSeederForTriplestores

object reduceBiTripleResult extends App {
  def typeProp=ResourceFactory.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")
  def classRes=ResourceFactory.createResource("http://www.w3.org/2002/07/owl#Class")
  def statRes=ResourceFactory.createResource("http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement")
  
  val input=ModelFactory.createDefaultModel().read("file:///Users/andrea/code/LODInspector/resources/test/bigResultSSB.nt"); 
  println("Initial: "+input.size)
  //val res=new datasourceSeederForTriplestores().reduceTriples(input)
  //println("Reduced: "+res.size)
  //TODO to be rewrittem
}