package bookstore.ordercontext.client.productcatalog

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
}

