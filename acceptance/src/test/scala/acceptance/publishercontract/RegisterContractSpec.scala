package acceptance.publishercontract

import bookstore.ordercontext.publishercontract.PublisherContractId
import bookstore.ordercontext.publishercontract.event.PublisherContractRegisteredEvent
import fixture.{AbstractAcceptanceTest, SomeContracts}
import spray.http.StatusCodes

class RegisterContractSpec extends AbstractAcceptanceTest with SomeContracts {

  feature("Register a contract") {
    scenario("Registering a contract") {
      Given("a valid contract request")
      val request = contracts(0)

      When("the request is sent to the service")
      val status = registerContract(request).head.status

      Then("the service responds with OK")
      status should be (StatusCodes.OK)

      And("the events contain a PublisherContractRegisteredEvent")
      val events = getEvents[PublisherContractRegisteredEvent]

      events should have length 1
      events.head should have (
        'aggregateId (PublisherContractId(request.publisherContractId)),
        'publisherName (request.publisherName),
        'feePercentage (request.feePercentage),
        'limit (request.limit)
      )
    }
  }
}
