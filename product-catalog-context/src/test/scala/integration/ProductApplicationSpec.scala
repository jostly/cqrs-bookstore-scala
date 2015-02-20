package integration

import akka.actor.{Props, ActorSystem}
import akka.testkit.{ImplicitSender, TestKit}
import bookstore.productcatalog.api.{BookDto, ProductDto}
import bookstore.productcatalog.application.ProductApplication
import bookstore.productcatalog.domain.{Book, Product}
import org.json4s.native.Serialization
import org.json4s.{Formats, NoTypeHints}
import org.scalatest._
import spray.client.pipelining._
import spray.http._
import spray.httpx.Json4sSupport

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Try}


class ProductApplicationSpec(_system: ActorSystem) extends TestKit(_system)
with ImplicitSender with Json4sSupport
with FeatureSpecLike with Matchers with GivenWhenThen
with BeforeAndAfterAll with BeforeAndAfterEach {

  import system.dispatcher

  implicit def json4sFormats: Formats = Serialization.formats(NoTypeHints)

  def this() = this(ActorSystem("CreatingProduct"))

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  override def beforeEach {
    app.productRepository.clear
  }

  def postProductToService(product: ProductDto, timeout: Duration = 1.second) = {
    val pipeline: HttpRequest => Future[HttpResponse] = sendReceive
    Await.result(pipeline(Post("http://localhost:8080/products", product)), timeout)
  }

  def getProductList(timeout: Duration = 1.second) = {
    val pipeline: HttpRequest => Future[List[ProductDto]] = sendReceive ~> unmarshal[List[ProductDto]]
    Await.result(pipeline(Get("http://localhost:8080/products")), timeout)
  }

  def getProduct(productId: String, timeout: Duration = 1.second) = {
    val pipeline: HttpRequest => Future[Option[ProductDto]] = sendReceive ~> unmarshal[Option[ProductDto]]
    Await.result(pipeline(Get("http://localhost:8080/products/" + productId)), timeout)
  }

  val app = new ProductApplication(system)

  val product1a = ProductDto(
    productId = "1",
    book = BookDto(bookId = "a", isbn = "b", title = "c", description = "d"),
    price = 550,
    publisherContractId = "4"
  )

  val product1b = ProductDto(
    productId = "1",
    book = BookDto(bookId = "c", isbn = "d", title = "e", description = "f"),
    price = 123,
    publisherContractId = "7"
  )

  val product2 = ProductDto(
    productId = "2",
    book = BookDto(bookId = "a", isbn = "b", title = "b", description = "d"),
    price = 5,
    publisherContractId = "8"
  )

  feature("Creating products") {
    scenario("Creating a product on an empty service") {
      Given("no stored products")

      When("a product is created")
      postProductToService(product1a)

      Then("the service should have only that product listed")
      getProductList() should be (List(product1a))

    }

    scenario("Creating a product with same ID as existing product") {
      Given("one stored product")
      postProductToService(product1a)

      When("a product is created with the same product ID")
      postProductToService(product1b)

      Then("the service should have only that redefined product listed")
      getProductList() should be(List(product1b))
    }

    scenario("Creating two products") {
      Given("a stored product")
      postProductToService(product1a)

      When("a product is created with a different product ID")
      postProductToService(product2)

      Then("the service should list both products")
      getProductList().toSet should be(Set(product2, product1a))
    }
  }

  feature("Fetching single products") {
    scenario("The product exists") {
      Given("a stored product")
      postProductToService(product1a)

      When("the the service is queried for that product ID")
      val p = getProduct("1")

      Then("the reply should contain the stored product")
      p should be (Some(product1a))
    }
    scenario("The product doesn't exist") {
      Given("a stored product")
      postProductToService(product1a)

      When("the service is queried for another product ID")
      val p = Try(getProduct("2"))

      Then("the reply should be empty")
      p shouldBe 'failure
      p match {
        case Failure(e) =>
          e.getMessage should include ("404 Not Found")
        case _ =>
      }
    }
  }

  feature("Fetching product list") {
    scenario("Empty product list") {
      Given("no stored products")

      When("the service is queried for the product list")
      val p = getProductList()

      Then("the reply should be an empty list")
      p should be (List.empty)
    }
    scenario("Single product list") {
      Given("one stored products")
      postProductToService(product1a)

      When("the service is queried for the product list")
      val p = getProductList()

      Then("the reply should contain just the stored product")
      p should be (List(product1a))
    }
    scenario("Multiple product list") {
      Given("two stored products")
      postProductToService(product1a)
      postProductToService(product2)

      When("the service is queried for the product list")
      val p = getProductList()

      Then("the reply should contain both products in book title order")
      p should be (List(product2, product1a))
    }
  }

}
