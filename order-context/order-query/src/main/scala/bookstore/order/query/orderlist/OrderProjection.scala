package bookstore.order.query.orderlist

import bookstore.order.{OrderId, ProductId}

case class OrderProjection(orderId: OrderId,
                           orderPlacedTimestamp: Long = 0,
                           customerName: String,
                           orderAmount: Long = 0,
                           orderLines: List[OrderLineProjection],
                           status: String)

case class OrderLineProjection(productId: ProductId, title: String, quantity: Int = 0, unitPrice: Long = 0)
