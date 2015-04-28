package basic

import org.apache.spark._
import SparkContext._

object WordCount_Mesos {
  def main(args : Array[String])
  {
    val conf = new SparkConf()
//      .setMaster("mesos://localhost:5050")
      .setMaster("mesos://zk://localhost:2181/mesos")
      .setAppName("My app")
      .set("spark.executor.uri", "/mesos/spark-1.2.1-bin-hadoop2.4.tgz")
//    val sc = new SparkContext(conf)
    val sc = new SparkContext(args(0), "WordCount", conf)
    
    val textFile = sc.textFile(args(1))
    
    val result = textFile.flatMap(line => line.split("\\s+")).map(word => (word, 1)).reduceByKey(_ + _)
    
    result.saveAsTextFile(args(2))
  }
}