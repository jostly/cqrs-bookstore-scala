package bookstore.productcatalog.application

import akka.actor.{Actor, ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import bookstore.infrastructure.{CORSSupport, BindActor}
import bookstore.productcatalog.domain.ProductRepository
import bookstore.productcatalog.infrastructure.InMemoryProductRepository
import bookstore.productcatalog.resource.ProductResource
import org.json4s.native.Serialization
import org.json4s.{Formats, NoTypeHints}
import spray.can.Http
import spray.httpx.Json4sSupport
import spray.routing.HttpService

import scala.concurrent.Await
import scala.concurrent.duration._

class ProductApplication(val system: ActorSystem, port: Int = 8080) {

  val productRepository = new InMemoryProductRepository

  val service = system.actorOf(Props(classOf[ProductRoutingActor], productRepository), "product-catalog")

  val binder = system.actorOf(Props(classOf[BindActor]))

  implicit val timeout = Timeout(5.seconds)

  def start() = {
    Await.result(binder ? Http.Bind(service, interface = "localhost", port = port), 5.seconds)
  }

  def close(): Unit = {
    Await.result(binder ? Http.Unbind, 5.seconds)
  }
}

class ProductRoutingActor(override val repository: ProductRepository) extends Actor
with HttpService with ProductResource with Json4sSupport with CORSSupport {
  implicit def json4sFormats: Formats = Serialization.formats(NoTypeHints)

  def actorRefFactory = context

  def receive = runRoute(
    respondWithCORS("http://localhost") {
      productRoute
    }
  )
}
