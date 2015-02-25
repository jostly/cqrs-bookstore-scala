package bookstore.ordercontext.query.service

import akka.actor.ActorSystem
import bookstore.ordercontext.client.productcatalog.ProductCatalogClient
import bookstore.ordercontext.order.{OrderId, ProductId}
import bookstore.ordercontext.publishercontract.PublisherContractId
import bookstore.ordercontext.query.orderlist.OrderProjectionRepository
import com.typesafe.scalalogging.LazyLogging

class QueryService(orderProjectionRepository: OrderProjectionRepository) extends LazyLogging {
  def findPublisherContract(productId: ProductId)(implicit actorSystem: ActorSystem): Option[PublisherContractId] =
    new ProductCatalogClient().
      getProduct(productId).
      map(p => PublisherContractId(p.publisherContractId))

  def getOrders() = {
    val l = orderProjectionRepository.listOrdersByTimestamp
    logger.info("Found orders: " + l)
    l
  }

  def getOrder(orderId: OrderId) =
    orderProjectionRepository.getById(orderId)

}
