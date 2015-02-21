package fixture

import bookstore.productcatalog.api.ProductDto
import org.json4s.NoTypeHints
import org.json4s.native.Serialization
import org.json4s.native.Serialization.read

import scala.io.Source

trait SomeProducts {
  self: UUIDGenerator =>

  implicit private[this] val formats = Serialization.formats(NoTypeHints)

  def fromFile(filename: String) = Source.fromURL(getClass.getClassLoader.getResource(filename)).mkString

  val products = read[Array[ProductDto]](fromFile("fixture/products.json"))

  val productIds = products.map(_.productId).array

  val bookIds = products.map(_.book.bookId)

  val publisherContractIds = products.map(_.publisherContractId)
}
