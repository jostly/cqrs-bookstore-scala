package bookstore.order.command

import bookstore.order.OrderId
import bookstore.order.command.domain.{CustomerInformation, OrderLine}

case class PlaceOrderCommand(orderId: OrderId,
                              customerInformation: CustomerInformation,
                              orderLines: List[OrderLine],
                              totalAmount: Long) extends Command
