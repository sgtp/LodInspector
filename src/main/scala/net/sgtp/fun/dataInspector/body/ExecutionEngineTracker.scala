package net.sgtp.fun.dataInspector.body
/**
 * Keeps tracks of the state of an execution engine
 * One class per Execution Engine. Could be an inner class or directly subsumed by it.
 * Keeping as a different class for clarity.
 */
class ExecutionEngineTracker(ee:ExecutionEngine) {
  def registerStartSeederAction(searchTerm:String)={} 
  def registerEndSeederAction(searchTerm:String)={} //TODO to be used in Future callbacks
}