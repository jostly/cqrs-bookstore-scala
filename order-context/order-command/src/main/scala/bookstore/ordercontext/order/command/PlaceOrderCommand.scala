package bookstore.ordercontext.order.command

import bookstore.command.Command
import bookstore.ordercontext.order.OrderId
import bookstore.ordercontext.order.domain.{CustomerInformation, OrderLine}

case class PlaceOrderCommand(orderId: OrderId,
                              customerInformation: CustomerInformation,
                              orderLines: List[OrderLine],
                              totalAmount: Long) extends Command
