package bookstore.order.command.api

import bookstore.validation.{PropertyPath, Validatable}

case class CartDto(cartId: String,
                   totalPrice: Long = 0,
                   totalQuantity: Int = 0,
                   lineItems: List[LineItemDto] = Nil) extends Validatable {

  override def validate(implicit path: PropertyPath = PropertyPath("")) = {
    requireUUID("cartId")
    requireMoreThan("totalPrice", 0L)
    requireMoreThan("totalQuantity", 0)
    requireNonEmptyList("lineItems")
    lineItems.foldLeft(0) { (i, li) =>
      li.validate(path + s"lineItems($i)")
      i+1
    }
  }
}

case class LineItemDto(productId: String,
                       title: String,
                       price: Long = 0,
                       quantity: Int = 0,
                       totalPrice: Long = 0) extends Validatable {

  override def validate(implicit path: PropertyPath = PropertyPath("")) = {
    requireUUID("productId")
    requireNonEmptyString("title")
    requireMoreThan("price", 0L)
    requireMoreThan("quantity", 0)
    requireMoreThan("totalPrice", 0L)
  }
}