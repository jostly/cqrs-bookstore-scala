package bookstore.ordercontext.saga

import bookstore.ordercontext.order.event.OrderActivatedEvent
import bookstore.ordercontext.publishercontract.command.RegisterPurchaseCommand
import bookstore.ordercontext.query.service.QueryService
import bookstore.saga.Saga
import com.typesafe.scalalogging.LazyLogging

class PurchaseRegistrationSaga(queryService: QueryService) extends Saga with LazyLogging {

  override def listen: Receive = {
    case event: OrderActivatedEvent =>
      logger.info(s"Received: $event")
      queryService.getOrder(event.aggregateId) match {
        case Some(order) =>
          order.orderLines.foreach { orderLine =>
            queryService.findPublisherContract(orderLine.productId)(context.system) match {
              case Some(contractId) =>
                context.system.eventStream.publish(RegisterPurchaseCommand(contractId, orderLine.productId, orderLine.unitPrice, orderLine.quantity))
              case None =>
                logger.warn(s"Couldn't find a contract for product ${orderLine.productId}")
            }
          }
        case None =>
          logger.warn(s"Couldn't find order with id ${event.aggregateId}")
      }
  }
}
