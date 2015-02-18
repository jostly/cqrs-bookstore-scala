package bookstore.order.command

import akka.actor.Actor
import bookstore.domain.Repository
import bookstore.eventbus.Command
import bookstore.order.OrderId
import bookstore.order.command.domain.Order

class OrderCommandHandler(repository: Repository) extends Actor {

  context.system.eventStream.subscribe(self, classOf[Command])

  def receive = {
    case command: PlaceOrderCommand =>
      val order = new Order()
      order.place(command.orderId, command.customerInformation, command.orderLines, command.totalAmount)
      repository.save[OrderId, Order](order)

  }
}
