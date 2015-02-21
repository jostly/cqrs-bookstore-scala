package bookstore.order.command.api

import bookstore.validation.{PropertyPath, Validatable}

case class PlaceOrderRequest(orderId: String,
                             customerName: String,
                             customerEmail: String,
                             customerAddress: String,
                             cart: CartDto) extends Validatable {

  override def validate(implicit path: PropertyPath = PropertyPath("")) = {
    requireUUID("orderId")
    requireNonEmptyString("customerName")
    requireNonEmptyString("customerEmail")
    requireNonEmptyString("customerAddress")
    requireValid("cart")
  }
}

