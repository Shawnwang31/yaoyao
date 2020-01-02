package com



import akka.actor.{Actor, ActorRef, ActorSystem, Props}





class HelloActor extends Actor {
  var actorslist:List[ActorRef]= List()
  var testHelloName = "---"
  override def preStart(): Unit = {
    super.preStart()
    for(i <- 1 to 50){
     val subactorX=context.actorOf(Props(new SubHelloActor(helloName = testHelloName)),name="subactor"+i)

      actorslist =  subactorX ::actorslist
      print("finish one sub actor---")
    }
    testHelloName = "123"
  }
  def receive = {
    case "hello" => {
      for (subactor <- actorslist) {
        subactor ! "printname"
      }
    }
    case _ => println("default")
  }
}

class SubHelloActor(helloName:String) extends Actor {
  override def preStart(): Unit = {
    super.preStart()
    Thread.sleep(500)
    println("-------------------");
  }
  def receive = {
    case "printname" => println(helloName)
    case _ => println("XXX")
  }
}

object Demo{
  def main(args: Array[String]): Unit = {

    val system = ActorSystem("HelloSystem")
    // 缺省的Actor构造函数
    val helloActor = system.actorOf(Props(new HelloActor()),name = "helloactor")

    helloActor ! "hello"
  }
}