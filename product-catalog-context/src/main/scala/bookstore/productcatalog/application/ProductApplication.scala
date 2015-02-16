package bookstore.productcatalog.application

import akka.actor.{Props, ActorSystem}
import akka.io.IO
import akka.util.Timeout
import bookstore.productcatalog.infrastructure.InMemoryProductRepository
import bookstore.productcatalog.resource.ProductResource
import spray.can.Http
import akka.pattern.ask

import scala.concurrent.duration._

class ProductApplication(val system: ActorSystem) {

  val productRepository = new InMemoryProductRepository

  // create and start our service actor
  val service = system.actorOf(Props(classOf[ProductResource], productRepository), "demo-service")

  implicit val timeout = Timeout(5.seconds)
  // start a new HTTP server on port 8080 with our service actor as the handler
  IO(Http)(system) ? Http.Bind(service, interface = "localhost", port = 8080)

}
