package bookstore.order.command.resource

import akka.actor.Actor
import bookstore.order.command._
import bookstore.order.command.api.{OrderActivationRequest, PlaceOrderRequest}
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
              complete {
                logger.info(s"Placing order: $request")

                val command = CommandFactory.toCommand(request)

                context.system.eventStream.publish(command)

                request.orderId
              }
            }
          }
        }
      } ~
      path("activations") {
        post {
          decompressRequest() {
            entity(as[OrderActivationRequest]) { request =>
              complete {
                logger.info(s"Activating order: $request")

                val command = CommandFactory.toCommand(request)

                context.system.eventStream.publish(command)

                request.orderId
              }
            }
          }
        }
      }
    }
}
