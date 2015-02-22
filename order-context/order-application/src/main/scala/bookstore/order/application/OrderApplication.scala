package bookstore.order.application

import akka.actor.{ActorRef, Props, ActorSystem, Actor}
import akka.io.IO
import akka.util.Timeout
import akka.pattern.ask
import bookstore.GenericId
import bookstore.domain.{AggregateRoot, Repository}
import bookstore.event.{DomainEvent, DomainEventStore}
import bookstore.infrastructure.BindActor
import bookstore.order.application.infrastructure.{InMemoryOrderProjectionRepository, InMemoryDomainEventStore, DefaultRepository}
import bookstore.order.command.OrderCommandHandler
import bookstore.order.command.resource.CommandResource
import bookstore.order.query.orderlist.OrderListDenormalizer
import bookstore.order.query.resource.QueryResource
import bookstore.order.query.service.QueryService
import com.typesafe.scalalogging.LazyLogging
import org.json4s.{NoTypeHints, Formats}
import org.json4s.native.Serialization
import spray.can.Http
import spray.httpx.Json4sSupport
import spray.routing.HttpService

import scala.concurrent.Await
import scala.concurrent.duration._

class OrderApplication(val system: ActorSystem, port: Int = 8080) extends LazyLogging {

  val domainEventStore = new InMemoryDomainEventStore()

  val repository = new DefaultRepository(system.eventStream, domainEventStore)

  val handler = system.actorOf(Props(classOf[OrderCommandHandler], repository))

  val orderProjectionRepository = new InMemoryOrderProjectionRepository()

  system.actorOf(Props(classOf[OrderListDenormalizer], orderProjectionRepository))

  val queryService = new QueryService(orderProjectionRepository)

  val service = system.actorOf(Props(classOf[OrderRoutingActor], domainEventStore, queryService), "order")

  val binder = system.actorOf(Props(classOf[BindActor]))

  implicit val timeout = Timeout(5.seconds)

  def start() = {
    Await.result(binder ? Http.Bind(service, interface = "localhost", port = port), 5.seconds)
  }

  def close(): Unit = {
    Await.result(binder ? Http.Unbind, 5.seconds)
  }
}

class OrderRoutingActor(val domainEventStore: DomainEventStore, val queryService: QueryService) extends Actor
with HttpService with CommandResource with QueryResource with Json4sSupport {
  implicit def json4sFormats: Formats = Serialization.formats(NoTypeHints)

  def actorRefFactory = context

  def receive = runRoute(
    pathPrefix("service") {
      commandRoute ~ queryRoute
    }
  )
}