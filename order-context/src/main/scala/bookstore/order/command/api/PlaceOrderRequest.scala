package bookstore.order.command.api

case class PlaceOrderRequest(orderId: String,
                             customerName: String,
                             customerEmail: String,
                             customerAddress: String,
                             cart: CartDto)
