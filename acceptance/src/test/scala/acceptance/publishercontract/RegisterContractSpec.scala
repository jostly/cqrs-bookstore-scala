package acceptance.publishercontract

import bookstore.ordercontext.api.RegisterPublisherContractRequest
import bookstore.ordercontext.publishercontract.PublisherContractId
import bookstore.ordercontext.publishercontract.event.PublisherContractRegisteredEvent
import fixture.{AbstractAcceptanceTest, SomeOrders}
import spray.http.StatusCodes

class RegisterContractSpec extends AbstractAcceptanceTest with SomeOrders {

  val contractId = nextId()

  feature("Register a contract") {
    scenario("Registering a contract") {
      Given("a valid contract request")
      val request = RegisterPublisherContractRequest(contractId, "publisherName", feePercentage = 1.5, limit = 1000)

      When("the request is sent to the service")
      val status = registerContract(request).status

      Then("the service responds with OK")
      status should be (StatusCodes.OK)

      And("the events contain a PublisherContractRegisteredEvent")
      val events = getEvents[PublisherContractRegisteredEvent]

      events should have length (1)
      events.head should have (
        'aggregateId (PublisherContractId(contractId)),
        'publisherName ("publisherName"),
        'feePercentage (1.5),
        'limit (1000)
      )
    }
  }
}
