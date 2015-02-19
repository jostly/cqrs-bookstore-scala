package bookstore.order.query.resource

import bookstore.event.DomainEventStore
import bookstore.order.OrderId
import bookstore.order.query.orderlist.OrderProjection
import bookstore.order.query.service.QueryService
import com.typesafe.scalalogging.LazyLogging
import spray.httpx.Json4sSupport
import spray.routing.HttpService

trait QueryResource extends HttpService with Json4sSupport with LazyLogging {

  val domainEventStore: DomainEventStore
  val queryService: QueryService

  val queryRoute =
    pathPrefix("query") {
      pathPrefix("orders") {
        pathEnd {
          get {
            complete {
              logger.info("Fetching orders")
              queryService.getOrders()
            }
          }
        }
      } ~
      path("events") {
        get {
          complete {
            logger.info("Fetching events")
            domainEventStore.getAllEvents.map(event => Array(event.getClass.getSimpleName, event))
          }
        }
      }
    }
}
