package bookstore.productcatalog.api

import bookstore.productcatalog.domain._
import bookstore.validation._

case class ProductDto(productId: String,
                      book: BookDto,
                      price: Long = 0,
                      publisherContractId: String) {
  require(uuid(productId))
  require(book != null)
  require(price > 0)
  require(uuid(publisherContractId))
}

case class BookDto(bookId: String, isbn: String, title: String, description: String) {
  require(uuid(bookId))
  require(nonEmpty(isbn))
  require(nonEmpty(title))
  require(nonEmpty(description))
}

object ProductDtoFactory {
  def apply(product: Product): ProductDto = {
    val book = product.book
    ProductDto(
      product.productId,
      BookDto(book.bookId, book.isbn, book.title, book.description),
      product.price,
      product.publisherContractId
    )
  }
}