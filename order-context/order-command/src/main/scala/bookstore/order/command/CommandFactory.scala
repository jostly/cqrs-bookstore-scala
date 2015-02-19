package bookstore.order.command

import bookstore.order.{ProductId, OrderId}
import bookstore.order.command.api.{OrderActivationRequest, PlaceOrderRequest}
import bookstore.order.command.domain.{OrderLine, CustomerInformation}

object CommandFactory {

  def toCommand(request: PlaceOrderRequest): PlaceOrderCommand = {
    val cart = request.cart

    PlaceOrderCommand(
      OrderId(request.orderId),
      CustomerInformation(
        request.customerName,
        request.customerEmail,
        request.customerAddress
      ),
      cart.lineItems.map(li => OrderLine(
        ProductId(li.productId),
        li.title,
        li.quantity,
        li.price
      )),
      cart.totalPrice
    )
  }

  def toCommand(request: OrderActivationRequest): ActivateOrderCommand =
    ActivateOrderCommand(OrderId(request.orderId))

}
