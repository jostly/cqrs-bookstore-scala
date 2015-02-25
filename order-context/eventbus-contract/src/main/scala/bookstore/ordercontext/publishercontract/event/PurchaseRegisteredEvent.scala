package bookstore.ordercontext.publishercontract.event

import bookstore.event.DomainEvent
import bookstore.ordercontext.order.ProductId
import bookstore.ordercontext.publishercontract.PublisherContractId

case class PurchaseRegisteredEvent(aggregateId: PublisherContractId,
                                   version: Int,
                                   timestamp: Long,
                                   productId: ProductId,
                                   purchaseAmount: Long,
                                   feeAmount: Long,
                                   accumulatedFee: Long)
  extends DomainEvent[PublisherContractId]
