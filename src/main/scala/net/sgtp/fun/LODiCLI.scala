package net.sgtp.fun

import config._
import helper._

object LODiCLI extends App {
  val verbose=args.contains("-v")
  val action=if(args.contains("-search")) "search"
  else if(args.contains("-inspect")) "inspect"
  else "nothing"
  if(verbose) println("Action is: "+action)
  if(action.equals("nothing")) System.exit(1)
  
  val searchString=action match {
    case "search" => args(args.indexOf("-search")+1)
    case _ => ""
  }
  
  val inspectTypes=action match {
    case "inspect" => (args.contains("-t") || (!args.contains("-t") && !args.contains("-p")))
    case _ => false  
  }
  val inspectProperties=action match {
    case "inspect" => (args.contains("-p") || (!args.contains("-t") && !args.contains("-p")))
    case _ => false  
  }
  
  val expansion=if(args.contains("-noexp")) false
  else true
  
  val expansionTimeOut1=if(args.contains("-texp1")) args(args.indexOf("-texp1")+1).toInt
  else defaultExpTimeout1
  
  val expansionTimeOut2=if(args.contains("-texp2")) args(args.indexOf("-texp2")+1).toInt
  else defaultExpTimeout2
  
  val queryTimeOut1=if(args.contains("-tquery1")) args(args.indexOf("-tquery1")+1).toInt
  else defaultQueryTimeout1
  
  val queryTimeOut2=if(args.contains("-tquery2")) args(args.indexOf("-tquery2")+1).toInt
  else defaultQueryTimeout2
  
  val endpoints=args.contains("-e") match {
    case true    =>args(args.indexOf("-e")+1).split(",").toList
    case false   =>{
      val yummyScore=args.contains("-ys") match {
        case true  => args(args.indexOf("-ys")+1).toInt
        case false => defaultYummyScore
      }
      if(verbose) println("Querying yummydata for endpoints with score > "+yummyScore)
      endpointsFromUmaka(yummyScore)
    }  
  }
  
  if(verbose) { 
    println(s"Endpoints in scope: ${endpoints.size}")
    endpoints.foreach(println)
  }
  
  if(action.equals("search")) {
    val stringsToSearch=if(!expansion) List(searchString)
    else {
      if(verbose) println("Query expansion step")
      expandQueryTerms(searchString,endpoints,verbose,expansionTimeOut1,expansionTimeOut2).toList
    }
    if(verbose) {
      println("Expanded search terms: ")
      stringsToSearch.foreach(println)
    }
    if(stringsToSearch.size==0) {
      println("Nothing found")
      System.exit(1)
    }
    if(verbose) println("Querying systems")
    val namesToData=retrieveTriples(stringsToSearch,endpoints,verbose,queryTimeOut1,queryTimeOut2).toList.filter(_._2.size>0)
    val sortedNamesToData=namesToData.sortWith(_._2.size>_._2.size)
    sortedNamesToData.foreach(x=>println(s"for ${x._1} found ${x._2.size} statemets")) 
    println("Overlap analysis")
    val result=overlapAnalysis(sortedNamesToData).filter(_._3!=100).sortWith(_._3<_._3)
    result.foreach(x=>println(s"Overlap of ${x._3}% found between ${x._1} and ${x._2}"))
   
    
  }
  
  if(action.equals("inspect")){
    val res=endpoints.par.map(e=>{
      if(inspectTypes) {
        val typeCoverage=computeTypeCoverage(e,queryTimeOut1,queryTimeOut2)
        if(verbose) {
          if(typeCoverage.isDefined) println(s"Type coverage for ${e} is ${typeCoverage.get} %")
          else println(e+ " could not compute (in time)")
        }
      }
      if(inspectProperties) {
        val propertyStats=computePropertyStats(e,verbose,queryTimeOut1,queryTimeOut2)
        if(verbose) {
          if(propertyStats.isDefined) {
            val allProps=propertyStats.get
            println("PROPERTY ANALYSIS for "+e)
            println("Total props : "+allProps.size)
            val analyzedProps=allProps.filter(_.isDefined).map(_.get).sortWith(_._2<_._2)
            println("Results for : "+analyzedProps.size)
            analyzedProps.foreach(x=>println(s"${x._2} for ${x._1}"))
          }
        }
      }

    })
  }
  
  
}