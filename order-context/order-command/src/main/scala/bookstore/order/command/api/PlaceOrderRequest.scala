package bookstore.order.command.api

import bookstore.validation._

case class PlaceOrderRequest(orderId: String,
                             customerName: String,
                             customerEmail: String,
                             customerAddress: String,
                             cart: CartDto) {

  require(uuid(orderId))
  require(nonEmpty(customerName))
  require(nonEmpty(customerEmail))
  require(nonEmpty(customerAddress))
}

