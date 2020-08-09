package com.yc.akka.test3

import akka.actor.{Actor, ActorSelection, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

import scala.io.StdIn

class ClientActor extends Actor{
    var serverActorRef:ActorSelection=_ //创建一个ActorSelection空对象，存取serverActorRef
//容器中的生命周期  preStart() 表示在actor启动前运行这个方法
  override def preStart():Unit={
    println("preStart()")
    //可以启动服务器，查看服务器ip和端口  akka.tcp://robot_servver@127.0.0.1:10000
    //   /user/robot_server 指的是服务器 robotServerRef=actorySystem.actorOf(Props[RobotServer],"robot_server")
    serverActorRef=context.actorSelection("akka.tcp://robot_server@127.0.0.1:30000/user/robot_server")
  }

  override def receive: Receive = {
    //接到服务器消息，只要是String类型，直接打印
    case "start" => println("客户端已经启动")
    case msg:String =>{
      println("client端要发送的消息:"+msg)
      serverActorRef ! ClientMessage(msg)
    }
    case ServerMessage(msg) => println(s"收到服务器消息是$msg")
  }
}

object Client extends App{
  val host:String ="127.0.0.1"
  val port:Int=20000
  val config=ConfigFactory.parseString(
    s"""
      |akka.actor.provider="akka.remote.RemoteActorRefProvider"
      |akka.remote.netty.tcp.hostname=$host
      |akka.remote.netty.tcp.port=$port
      |""".stripMargin
  )
  private val clientSystem=ActorSystem("client",config)
  private val actorRef=clientSystem.actorOf(Props[ClientActor],name="client_actor")
  actorRef ! "start"

  //循环该用户输入
  while(true){
    val request=StdIn.readLine()
    actorRef ! request
  }
}
