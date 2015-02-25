package bookstore.ordercontext.client.productcatalog

import akka.actor.ActorSystem
import bookstore.ordercontext.order.ProductId
import bookstore.ordercontext.publishercontract.PublisherContractId
import org.json4s.{NoTypeHints, Formats}
import org.json4s.native.Serialization
import spray.client.pipelining._
import spray.http.HttpRequest
import spray.httpx.Json4sSupport

import scala.concurrent.duration._

import scala.concurrent.{Await, Future}

class ProductCatalogClient extends Json4sSupport {

  implicit def json4sFormats: Formats = Serialization.formats(NoTypeHints)

  def getProduct(productId: ProductId)(implicit actorSystem: ActorSystem): Option[ProductDto] = {
    import actorSystem.dispatcher
    try {
      val pipeline: HttpRequest => Future[ProductDto] = sendReceive ~> unmarshal[ProductDto]
      val product = Await.result(pipeline(Get("http://localhost:8070/products/" + productId.id)), 1.second)
      Some(product)
    } catch {
      case _: Exception => None
    }
  }
}
