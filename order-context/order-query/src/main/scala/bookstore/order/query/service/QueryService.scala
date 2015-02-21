package bookstore.order.query.service

import bookstore.order.query.orderlist.OrderProjectionRepository
import com.typesafe.scalalogging.LazyLogging

class QueryService(orderProjectionRepository: OrderProjectionRepository) extends LazyLogging {

  def getOrders() = {
    val l = orderProjectionRepository.listOrdersByTimestamp
    logger.info("Found orders: " + l)
    l
  }

}
