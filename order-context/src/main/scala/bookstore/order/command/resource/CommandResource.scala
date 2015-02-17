package bookstore.order.command.resource

import bookstore.order.command.api.PlaceOrderRequest
import com.typesafe.scalalogging.LazyLogging
import spray.httpx.Json4sSupport
import spray.routing.HttpService

trait CommandResource extends HttpService with Json4sSupport with LazyLogging {

  val commandRoute =
  pathPrefix("order-requests") {
    pathEnd {
      post {
        decompressRequest() {
          entity(as[PlaceOrderRequest]) { request =>
            logger.info("Placing customer order: " + request)

            complete("ok: " + request)
          }
        }
      }
    }
  }
}

/*




@POST    def placeOrder(@Valid  placeOrderRequest : PlaceOrderRequest){
logger.info("Placing customer order: " + placeOrderRequest)
  val cart : CartDto = placeOrderRequest.cart
  val placeOrderCommand : PlaceOrderCommand = commandFactory.toCommand(cart, placeOrderRequest)
commandBus.dispatch(placeOrderCommand)
}



 */