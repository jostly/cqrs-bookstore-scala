package bookstore.order.query.api

// TODO: status should be a proper type, but this needs to be split into a Dto then
case class OrderProjectionDto(orderId: OrderIdDto,
                      orderPlacedTimestamp: Long = 0,
                      orderAmount: Long = 0,
                      customerName: String,
                      status: String)

case class OrderIdDto(id: String)

object OrderStatus {
  val PLACED = "PLACED"
  val ACTIVATED = "ACTIVATED"
}

