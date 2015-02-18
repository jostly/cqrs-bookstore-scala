package bookstore.order.application

import akka.actor.{Props, ActorSystem, Actor}
import akka.io.IO
import akka.util.Timeout
import akka.pattern.ask
import bookstore.GenericId
import bookstore.domain.{AggregateRoot, Repository}
import bookstore.event.{DomainEvent, DomainEventStore}
import bookstore.order.application.infrastructure.{InMemoryDomainEventStore, DefaultRepository}
import bookstore.order.command.OrderCommandHandler
import bookstore.order.command.resource.CommandResource
import bookstore.order.query.resource.QueryResource
import org.json4s.{NoTypeHints, Formats}
import org.json4s.native.Serialization
import spray.can.Http
import spray.httpx.Json4sSupport
import spray.routing.HttpService

import scala.concurrent.duration._

class OrderApplication(val system: ActorSystem, port: Int = 8080) {

  val domainEventStore = new InMemoryDomainEventStore()

  val repository = new DefaultRepository(system.eventStream, domainEventStore)

  val handler = system.actorOf(Props(classOf[OrderCommandHandler], repository))

  val service = system.actorOf(Props(classOf[OrderRoutingActor]), "order")

  implicit val timeout = Timeout(5.seconds)
  IO(Http)(system) ? Http.Bind(service, interface = "localhost", port = port)

}

class OrderRoutingActor extends Actor
with HttpService with CommandResource with QueryResource with Json4sSupport {
  implicit def json4sFormats: Formats = Serialization.formats(NoTypeHints)

  def actorRefFactory = context

  def receive = runRoute(
    pathPrefix("service") {
      commandRoute ~ queryRoute
    }
  )
}