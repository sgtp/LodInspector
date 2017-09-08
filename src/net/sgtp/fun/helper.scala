package net.sgtp.fun

import collection.JavaConversions._

import org.json4s._
import org.json4s.native.JsonMethods._

import org.apache.jena.query._;
import org.apache.jena.rdf.model._

import Queries._

object helper {
  def endpointsFromUmaka(score:Int):List[String]={
      val umakaEndpointsQuery="http://d.umaka.dbcls.jp/api/endpoints/search?alive_rate_lower="+score
      val result=scala.io.Source.fromURL(umakaEndpointsQuery).getLines.mkString("\n")
      val jsonParsedResult=parse(result)
      val endpoints=for {
        JObject(endpoint) <- jsonParsedResult
        JField("url", JString(url))  <- endpoint
      } yield url  
      endpoints
  }
  def expandQueryTerms(value:String,endpoints:List[String],verbose:Boolean,expansionTimeOut1:Int,expansionTimeOut2:Int)={
    val expandedQuery=searchQuery(value)   
    val queryResults=endpoints.par.map(e=>{
         val query = QueryFactory.create(expandedQuery); 
         val qexec = QueryExecutionFactory.sparqlService(e, query);
         qexec.setTimeout(expansionTimeOut1, expansionTimeOut2);
         try {  val r = qexec.execSelect().toList; 
           val res=r.map(x=>x.getLiteral("values").getString)
           if(verbose) println(e+ " OK")
           res
        }
        catch {case ex => {if(verbose) println(s"Exception for ${e} : "+ex.getMessage);  List[String]()}}
    //r
  })
  
  val names=queryResults.flatten.toSet 
  names.foreach(println)
  names
  }
  
  
  def retrieveTriples(names:List[String],endpoints:List[String],verbose:Boolean,queryTimeout1:Int,queryTimeout2:Int)={
    val total=endpoints.size*names.size
    var counter=0
    val eToNames=endpoints.par.map(e=>{(e,{
    val m=ModelFactory.createDefaultModel()
    names.foldLeft(m){(m,i)=>{
      val queryString=searchViaSimpleLiteral(i)
      val query=QueryFactory.create(queryString)
      val qexec = QueryExecutionFactory.sparqlService(e, query)
      qexec.setTimeout(queryTimeout1,queryTimeout2)
      if(verbose) if(counter%20==0)  println(((counter.toDouble/total.toDouble)*100).toInt+"%")
      try {
        val innerModel=qexec.execConstruct()
        counter+=1
        m.add(innerModel)
      }
      catch {case _=>{counter+=1;m}}
    }
    
    }
    m
  })
  })
  eToNames
   
  }
   
  def overlapAnalysis(eToStats:List[(String,Model)])={
    val reduced=eToStats.filter(_._2.size>0)
    val literals=reduced.map(x=>(x._1,{
    x._2.listObjects.toList.filter(y=>y.isLiteral)
    }))

    val res=for {
      loop1 <- literals
      loop2 <- literals
      val e1=loop1._1
      val l1=loop1._2.toSet
      val e2=loop2._1
      val l2=loop2._2.toSet
      val inters=l1.intersect(l2)
      val union=l1++l2
    } yield (e1,e2,((inters.size.toDouble/union.size.toDouble)*100).toInt)
    
  res
 //returns here
  }
  
  def computeTypeCoverage(e:String,queryTimeOut1:Int,queryTimeOut2:Int):Option[Int]= {
    val totalE=runQueryAndGetSingleNumericValue(totalEntities,e,queryTimeOut1,queryTimeOut2)
    val typedE=runQueryAndGetSingleNumericValue(typedEntities,e,queryTimeOut1,queryTimeOut2)
   
    if(totalE.isDefined && typedE.isDefined) {
      val tot=totalE.get.toDouble
      val typed=typedE.get.toDouble
      val perc=((typed*100.0)/tot).toInt
      Some(perc)
    }
    else None
  }
  
  def runQueryAndGetSingleNumericValue(queryString:String,endpoint:String,t1:Int,t2:Int):Option[Int]={
    val query = QueryFactory.create(queryString); 
    val qexec = QueryExecutionFactory.sparqlService(endpoint, query);
    qexec.setTimeout(t1, t2);
    try {  val r = qexec.execSelect(); 
           val variable=r.getResultVars.get(0)
           val res=r.next.getLiteral(variable).getInt
           Some(res)
        }
    catch {
      case exc:Exception => None
    }
  } 
  
  
  def computePropertyStats(e:String,verbose:Boolean,t1:Int,t2:Int):Option[List[Option[(String,Int)]]]={
    val query = QueryFactory.create(literalProperties); 
    val qexec = QueryExecutionFactory.sparqlService(e, query);
    qexec.setTimeout(t1, t2);
    val properties=try {  val r = qexec.execSelect().toList
           Some(r.map(x=>x.getResource("p").toString))
        }
    catch {
      case exc:Exception => None
    }
    properties match {
      case None =>  {if(verbose) println("No props found for : "+e);None}
      case Some(propertiesValue) => {
        val res=properties.get.par.map(p=>{
        def allP=runQueryAndGetSingleNumericValue(allProperties(p),e,t1,t2)
        def distP=runQueryAndGetSingleNumericValue(distProperties(p),e,t1,t2)
        if(allP.isDefined && distP.isDefined) {
          val tot=allP.get.toDouble
          val dist=distP.get.toDouble
          val perc=((dist*100.0)/tot).toInt
          //println(s"$e $p $tot $dist")
          Some((p,perc))
        }
        else {
          if(verbose) println(e+ "can't compute "+p)
          None  
        }
      })
      Some(res.toList)
    }
      }
    }
   
  
  
  
}