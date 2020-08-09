package com.yc.akka.test2

import akka.actor.Actor
//给对方发消息 要持有对象才可以  sender() 去获取
class ActorA extends Actor {
  override def receive : Receive={
    case "start" => println("i am a,I am ready")  //start 自己发给自己的信息
    case "are you ok" =>{ // 这里接收别的ActorB发来的消息
      println("收到了 are you ok")
      //利用 sender 来获取发送者B
      //sender() are you ok 消息的发送者
      sender() ! "i am ok too.."
    }
  }
}
