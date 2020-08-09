package com.yc.akka.test2

import akka.actor.{ActorSystem, Props}

object MyApp extends App {
  private val acs = ActorSystem("test")

  //通过ActorSystem创建Actor的引用
  private val aActorRef = acs.actorOf(Props[ActorA], name = "a")

  //创建ActorB的引用，并执有ActorA的引用
  private val bActorRef = acs.actorOf(Props(new ActorB(aActorRef)), name = "b")

  aActorRef ! "start" //发送消息
  bActorRef ! "start"
}