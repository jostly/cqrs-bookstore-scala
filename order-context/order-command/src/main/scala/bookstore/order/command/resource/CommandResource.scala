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
              logger.info("Placing customer order: " + request)

              val command = CommandFactory.toCommand(request)

              logger.debug("Publishing command: " + command)

              context.system.eventStream.publish(command)

              complete("")
            }
          }
        }
      } ~
      path("activations") {
        post {
          decompressRequest() {
            entity(as[OrderActivationRequest]) { request =>
              logger.info("Activating order: " + request)

              val command = CommandFactory.toCommand(request)

              logger.debug("Publishing command: " + command)

              context.system.eventStream.publish(command)

              complete("")
            }
          }
        }
      }
    }
}
