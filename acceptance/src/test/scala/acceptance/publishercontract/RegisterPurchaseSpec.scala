package acceptance.publishercontract

import bookstore.ordercontext.api.OrderActivationRequest
import bookstore.ordercontext.publishercontract.PublisherContractId
import bookstore.ordercontext.publishercontract.event.PurchaseRegisteredEvent
import fixture.{AbstractAcceptanceTest, SomeContracts, SomeOrders, SomeProducts}
import spray.http.StatusCodes

import scala.concurrent.duration._

class RegisterPurchaseSpec extends AbstractAcceptanceTest with SomeOrders with SomeContracts with SomeProducts {

  scenario("Activating an order") {

    Given("registered contracts")
    registerContract(contracts: _*)

    And("created products")
    createProduct(products: _*)

    And("a placed order")
    placeOrder(orderRequest).status should be(StatusCodes.OK)

    When("the order is activated")
    val status = activateOrder(OrderActivationRequest(orderId)).status

    Then("the service responds with OK")
    status should be(StatusCodes.OK)

    And("the service should have that product listed as activated")
    Thread.sleep(200)
    within(1.second) { // TODO this doesn't do what I thought it did
      val events = getEvents[PurchaseRegisteredEvent]

      events should have length 2
      val ev1 = events.head
      val ev2 = events.tail.head
      ev1 should have(
        'aggregateId (PublisherContractId(products(0).publisherContractId)),
        'purchaseAmount (5000),
        'feeAmount (275),
        'accumulatedFee (550)
      )
      ev2 should have(
        'aggregateId (PublisherContractId(products(0).publisherContractId)),
        'purchaseAmount (5000),
        'feeAmount (275),
        'accumulatedFee (275)
      )
    }
  }
}
