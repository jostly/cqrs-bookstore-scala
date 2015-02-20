package fixture

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import bookstore.order.application.OrderApplication
import bookstore.productcatalog.api.ProductDto
import bookstore.productcatalog.application.ProductApplication
import org.json4s.native.Serialization
import org.json4s.{Formats, NoTypeHints}
import org.scalatest._
import spray.client.pipelining._
import spray.http.{HttpRequest, HttpResponse}
import spray.httpx.Json4sSupport

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

abstract class AbstractAcceptanceTest extends TestKit(ActorSystem("acceptance"))
with UUIDGenerator
with ImplicitSender with Json4sSupport
with FeatureSpecLike with Matchers with GivenWhenThen
with BeforeAndAfterAll with BeforeAndAfterEach {

  import system.dispatcher

  implicit def json4sFormats: Formats = Serialization.formats(NoTypeHints)

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  val orderApplication = new OrderApplication(system, 8090)
  val productApplication = new ProductApplication(system, 8070)

  def reset() {
    orderApplication.reset()
    productApplication.reset()
  }

  def post(url: String, payload: AnyRef, timeout: Duration = 1.second): HttpResponse = {
    val pipeline: HttpRequest => Future[HttpResponse] = sendReceive
    Await.result(pipeline(Post(url, payload)), timeout)
  }

  def get[T](url: String, t: (HttpResponse) => T, timeout: Duration = 1.second) = {
    val pipeline: HttpRequest => Future[T] = sendReceive ~> t
    Await.result(pipeline(Get(url)), timeout)
  }

  def host: String = "http://localhost"

  def productContextUrl: String = host + ":8070/products"

  def createProduct(product: ProductDto) = post(productContextUrl, product)

  def getProduct(id: String): Option[ProductDto] = get(productContextUrl + "/" + id, unmarshal[Option[ProductDto]])

}

