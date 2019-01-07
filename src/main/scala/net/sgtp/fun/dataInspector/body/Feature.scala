package net.sgtp.fun.dataInspector.body

object Feature extends Enumeration {
  type Feature=Value
  val VALUE, PROPERTY, RESOURCE = Value
  def isSwarmable(e:Feature)=if(e==VALUE)  true else false
}

