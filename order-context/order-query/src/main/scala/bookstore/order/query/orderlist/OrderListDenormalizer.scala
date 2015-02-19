package bookstore.order.query.orderlist

import akka.actor.Actor
import bookstore.event.DomainEvent
import bookstore.order.event.{OrderActivatedEvent, OrderPlacedEvent}
import com.typesafe.scalalogging.LazyLogging

class OrderListDenormalizer(repository: OrderProjectionRepository) extends Actor with LazyLogging {

  context.system.eventStream.subscribe(self, classOf[DomainEvent[_]])

  def receive = {
    case event: OrderPlacedEvent =>
      logger.debug(s"Received $event")
      handleEvent(event)
    case event: OrderActivatedEvent =>
      logger.debug(s"Received $event")
      handleEvent(event)

  }

  def handleEvent(event: OrderActivatedEvent): Unit =
    repository.getById(event.aggregateId) match {
      case Some(projection) =>
        repository.save(projection.copy(status = "ACTIVATED"))
  }

  def handleEvent(event: OrderPlacedEvent): Unit = {
    val projection = OrderProjection(
      event.aggregateId,
      event.timestamp,
      event.customerInformation.customerName,
      event.orderAmount,
      event.orderLines.map(line => OrderLineProjection(line.productId, line.title, line.quantity, line.unitPrice)),
      "PLACED"
    )
    repository.save(projection)
  }
}
