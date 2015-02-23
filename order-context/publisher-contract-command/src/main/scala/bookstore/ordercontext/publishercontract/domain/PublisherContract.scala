package bookstore.ordercontext.publishercontract.domain

import bookstore.domain.AggregateRoot
import bookstore.ordercontext.publishercontract.PublisherContractId
import bookstore.ordercontext.publishercontract.event.PublisherContractRegisteredEvent

class PublisherContract extends AggregateRoot[PublisherContractId] {

  var publisherName: String = ""
  var feePercentage: Double = 0.0
  var limit: Long = 0L
  var accumulatedFee: Long = 0L

  def register(publisherContractId: PublisherContractId,
               publisherName: String,
               feePercentage: Double,
               limit: Long): Unit = {

    assertHasNotBeenRegistered()
    applyChange(PublisherContractRegisteredEvent(publisherContractId, nextVersion(), now(), publisherName, feePercentage, limit))
  }

  private[this] def assertHasNotBeenRegistered() {
    if (id != null) throw new IllegalStateException("Contract has already been registered")
  }

  def handleEvent(event: PublisherContractRegisteredEvent) {
    id = event.aggregateId
    version = event.version
    timestamp = event.timestamp
    publisherName = event.publisherName
    feePercentage = event.feePercentage
    limit = event.limit
    accumulatedFee = 0L
  }

}
