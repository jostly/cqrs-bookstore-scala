package acceptance

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import bookstore.order.{ProductId, OrderId}
import bookstore.order.application.OrderApplication
import bookstore.order.command.api.{OrderActivationRequest, CartDto, LineItemDto, PlaceOrderRequest}
import bookstore.order.query.orderlist.{OrderLineProjection, OrderProjection}
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

  def placeOrder(request: PlaceOrderRequest, timeout: Duration = 1.second) = {
    val pipeline: HttpRequest => Future[HttpResponse] = sendReceive
    Await.result(pipeline(Post("http://localhost:8080/service/order-requests", request)), timeout).status
  }

  def activateOrder(request: OrderActivationRequest, timeout: Duration = 1.second) = {
    val pipeline: HttpRequest => Future[HttpResponse] = sendReceive
    Await.result(pipeline(Post("http://localhost:8080/service/order-requests/activations", request)), timeout).status
  }

  def getOrders(timeout: Duration = 1.second) = {
    val pipeline: HttpRequest => Future[List[OrderProjection]] = sendReceive ~> unmarshal[List[OrderProjection]]
    Await.result(pipeline(Get("http://localhost:8080/service/query/orders")), timeout)
  }

  def getEvents(timeout: Duration = 1.second) = {
    val pipeline: HttpRequest => Future[HttpResponse] = sendReceive
    Await.result(pipeline(Get("http://localhost:8080/service/query/events")), timeout).entity.asString
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
      val status = placeOrder(request)

      Then("the service responds with OK")
      status should be (StatusCodes.OK)

      And("the service should have that product listed")
      within(500 millis) {
        getOrders() should matchPattern { case List(
        OrderProjection(
          OrderId("id1"),
          _,
          "customer name",
          10000,
          List(
            OrderLineProjection(ProductId("productId1"), "title1", 1, 5000),
            OrderLineProjection(ProductId("productId2"), "title2", 2, 2500)
          ),
          "PLACED"
        )
        ) =>
        }
      }
    }

    scenario("Activating an order") {
      Given("a placed order")
      placeOrder(validOrderRequest) should be (StatusCodes.OK)

      When("the order is activated")
      val status = activateOrder(OrderActivationRequest("id1"))

      Then("the service responds with OK")
      status should be (StatusCodes.OK)

      And("the service should have that product listed as activated")
      within(500 millis) {
        getOrders() should matchPattern { case List(
        OrderProjection(
        OrderId("id1"),
        _,
        "customer name",
        10000,
        List(
        OrderLineProjection(ProductId("productId1"), "title1", 1, 5000),
        OrderLineProjection(ProductId("productId2"), "title2", 2, 2500)
        ),
        "ACTIVATED"
        )
        ) =>
        }
      }
    }
  }

}
