package bookstore.order.command.api

case class CartDto(cartId: String,
                   totalPrice: Long = 0,
                   totalQuantity: Int = 0,
                   lineItems: List[LineItemDto] = Nil)

case class LineItemDto(productId: String,
                       title: String,
                       price: Long = 0,
                       quantity: Int = 0,
                       totalPrice: Long = 0)