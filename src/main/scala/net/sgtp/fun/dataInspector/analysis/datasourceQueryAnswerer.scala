package net.sgtp.fun.dataInspector.analysis

import net.sgtp.fun.dataInspector.model.AbstractDataElement
import net.sgtp.fun.dataInspector.body.Feature._
import net.sgtp.fun.dataInspector.model.roughResults

/**
 * Wraps a data source and answers questions about it.
 * (the data source will need to be specified in constructors)
 */
abstract class datasourceQueryAnswerer {
  /**
   * Returns a list of data elements from a search string and a location
   * feature where things should be found ("Class", "Attribute" or "Relations")
   * @searchString what is to be searched
   */
  def retrieveRoughResults(feature:Feature,searchString:String):roughResults
  
  /**
   * Given a fully scoped identifier for a class (e.g.: a URI), returns all the instances associated to it.
   * 
   */
  def countInstances(classURI:String):Int
  
    /**
   * Given a fully scoped identifier for a class (e.g.: a URI), returns its name.
   * @nameProp a suggested name for the name property or attribute, fully qualified (e.g.: a URI)
   * 
   */
  def getName(uri:String,nameProp:String):String
  
  
  def getInstancesForClass(uri:String,number:Int):List[String]
}



 