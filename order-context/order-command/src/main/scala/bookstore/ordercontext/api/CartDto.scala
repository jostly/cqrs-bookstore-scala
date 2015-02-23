package bookstore.ordercontext.api

import bookstore.validation._

case class CartDto(cartId: String,
                   totalPrice: Long = 0,
                   totalQuantity: Int = 0,
                   lineItems: List[LineItemDto] = Nil) {

  require(uuid(cartId))
  require(totalPrice > 0)
  require(totalQuantity > 0)
  require(nonEmpty(lineItems))
}

case class LineItemDto(productId: String,
                       title: String,
                       price: Long = 0,
                       quantity: Int = 0,
                       totalPrice: Long = 0) {

  require(uuid(productId))
  require(nonEmpty(title))
  require(price > 0)
  require(quantity > 0)
  require(totalPrice > 0)
}