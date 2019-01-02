package net.sgtp.fun.dataInspector.analysis

import net.sgtp.fun.dataInspector.model.AbstractDataElement
import net.sgtp.fun.dataInspector.body.options
import net.sgtp.fun.dataInspector.body.NodesMemory

/**
 * determines an initial set of nodes for a data source (given a query)
 */
abstract class datasourceSeeder(val dsQA:datasourceQueryAnswerer,val ops:options,val wm:NodesMemory) {
  def seedFromSearchTerm(searchTerm:String,ops:options=ops):Unit  
}