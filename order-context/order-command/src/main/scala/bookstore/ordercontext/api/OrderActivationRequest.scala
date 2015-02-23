package bookstore.ordercontext.api

import bookstore.validation._

case class OrderActivationRequest(orderId: String) {
  require(uuid(orderId))
}
