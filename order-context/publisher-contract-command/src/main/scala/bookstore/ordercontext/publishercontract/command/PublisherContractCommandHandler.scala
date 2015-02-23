package bookstore.ordercontext.publishercontract.command

import akka.actor.Actor
import bookstore.command.Command
import bookstore.domain.Repository
import bookstore.ordercontext.publishercontract.PublisherContractId
import bookstore.ordercontext.publishercontract.domain.PublisherContract
import com.typesafe.scalalogging.LazyLogging

class PublisherContractCommandHandler(repository: Repository) extends Actor with LazyLogging {

  context.system.eventStream.subscribe(self, classOf[Command])

  def receive = {
    case RegisterPublisherContractCommand(id, name, fee, limit) =>
      val contract = new PublisherContract()
      contract.register(id, name, fee, limit)
      repository.save[PublisherContractId, PublisherContract](contract)
  }

}
