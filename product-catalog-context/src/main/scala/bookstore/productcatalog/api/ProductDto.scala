package bookstore.productcatalog.api

import bookstore.productcatalog.domain._

case class ProductDto(productId: String, book: BookDto, price: Long, publisherContractId: String)

case class BookDto(bookId: String, isbn: String, title: String, description: String)

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