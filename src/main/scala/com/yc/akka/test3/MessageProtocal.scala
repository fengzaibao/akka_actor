package com.yc.akka.test3
//当一个类被定义成为case类后，Scala会自动帮你创建一个伴生对象并帮你实现一系列方法
//1.实现apply方法，意味你不需要使用new关键字就能创建该类对象
//2.实现了unapply方法，可以通过模式匹配来获取类属性，是Scala中抽取器的实现和模式匹配的关键方法
//3.实现了类构造参数的getter方法(构造参数默认被声明为val)，但是当你构造参数是声明为var类型的，它将帮你实现setter和getter方法
//4.还默认帮你实现了toString，equals，copy和hashCode方法

//消息协议的样例类
//1.服务器发给客户端的协议
case class ServerMessage(msg:String) ////ServerMessage("xxx")  => apply("xxx")

//2.客户端发给服务器的协议
case class ClientMessage(msg:String)


/**
 * 自定义tomcat : http协议   流，StringTokenizer -> header,body....
 *
 * browser -> server  请求
 *
 * server -> broser  响应
 */

