package com.yc.akka.test1

import akka.actor.{Actor, ActorSystem, Props}
//必须继承自Actor
class HelloActor extends Actor{

  //Actor中的回调方法，用于获取消息
  //receive    Receive 对象
  override def receive: Receive ={
    //模式匹配  提取器
    //接收消息并处理
    case "hello" => println("hello too")
    case "bye" => println("bye too")
    case "stop" =>{
      //context系统上下文
      context.stop(self)//context.stop(self)停止当前环境
      context.system.terminate()//关闭ActorSystem
    }
  }
}

object HelloActor{
  //取得actorRef对象
  private val system=ActorSystem("actor_system_object")
  //取得actorRef
  private val helloActorRef=system.actorOf(Props[HelloActor],name = "helloActor")
  def main(args:Array[String]):Unit={
    //给自己发消息
    helloActorRef ! "hello"
    helloActorRef ! "bye"
    helloActorRef ! "stop"
  }
}