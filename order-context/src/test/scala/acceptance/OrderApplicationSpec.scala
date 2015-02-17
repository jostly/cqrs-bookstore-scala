package acceptance

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import bookstore.order.application.OrderApplication
import bookstore.order.command.api.{CartDto, LineItemDto, PlaceOrderRequest}
import bookstore.order.query.api.{OrderId, OrderProjection, OrderStatus}
import org.json4s.native.Serialization
import org.json4s.{Formats, NoTypeHints}
import org.scalatest._
import spray.client.pipelining._
import spray.http._
import spray.httpx.Json4sSupport

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}


class OrderApplicationSpec(_system: ActorSystem) extends TestKit(_system)
with ImplicitSender with Json4sSupport
with FeatureSpecLike with Matchers with GivenWhenThen
with BeforeAndAfterAll with BeforeAndAfterEach {

  import system.dispatcher

  implicit def json4sFormats: Formats = Serialization.formats(NoTypeHints)

  def this() = this(ActorSystem("CreatingProduct"))

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  override def beforeEach {
  }

  def postRequest(request: PlaceOrderRequest, timeout: Duration = 1.second) = {
    val pipeline: HttpRequest => Future[HttpResponse] = sendReceive
    Await.result(pipeline(Post("http://localhost:8080/service/order-requests", request)), timeout)
  }

  def getOrders(timeout: Duration = 1.second) = {
    val pipeline: HttpRequest => Future[List[OrderProjection]] = sendReceive ~> unmarshal[List[OrderProjection]]
    Await.result(pipeline(Get("http://localhost:8080/service/query/orders")), timeout)
  }


  val app = new OrderApplication(system)

  val validOrderRequest = PlaceOrderRequest(
    "id1",
    "customer name",
    "customer email",
    "customer address",
    CartDto(
      "cartId1",
      10000,
      3,
      List(
        LineItemDto("productId1", "title1", 5000, 1, 5000),
        LineItemDto("productId2", "title2", 2500, 2, 5000)
      )
    )
  )

  feature("Placing an order") {
    scenario("Placing a valid order") {
      Given("a valid order request")
      val request = validOrderRequest

      When("the request is sent")
      postRequest(request)

      Then("the service should have only that product listed")
      getOrders() should be (List(OrderProjection(OrderId("1"), 0, 10000, "customer name", OrderStatus.PLACED)))
    }
  }

}
