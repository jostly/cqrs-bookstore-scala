package bookstore.order.command

import bookstore.command.Command
import bookstore.order.OrderId

case class ActivateOrderCommand(orderId: OrderId) extends Command
