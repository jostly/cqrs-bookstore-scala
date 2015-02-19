package bookstore.order.event

import bookstore.event.DomainEvent
import bookstore.order.OrderId

case class OrderActivatedEvent(aggregateId: OrderId, version: Int, timestamp: Long) extends DomainEvent[OrderId]
