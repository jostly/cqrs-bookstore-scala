package bookstore.order.query.orderlist

import bookstore.order.OrderId

trait OrderProjectionRepository {
  def save(orderProjection: OrderProjection)

  def getById(orderId: OrderId): Option[OrderProjection]

  def listOrdersByTimestamp: List[OrderProjection]
}