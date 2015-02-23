package bookstore.ordercontext.resource

import akka.actor.Actor
import bookstore.ordercontext.publishercontract.PublisherContractId
import bookstore.ordercontext.api.RegisterPublisherContractRequest
import bookstore.ordercontext.publishercontract.command.RegisterPublisherContractCommand
import com.typesafe.scalalogging.LazyLogging
import spray.httpx.Json4sSupport
import spray.routing.HttpService

trait ContractCommandResource extends HttpService with Json4sSupport with LazyLogging {
  self: Actor =>

  val contractCommandRoute =
    pathPrefix("publisher-contract-requests") {
      pathEnd {
        post {
          decompressRequest() {
            entity(as[RegisterPublisherContractRequest]) { request =>
              complete {
                logger.info(s"Registering contract: $request")

                val command = RegisterPublisherContractCommand(
                  publisherContractId = PublisherContractId(request.publisherContractId),
                  publisherName = request.publisherName,
                  feePercentage = request.feePercentage,
                  limit = request.limit)

                context.system.eventStream.publish(command)

                request.publisherContractId
              }
            }
          }
        }
      }
    }
}
