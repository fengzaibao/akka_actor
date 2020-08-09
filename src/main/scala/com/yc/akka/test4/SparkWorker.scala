package com.yc.akka.test4

import java.util.UUID

import akka.actor.{Actor, ActorSelection, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration._   //导入时间单位millis

class SparkWorker(masterUrl:String) extends Actor{
  //master的actorRef
  var masterProxy:ActorSelection= _
  val workId=UUID.randomUUID().toString  //客户端的id
  override def preStart():Unit={
    masterProxy=context.actorSelection(masterUrl)
  }
  override def receive: Receive ={
    case "started" => {//自己已经就绪
      //向master注册自己的信息 id，core，ram
      //TODO:获取自己的cpu，内存
      println("客户端"+workId+"启动.....")
      masterProxy ! RegisterWorkerInfo(workId,4,32*1024)//此时向master发送注册信息
    }
    case RegisterWorkerInfo => {//master通过响应发送给自己的注册成功信息,接到这个消息后,则定时发送心跳信息给服务器
      //worker启动一个定时器，定时向master发送心跳
      import context.dispatcher  //因为schedule中需要参数 implicit executor: ExecutionContext,sender:ActorRef=Actor.nosender
      //context上下文指的是当前的actor
      //scheduler 定时调度器对象
      /*
        final def schedule(
          initialDelay: FiniteDuration
          interval : FiniteDuration,
          receiver : ActorRef,
          message: Any)
          (implicit executor: ExecutionContext,sender:ActorRef=Actor.nosender)
       */
      context.system.scheduler.schedule(initialDelay = 0 millis ,interval = 1500 millis,self,SendHeartBeat)
    }
    case SendHeartBeat => {
      //开始向master发送心跳了
      println(s"----$workId 发送心跳---")
      masterProxy ! HearBeat(workId) //此时master将会收到心跳信息
    }
  }
}

object SparkWorker{
  def main(args:Array[String]):Unit={
    var host="127.0.0.1"
    var port="10002"
    var workName="spark_worker"
    var masterUrl="akka.tcp://sparkMaster@127.0.0.1:10001/user/master1"

    //检验命令行参数
    if(args.length==4){
      host=args(0)
      port=args(1)
      workName=args(2)
      masterUrl=args(3)
    }
    val config=ConfigFactory.parseString(
      s"""
         |akka.actor.provider="akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname=$host
         |akka.remote.netty.tcp.port=$port
         |""".stripMargin
    )
    val actorSystem=ActorSystem("sparkWorker",config)
    //创建自己的actorRef
    val workerActorRef=actorSystem.actorOf(Props(new SparkWorker(masterUrl)),workName)

    workerActorRef !  "started"
  }
}