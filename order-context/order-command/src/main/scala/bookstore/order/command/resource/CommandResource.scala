package bookstore.order.command.resource

import akka.actor.Actor
import bookstore.order.{ProductId, OrderId}
import bookstore.order.command._
import bookstore.order.command.api.PlaceOrderRequest
import bookstore.order.command.domain.{CustomerInformation, OrderLine}
import com.typesafe.scalalogging.LazyLogging
import spray.httpx.Json4sSupport
import spray.routing.HttpService

trait CommandResource extends HttpService with Json4sSupport with LazyLogging { self: Actor =>

  val commandRoute =
    pathPrefix("order-requests") {
      pathEnd {
        post {
          decompressRequest() {
            entity(as[PlaceOrderRequest]) { request =>
              logger.info("Placing customer order: " + request)

              val cart = request.cart

              val command = PlaceOrderCommand(
                OrderId(request.orderId),
                CustomerInformation(
                  request.customerName,
                  request.customerEmail,
                  request.customerAddress
                ),
                cart.lineItems.map(li => OrderLine(
                  ProductId(li.productId),
                  li.title,
                  li.quantity,
                  li.price
                )),
                cart.totalPrice
              )

              logger.debug("Publishing command: " + command)

              context.system.eventStream.publish(command)

              complete("")
            }
          }
        }
      }
    }
}
