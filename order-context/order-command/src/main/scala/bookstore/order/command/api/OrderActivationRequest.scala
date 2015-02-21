package bookstore.order.command.api

import bookstore.validation._

case class OrderActivationRequest(orderId: String) {
  require(uuid(orderId))
}
