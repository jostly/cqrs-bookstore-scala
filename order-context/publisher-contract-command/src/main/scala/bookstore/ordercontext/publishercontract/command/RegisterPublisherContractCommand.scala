package bookstore.ordercontext.publishercontract.command

import bookstore.command.Command
import bookstore.ordercontext.publishercontract.PublisherContractId

case class RegisterPublisherContractCommand(publisherContractId: PublisherContractId,
                                            publisherName: String,
                                            feePercentage: Double,
                                            limit: Long)
  extends Command
