package bookstore.ordercontext.publishercontract.command

import bookstore.command.Command
import bookstore.ordercontext.order.ProductId
import bookstore.ordercontext.publishercontract.PublisherContractId

case class RegisterPurchaseCommand(publisherContractId: PublisherContractId,
                                   productId: ProductId,
                                   unitPrice: Long,
                                   quantity: Int) extends Command