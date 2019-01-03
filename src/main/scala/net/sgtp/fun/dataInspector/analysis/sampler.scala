package net.sgtp.fun.dataInspector.analysis

//TODO sampler constructor to include query answerer. Simple implementation uses a LIMIT query
abstract class sampler {
  def sampleInstancesForClass(classURI:String,numberOfSamples:Int)
}