package bookstore.order.command

import akka.actor.Actor
import bookstore.domain.Repository
import bookstore.order.OrderId
import bookstore.order.command.domain.Order
import com.typesafe.scalalogging.LazyLogging

class OrderCommandHandler(repository: Repository) extends Actor with LazyLogging {

  context.system.eventStream.subscribe(self, classOf[Command])

  def receive = {
    case command: PlaceOrderCommand =>
      val order = new Order()
      order.place(command.orderId, command.customerInformation, command.orderLines, command.totalAmount)
      repository.save[OrderId, Order](order)

    case command: ActivateOrderCommand =>
      val order = repository.load(command.orderId, classOf[Order])
      order.activate()
      repository.save[OrderId, Order](order)
  }
}
