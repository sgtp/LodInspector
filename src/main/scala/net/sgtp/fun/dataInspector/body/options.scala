package net.sgtp.fun.dataInspector.body
/**
 * Capture options for the execution
 */

object options{
  val defaultYummyScore=75       // Filter cutoff for calls to YummyData
  val defaultExpTimeout1=3000    // ?
  val defaultExpTimeout2=6000    // ?
  val defaultQueryTimeout1=3000   // ?
  val defaultQueryTimeout2=6000   // ?
}

class options(
    val verbose:Boolean,
    val searchStep:Boolean,
        endpoint:String,
    val yummyScore:Int,    
    val searchStrings:Array[String],
    val expansionTimeOut1:Int,
    val expansionTimeOut2:Int,
    val queryTimeOut1:Int,
    val queryTimeOut2:Int) {
     val endpoints= endpoint.length() match {
        case 0 =>  Array[String] ()
        case _ =>  Array(endpoint)
      }
     
    def print()={
     
      System.out.println("Options")
      System.out.println("verbose "+verbose);
      System.out.println("SearchStep "+searchStep)
      if(!searchStep) {
        System.out.println("endpoints:")
        endpoints.foreach(x=>println("\t"+x))
      }
      System.out.println("queryTimeOut1 "+queryTimeOut1)
      System.out.println("queryTimeOut2 "+queryTimeOut2)
      System.out.println("Search for: "+searchStrings.mkString(" "))
    }
}