package bookstore.order.command

import bookstore.order.OrderId

case class ActivateOrderCommand(orderId: OrderId) extends Command
