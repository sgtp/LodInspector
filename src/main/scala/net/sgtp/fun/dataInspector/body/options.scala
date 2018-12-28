package net.sgtp.fun.dataInspector.body
/**
 * Capture options for the execution
 */

object options{
  val defaultVerbose=true
  val defaultYummyScore=75       // Filter cutoff for calls to YummyData
  val defaultExpTimeout1=3000    // ?
  val defaultExpTimeout2=6000    // ?
  val defaultQueryTimeout1=3000   // ?
  val defaultQueryTimeout2=6000   // ?
  val defaultPort=8090
  val defaultWebDir="resources/web/"
  val parThreads=300
}

class options {
  
    var verbose:Boolean= options.defaultVerbose
    var yummyScore:Int=options.defaultYummyScore
    var queryTimeOut1:Int=options.defaultQueryTimeout1
    var queryTimeOut2:Int=options.defaultQueryTimeout2
    var port:Int=options.defaultPort
    var webDir:String=options.defaultWebDir
    var parThreads:Int=options.parThreads
    
    
    def print()={
      println("Options:")
      println("verbose "+verbose)
      println("queryTimeOut1 "+queryTimeOut1)
      println("queryTimeOut2 "+queryTimeOut2)
    }
}