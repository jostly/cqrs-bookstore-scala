package bookstore.order.application.infrastructure

import bookstore.order.OrderId
import bookstore.order.query.orderlist.{OrderProjection, OrderProjectionRepository}

class InMemoryOrderProjectionRepository extends OrderProjectionRepository {

  private var projections: Map[OrderId, OrderProjection] = Map.empty

  override def save(orderProjection: OrderProjection): Unit =
    projections += ((orderProjection.orderId, orderProjection))

  override def listOrdersByTimestamp: List[OrderProjection] =
    projections.values.toList.sortWith(timestampCompare)

  override def getById(orderId: OrderId): Option[OrderProjection] =
    projections.get(orderId)

  private def timestampCompare(a: OrderProjection, b: OrderProjection): Boolean =
    a.orderPlacedTimestamp < b.orderPlacedTimestamp
}
