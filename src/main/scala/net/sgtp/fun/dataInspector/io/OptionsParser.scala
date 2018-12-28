package net.sgtp.fun.dataInspector.io
import net.sgtp.fun.dataInspector.body.options
/**
 * Command line parser
 */
object OptionsParser {
    def parseCli(args:Array[String]):options ={
    val options=new options()
    parseCliAndAdd(args,options)
  }
  def parseCliAndAdd(args:Array[String],options:options):options ={
    val options=new options()
    // Debug/monitoring flags
    val verbose=args.contains("-v")        // Defaults to false
    if(args.contains("-ys")) options.yummyScore=args(args.indexOf("-ys")+1).toInt
    if(args.contains("-tquery1")) options.queryTimeOut1=args(args.indexOf("-tquery1")+1).toInt
    if(args.contains("-tquery2")) options.queryTimeOut2=args(args.indexOf("-tquery2")+1).toInt
    if(args.contains("-port")) options.port=args(args.indexOf("-port")+1).toInt
    if(args.contains("-webdir")) options.webDir=args(args.indexOf("-webdir")+1).toString
    options    
  }
  def parseArgsMap(args:Map[String,String]):options ={
    val options=new options()
    parseArgsMapAndAdd(args,options)
  }
  def parseArgsMapAndAdd(args:Map[String,String],options:options):options ={
    val options=new options()
    if(args.contains("ys")) options.yummyScore=args.get("ys").get.toInt
    if(args.contains("tquery1")) options.queryTimeOut1=args.get("tquery1").get.toInt
    if(args.contains("tquery2")) options.queryTimeOut2=args.get("tquery2").get.toInt
    options    
  }
  
  
  
}