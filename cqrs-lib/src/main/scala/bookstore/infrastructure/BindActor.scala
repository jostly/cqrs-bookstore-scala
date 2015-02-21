package bookstore.infrastructure

import akka.actor.{ActorRef, Actor}
import akka.io.IO
import com.typesafe.scalalogging.LazyLogging
import spray.can.Http

class BindActor extends Actor with LazyLogging {

  def receive = {
    case bind: Http.Bind =>
      IO(Http)(context.system) ! bind
      context.become(binding(sender()))
  }

  def binding(app: ActorRef): Receive = {
    case b: Http.Bound =>
      app forward b
      context.become(bound(sender()))
    case x: AnyRef => logger.error(s"Binding received unexpected: $x")
  }

  def bound(control: ActorRef): Receive = {
    case unbind: Http.Unbind =>
      control.forward(unbind)
    case x: AnyRef => logger.error(s"Bound received unexpected: $x")
  }

}
