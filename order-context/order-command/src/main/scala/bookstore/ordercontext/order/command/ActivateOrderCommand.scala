package bookstore.ordercontext.order.command

import bookstore.command.Command
import bookstore.ordercontext.order.OrderId

case class ActivateOrderCommand(orderId: OrderId) extends Command
