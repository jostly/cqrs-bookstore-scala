package bookstore.order.query.orderlist

import akka.actor.Actor
import bookstore.event.DomainEvent
import bookstore.order.event.OrderPlacedEvent
import com.typesafe.scalalogging.LazyLogging

class OrderListDenormalizer(repository: OrderProjectionRepository) extends Actor with LazyLogging {

  context.system.eventStream.subscribe(self, classOf[DomainEvent[_]])

  def receive = {
    case event: OrderPlacedEvent =>
      logger.debug(s"Received $event")
      handleEvent(event)

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
