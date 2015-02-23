package bookstore.ordercontext.publishercontract.event

import bookstore.event.DomainEvent
import bookstore.ordercontext.publishercontract.PublisherContractId

case class PublisherContractRegisteredEvent(aggregateId: PublisherContractId,
                                            version: Int,
                                            timestamp: Long,
                                            publisherName: String,
                                            feePercentage: Double,
                                            limit: Long)
  extends DomainEvent[PublisherContractId]
