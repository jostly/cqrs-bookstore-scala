package bookstore.ordercontext.order.event

import bookstore.event.DomainEvent
import bookstore.ordercontext.order.OrderId

case class OrderActivatedEvent(aggregateId: OrderId, version: Int, timestamp: Long) extends DomainEvent[OrderId]
