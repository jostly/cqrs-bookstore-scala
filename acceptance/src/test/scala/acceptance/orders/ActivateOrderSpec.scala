package acceptance.orders

import bookstore.order.command.api.OrderActivationRequest
import bookstore.order.{ProductId, OrderId}
import bookstore.order.query.orderlist.{OrderLineProjection, OrderProjection}
import fixture.{AbstractAcceptanceTest, SomeOrders}
import spray.http.StatusCodes

import scala.concurrent.duration._

class ActivateOrderSpec extends AbstractAcceptanceTest with SomeOrders {

  scenario("Activating an order") {
    Given("a placed order")
    placeOrder(orderRequest).status should be (StatusCodes.OK)

    When("the order is activated")
    val status = activateOrder(OrderActivationRequest(productId1)).status

    Then("the service responds with OK")
    status should be (StatusCodes.OK)

    And("the service should have that product listed as activated")
    within(1.second) {
      getOrders() should matchPattern { case List(
      OrderProjection(
        OrderId(oid),
        _,
        "customer name",
        10000,
        List(
          OrderLineProjection(ProductId(pid1), "title1", 1, 5000),
          OrderLineProjection(ProductId(pid2), "title2", 2, 2500)
        ),
        "PLACED"
      )
      ) if oid == orderId && pid1 == productId1 && pid2 == productId2 =>
      }
    }
  }
}
