package bookstore.order.query.api

// TODO: status should be a proper type, but this needs to be split into a Dto then
case class OrderProjection(orderId: OrderId,
                      orderPlacedTimestamp: Long = 0,
                      orderAmount: Long = 0,
                      customerName: String,
                      status: String)

case class OrderId(id: String)

object OrderStatus {
  val PLACED = "PLACED"
  val ACTIVATED = "ACTIVATED"
}

/*



private final   val orderId : OrderId = null


private final   val orderPlacedTimestamp : Long = 0L


private final   val orderAmount : Long = 0L


private final   val customerName : String = null


private final   val orderLines : List[OrderLineProjection] = null


private   var status : OrderStatus = null



 */