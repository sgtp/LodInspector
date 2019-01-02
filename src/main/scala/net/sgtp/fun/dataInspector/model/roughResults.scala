package net.sgtp.fun.dataInspector.model

import net.sgtp.fun.dataInspector.body.DataSourceType._
import net.sgtp.fun.dataInspector.body.Feature._

abstract class roughResults(val dataSource:String, val dataSourceType:DataSourceType,val query:String,val featureFound:Feature) {
   def resultsSize:Long
   def hasResults:Boolean
   def resultsSizeMeasure:String
}