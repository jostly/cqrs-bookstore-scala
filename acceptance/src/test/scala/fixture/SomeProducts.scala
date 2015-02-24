package fixture

import bookstore.productcatalog.api.ProductDto

trait SomeProducts {
  self: UUIDGenerator =>

  val products = JsonLoader.load[Array[ProductDto]]("fixture/products.json")

  val productIds = products.map(_.productId).array

  val bookIds = products.map(_.book.bookId)

  val publisherContractIds = products.map(_.publisherContractId)
}
