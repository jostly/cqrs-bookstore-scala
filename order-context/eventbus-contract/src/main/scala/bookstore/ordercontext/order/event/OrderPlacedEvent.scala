package bookstore.ordercontext.order.event

import bookstore.event.DomainEvent
import bookstore.ordercontext.order.{CustomerInformation, OrderId, OrderLine}

case class OrderPlacedEvent(aggregateId: OrderId,
                            version: Int,
                            timestamp: Long,
                            customerInformation: CustomerInformation,
                            orderLines: List[OrderLine],
                            orderAmount: Long)
  extends DomainEvent[OrderId]

