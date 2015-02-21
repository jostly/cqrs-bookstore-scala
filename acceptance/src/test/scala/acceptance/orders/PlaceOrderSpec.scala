package acceptance.orders

import bookstore.order.query.orderlist.OrderLineProjection
import bookstore.order.{OrderId, ProductId}
import fixture.{AbstractAcceptanceTest, SomeOrders}
import spray.http.StatusCodes

import scala.concurrent.duration._

class PlaceOrderSpec extends AbstractAcceptanceTest with SomeOrders {

  feature("Placing an order") {
    scenario("Placing a valid order") {
      Given("a valid order request")
      val request = orderRequest

      When("the request is sent")
      val status = placeOrder(request).status

      Then("the service responds with OK")
      status should be(StatusCodes.OK)

      And("the service should have that product listed")
      within(1.second) {
        val orders = getOrders()
        orders should have length 1

        orders.head should have (
          'orderId (OrderId(orderId)),
          'customerName ("customer name"),
          'orderAmount (10000),
          'orderLines (List(OrderLineProjection(ProductId(productId1), "title1", 1, 5000), OrderLineProjection(ProductId(productId2), "title2", 2, 2500))),
          'status ("PLACED")
        )
      }
    }
  }
}
