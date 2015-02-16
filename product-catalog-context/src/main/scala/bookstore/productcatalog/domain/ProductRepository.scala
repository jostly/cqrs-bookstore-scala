package bookstore.productcatalog.domain

trait ProductRepository {
  def getProducts: List[Product]

  def getProduct(productId: String): Option[Product]

  def save(product: Product)
}

case class Book(bookId: String, isbn: String, title: String, description: String)

case class Product(productId: String, book: Book, price: Long = 0, publisherContractId: String)

