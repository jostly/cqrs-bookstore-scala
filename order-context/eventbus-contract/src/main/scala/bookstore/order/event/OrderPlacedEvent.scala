package bookstore.order.event

import bookstore.event.DomainEvent
import bookstore.order.{OrderLine, OrderId, CustomerInformation}

case class OrderPlacedEvent(aggregateId: OrderId,
                            version: Int,
                            timestamp: Long,
                            customerInformation: CustomerInformation,
                            orderLines: List[OrderLine],
                            orderAmount: Long)
  extends DomainEvent[OrderId]

