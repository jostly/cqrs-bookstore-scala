package bookstore.ordercontext.query.orderlist

import bookstore.ordercontext.order.OrderId

trait OrderProjectionRepository {
  def save(orderProjection: OrderProjection)

  def getById(orderId: OrderId): Option[OrderProjection]

  def listOrdersByTimestamp: List[OrderProjection]
}