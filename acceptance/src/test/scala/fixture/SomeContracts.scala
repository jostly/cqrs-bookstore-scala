package fixture

import bookstore.ordercontext.api.RegisterPublisherContractRequest

trait SomeContracts {
  self: UUIDGenerator =>

  val contracts = JsonLoader.load[Array[RegisterPublisherContractRequest]]("fixture/contracts.json")

  val contractIds = contracts.map(_.publisherContractId)

}
