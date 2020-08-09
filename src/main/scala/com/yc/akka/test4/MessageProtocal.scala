package com.yc.akka.test4

/**
 * worker -> master
 */

//worker向master注册自己(信息)
case class RegisterWorkerInfo(id:String,core:Int,ram:Int)

//worker给master发送心跳信息
case class HearBeat(id:String) //发送给服务器的，用serverActorRef -> 服务器的地址 ip，端口，服务名

//worker发送给自己的信息，告诉自己说要开始周期性的向master发送心跳信息
case object SendHeartBeat //发送给自己，所以用自己的actorRef

/**
 * master -> worker
 */
//master向worker发送注册成功的消息
case object RegisterWorkerInfo

//master自己给自己发送一个检查超时worker的信息，并启动一个调度器scheduler，周期性检测删除超时的worker
case object CheckTimeOutWorker

//master发送给自己的信息，删除超时的worker
case object RemoveTimeOutWorket

//存储worker信息的类
class WorkerInfo(val id:String,core:Int,ram:Int){
  var lastHeartBeatTime:Long= _  //上一次心跳记录的时间

  override def toString: String = id+"\tcpu:"+core+",mem:"+ram
}
