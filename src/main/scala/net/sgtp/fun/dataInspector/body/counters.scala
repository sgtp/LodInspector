package net.sgtp.fun.dataInspector.body

class counters {
  var queriesIssued=0
  var queriesCompleted=0
  var queriesWithResults=0
  var queriesFailed=0
  var queriesNoResult=0
  
  var endPointOpened=0
  var endPointTerminated=0;
  
  
  
  def recordOpen() ={
    queriesIssued+=1  
    dump()
  }
  def recordSuccessWithResults()={
     queriesCompleted+=1
     queriesWithResults+=1
     dump()
  }
  def recordSucessNoResult()={
     queriesNoResult+=1
     queriesCompleted+=1
     dump()
  }
  def recordFailue()={
     queriesCompleted+=1
     queriesFailed+=1
     dump()
  }
  
  
  
  
  def dump()={
    //println("Endpoints (O/T): "+endPointOpened+"/"+endPointTerminated+" Queries (I/C/R/N/F) "+queriesIssued+"/"+queriesCompleted+"/"+queriesWithResults+"/"+queriesNoResult+"/"+queriesFailed)
  }
  
}