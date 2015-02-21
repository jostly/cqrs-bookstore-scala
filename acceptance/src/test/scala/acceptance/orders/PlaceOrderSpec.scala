package acceptance.orders

import bookstore.order.query.orderlist.{OrderLineProjection, OrderProjection}
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
}
