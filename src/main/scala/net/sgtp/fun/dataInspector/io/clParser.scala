package net.sgtp.fun.dataInspector.io
import net.sgtp.fun.dataInspector.body.options
/**
 * Command line parser
 */
object clParser {
  def parseCli(args:Array[String]):options ={
  
    // Debug/monitoring flags
    val verbose=args.contains("-v")        // Defaults to false
  
  
    // Actions
    val searchStep= !(args.contains("-e"))
    val endpoint=  if(args.contains("-e")) args(args.indexOf("-e")+1)
                 else "" 
    // What to search
    val searchTerms=args.slice(args.indexOf("-search")+1, args.size).map(x=>x.replaceAll("_", " "))
    
                 
  
    // Timeouts and parameters
    
    val yummyScore=if(args.contains("-ys")) args(args.indexOf("-ys")+1).toInt
    else options.defaultYummyScore
    
    val expansionTimeOut1=if(args.contains("-texp1")) args(args.indexOf("-texp1")+1).toInt
    else options.defaultExpTimeout1
  
    val expansionTimeOut2=if(args.contains("-texp2")) args(args.indexOf("-texp2")+1).toInt
    else options.defaultExpTimeout2
  
    val queryTimeOut1=if(args.contains("-tquery1")) args(args.indexOf("-tquery1")+1).toInt
    else options.defaultQueryTimeout1
  
    val queryTimeOut2=if(args.contains("-tquery2")) args(args.indexOf("-tquery2")+1).toInt
    else options.defaultQueryTimeout2
    
    new options(verbose,searchStep,endpoint, yummyScore,searchTerms,expansionTimeOut1,expansionTimeOut2,queryTimeOut1,queryTimeOut2)
  }
  
  
  
}