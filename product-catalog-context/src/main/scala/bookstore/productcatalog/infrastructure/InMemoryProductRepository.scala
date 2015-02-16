package bookstore.productcatalog.infrastructure

import bookstore.productcatalog.domain.{Product, ProductRepository}

class InMemoryProductRepository extends ProductRepository {

  private var products = Map.empty[String, Product]

  override def getProducts: List[Product] =
    products.values.toList.sortBy(_.book.title)

  override def save(product: Product): Unit = this.synchronized {
    products = products + ((product.productId, product))
  }

  override def getProduct(productId: String): Option[Product] =
    products.get(productId)

  def clear = this.synchronized {
    products = Map.empty
  }

}
