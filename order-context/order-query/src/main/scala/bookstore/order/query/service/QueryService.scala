package bookstore.order.query.service

import bookstore.order.query.orderlist.OrderProjectionRepository

class QueryService(orderProjectionRepository: OrderProjectionRepository) {

  def getOrders() = orderProjectionRepository.listOrdersByTimestamp

}
