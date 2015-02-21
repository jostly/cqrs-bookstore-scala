package bookstore.productcatalog.resource

import bookstore.productcatalog.api.{BookDto, ProductDto, ProductDtoFactory}
import bookstore.productcatalog.domain.{Book, Product, ProductRepository}
import spray.http.MediaTypes._
import spray.httpx.Json4sSupport
import spray.routing.HttpService

trait ProductResource extends HttpService with Json4sSupport {
  val repository: ProductRepository

  val productRoute = {
    pathPrefix("products") {
      pathEnd {
        get {
          respondWithMediaType(`application/json`) {
            complete {
              repository.getProducts.map(ProductDtoFactory.apply)
            }
          }
        } ~
        post {
          decompressRequest() {
            entity(as[ProductDto]) { productDto =>
              complete {

                val bookDto: BookDto = productDto.book

                val product = Product(productDto.productId,
                  Book(bookDto.bookId, bookDto.isbn, bookDto.title, bookDto.description),
                  productDto.price,
                  productDto.publisherContractId)

                repository.save(product)

                productDto.productId
              }
            }
          }
        }
      } ~
      path(Rest) { productId =>
        get {
          respondWithMediaType(`application/json`) {
            complete {
              repository.getProduct(productId).map(ProductDtoFactory.apply)
            }
          }
        }
      }
    }
  }
}
