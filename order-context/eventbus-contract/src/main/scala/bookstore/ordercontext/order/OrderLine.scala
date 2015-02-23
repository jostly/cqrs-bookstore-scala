package bookstore.ordercontext.order

case class OrderLine(productId: ProductId, title: String, quantity: Int, unitPrice: Long)
