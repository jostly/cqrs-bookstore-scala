package bookstore.order.query.resource

import bookstore.order.query.api.{OrderStatus, OrderId, OrderProjection}
import com.typesafe.scalalogging.LazyLogging
import spray.httpx.Json4sSupport
import spray.routing.HttpService

trait QueryResource extends HttpService with Json4sSupport with LazyLogging {

  val queryRoute =
    pathPrefix("query") {
      pathPrefix("orders") {
        pathEnd {
          get {
            complete {
              logger.info("Fetching orders")
              List(OrderProjection(OrderId("1"), 0, 10000, "customer name", OrderStatus.PLACED))
            }
          }
        }
      }
    }
}
