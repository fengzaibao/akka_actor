package com.yc.akka.test3

import java.text.SimpleDateFormat
import java.util.Date

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
//聊天服务器：在这里，它能接收客户端发送过来的消息，再返回给客户端
class RobotServer extends Actor{
  override def receive: Receive = {
    case "start" => println("服务器已经启动")
    case ClientMessage(msg)=> {
      println(s"接收客户端消息$msg")
      msg match {
        case "what is your name" => sender() ! ServerMessage("hello") //sender()这条信息的客户端 !发  serverMessage("hello")响应
        case "time" =>{
          val d=new Date
          sender() ! ServerMessage(d.toString)
        }
        case "data" =>{
          val df=new SimpleDateFormat("yyyy-MM-dd")
          val d=df.format(new Date)
          sender() !ServerMessage(d)
        }
        case _ =>sender() ! ServerMessage("are you kidding me?") //_表示全部匹配不上，则错误  例如tomcat -> 404
      }
    }


  }
}
object MyApp extends App{
  val host:String="127.0.0.1"
  val port:Int=30000
  val config=ConfigFactory.parseString(
    s"""
       |akka.actor.provider="akka.remote.RemoteActorRefProvider"
       |akka.remote.netty.tcp.hostname=$host
       |akka.remote.netty.tcp.port=$port
       |""".stripMargin
  )
  //指定ip和端口
  private val actorySystem=ActorSystem("robot_server",config)
  //注意:这里创建一个名字为robot_server的服务器Actor的引用，这里是可以创建多个的，名字不同即可
  //客户端通过:serverActorRef=context.actorSelection("")
  //获取一个与指定服务端实例的引用
  private val robotServerRef=actorySystem.actorOf(Props[RobotServer],name = "robot_server")
  robotServerRef ! "start"
}

