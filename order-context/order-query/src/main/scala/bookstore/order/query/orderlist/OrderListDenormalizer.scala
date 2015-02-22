package bookstore.order.query.orderlist

import bookstore.event.DomainEventListener
import bookstore.order.event.{OrderActivatedEvent, OrderPlacedEvent}
import com.typesafe.scalalogging.LazyLogging

class OrderListDenormalizer(repository: OrderProjectionRepository)
  extends DomainEventListener(supportsReplay = true) with LazyLogging {

  def listen = {
    case event: OrderPlacedEvent =>
      logger.debug(s"Received $event")
      val projection = OrderProjection(
        event.aggregateId,
        event.timestamp,
        event.customerInformation.customerName,
        event.orderAmount,
        event.orderLines.map(line => OrderLineProjection(line.productId, line.title, line.quantity, line.unitPrice)),
        "PLACED"
      )
      repository.save(projection)

    case event: OrderActivatedEvent =>
      logger.debug(s"Received $event")
      repository.getById(event.aggregateId) match {
        case Some(projection) =>
          repository.save(projection.copy(status = "ACTIVATED"))
        case None =>
      }

  }
}
