package acceptance.orders

import bookstore.ordercontext.api.OrderActivationRequest
import bookstore.ordercontext.order.{OrderId, ProductId}
import bookstore.ordercontext.query.orderlist.OrderLineProjection
import fixture.{AbstractAcceptanceTest, SomeOrders}
import spray.http.StatusCodes

import scala.concurrent.duration._

class ActivateOrderSpec extends AbstractAcceptanceTest with SomeOrders {

  scenario("Activating an order") {
    Given("a placed order")
    placeOrder(orderRequest).status should be (StatusCodes.OK)

    When("the order is activated")
    val status = activateOrder(OrderActivationRequest(orderId)).status

    Then("the service responds with OK")
    status should be (StatusCodes.OK)

    And("the service should have that product listed as activated")
    within(1.second) {
      val orders = getOrders()
      orders should have length 1

      orders.head should have (
        'orderId (OrderId(orderId)),
        'customerName ("customer name"),
        'orderAmount (10000),
        'orderLines (List(OrderLineProjection(ProductId(productIds(0)), "title1", 1, 5000), OrderLineProjection(ProductId(productIds(1)), "title2", 2, 2500))),
        'status ("ACTIVATED")
      )
    }
  }
}
