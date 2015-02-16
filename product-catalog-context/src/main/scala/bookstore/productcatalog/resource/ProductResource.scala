package bookstore.productcatalog.resource

import akka.actor.Actor
import bookstore.productcatalog.api.{BookDto, ProductDto, ProductDtoFactory}
import bookstore.productcatalog.domain.{Book, Product, ProductRepository}
import org.json4s.{NoTypeHints, Formats}
import org.json4s.native.Serialization
import spray.http.MediaTypes._
import spray.httpx.Json4sSupport
import spray.routing.HttpService

class ProductResource(repository: ProductRepository) extends Actor with HttpService with Json4sSupport {
  implicit def json4sFormats: Formats = Serialization.formats(NoTypeHints)

  val productRoute = {
    path("products" / Rest) { productId =>
      get {
        respondWithMediaType(`application/json`) {
          complete {
            repository.getProduct(productId).map(ProductDtoFactory.apply)
          }
        }
      }
    } ~
      path("products") {
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

                  ""
                }
              }
            }
          }
      }
  }

  def actorRefFactory = context

  def receive = runRoute(productRoute)

}

