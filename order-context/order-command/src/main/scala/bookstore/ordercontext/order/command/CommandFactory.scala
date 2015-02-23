package bookstore.ordercontext.order.command

import bookstore.ordercontext.api.{OrderActivationRequest, PlaceOrderRequest}
import bookstore.ordercontext.order.domain.{CustomerInformation, OrderLine}
import bookstore.ordercontext.order.{OrderId, ProductId}

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
