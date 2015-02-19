package bookstore.order.command

import bookstore.eventbus.Command
import bookstore.order.OrderId

case class ActivateOrderCommand(orderId: OrderId) extends Command
