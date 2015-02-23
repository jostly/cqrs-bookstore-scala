package bookstore.ordercontext.api

import bookstore.validation._

case class RegisterPublisherContractRequest(publisherContractId: String,
                                            publisherName: String,
                                            feePercentage: Double,
                                            limit: Long) {
  require(uuid(publisherContractId))
  require(nonEmpty(publisherName))
  require(feePercentage > 0)
  require(limit >= 1)
}
