package fixture

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import bookstore.ordercontext.api.{OrderActivationRequest, PlaceOrderRequest, RegisterPublisherContractRequest}
import bookstore.ordercontext.application.OrderApplication
import bookstore.ordercontext.publishercontract.event.PublisherContractRegisteredEvent
import bookstore.ordercontext.query.orderlist.OrderProjection
import bookstore.productcatalog.api.ProductDto
import bookstore.productcatalog.application.ProductApplication
import org.json4s.JsonAST.{JString, JArray}
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization
import org.json4s.native.Serialization._
import org.json4s.{Formats, NoTypeHints}
import org.scalatest._
import spray.client.pipelining._
import spray.http.{HttpRequest, HttpResponse}
import spray.httpx.Json4sSupport

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.reflect.ClassTag

abstract class AbstractAcceptanceTest extends TestKit(ActorSystem("acceptance"))
with UUIDGenerator
with ImplicitSender with Json4sSupport
with FeatureSpecLike with Matchers with GivenWhenThen
with BeforeAndAfterAll with BeforeAndAfterEach {

  import system.dispatcher

  implicit def json4sFormats: Formats = Serialization.formats(NoTypeHints)

  override def afterAll {
    orderApplication.close()
    productApplication.close()
    TestKit.shutdownActorSystem(system)
  }

  override def beforeAll {
    orderApplication.start()
    productApplication.start()
  }

  val orderApplication = new OrderApplication(system, 8090)
  val productApplication = new ProductApplication(system, 8070)

  def post(url: String, payload: AnyRef, timeout: Duration = 1.second): HttpResponse = {
    val pipeline: HttpRequest => Future[HttpResponse] = sendReceive
    Await.result(pipeline(Post(url, payload)), timeout)
  }

  def get[T](url: String, t: (HttpResponse) => T, timeout: Duration = 1.second) = {
    val pipeline: HttpRequest => Future[T] = sendReceive ~> t
    Await.result(pipeline(Get(url)), timeout)
  }

  def getJson(url: String, timeout: Duration = 1.second) = {
    val pipeline: HttpRequest => Future[HttpResponse] = sendReceive
    val s = Await.result(pipeline(Get(url)), timeout).entity.asString
    parse(s)
  }

  def host: String = "http://localhost"

  def productContextUrl: String = host + ":8070/products"

  def createProduct(product: ProductDto) = post(productContextUrl, product)

  def getProduct(id: String): Option[ProductDto] = get(productContextUrl + "/" + id, unmarshal[Option[ProductDto]])

  def getProducts(): List[ProductDto] = get(productContextUrl, unmarshal[List[ProductDto]])

  def orderCommandUrl: String = host + ":8090/service/order-requests"
  
  def placeOrder(request: PlaceOrderRequest) = post(orderCommandUrl, request)

  def activateOrder(request: OrderActivationRequest) = post(orderCommandUrl + "/activations", request)

  def orderQueryUrl: String = host + ":8090/service/query"

  def getOrders() = get(orderQueryUrl + "/orders", unmarshal[List[OrderProjection]])

  def getEventsAsJson() = getJson(orderQueryUrl + "/events")

  def getEvents[T]()(implicit manifest: Manifest[T]): List[T] = {
    val JArray(events) = getEventsAsJson()
    val className = manifest.runtimeClass.getSimpleName

    events.filter {
      case JArray(List(JString(c), _)) if c == className => true
      case _ => false
    }.map {
      case JArray(List(_, o)) => read[T](compact(render(o)))
    }
  }

  def contractCommandUrl: String = host + ":8090/service/publisher-contract-requests"

  def registerContract(request: RegisterPublisherContractRequest) = post(contractCommandUrl, request)

}

