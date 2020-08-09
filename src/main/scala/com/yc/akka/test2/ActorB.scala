package com.yc.akka.test2

import akka.actor.{Actor, ActorRef}

class ActorB(val a:ActorRef) extends  Actor{
    override def receive:Receive={
      case "start" => {
        println("i am a,I am ready")
        a ! "are you ok"  //给a 发一条消息
    }
      case "i am ok too.." =>
    {
      println("good good")
      Thread.sleep(1000)
      a ! "are you ok"
   }
  }
}
