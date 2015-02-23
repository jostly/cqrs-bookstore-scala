package bookstore.ordercontext.resource

import bookstore.event.DomainEventStore
import bookstore.ordercontext.order.OrderId
import bookstore.ordercontext.query.orderlist.OrderProjection
import bookstore.ordercontext.query.service.QueryService
import com.typesafe.scalalogging.LazyLogging
import spray.http.MediaTypes._
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
            respondWithMediaType(`application/json`) {
              complete {
                logger.info("Fetching orders")
                queryService.getOrders()
              }
            }
          }
        }
      } ~
      path("events") {
        get {
          respondWithMediaType(`application/json`) {
            complete {
              logger.info("Fetching events")
              domainEventStore.getAllEvents.map(event => Array(event.getClass.getSimpleName, event))
            }
          }
        }
      }
    }
}
