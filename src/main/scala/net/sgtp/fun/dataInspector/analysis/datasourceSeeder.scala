package net.sgtp.fun.dataInspector.analysis

import net.sgtp.fun.dataInspector.model.AbstractDataElement
import net.sgtp.fun.dataInspector.body.options
import net.sgtp.fun.dataInspector.body.NodesMemory
import net.sgtp.fun.dataInspector.body.Feature._

/**
 * determines an initial set of nodes for a data source (given a query)
 */
abstract class datasourceSeeder(val dsQA:datasourceQueryAnswerer,val ops:options,val wm:NodesMemory) {
  def seedFromSearchTerm(searchTerm:String,strategy:Feature,ops:options=ops):List[AbstractDataElement]  
  val getSeederStrategies:List[Feature]
}