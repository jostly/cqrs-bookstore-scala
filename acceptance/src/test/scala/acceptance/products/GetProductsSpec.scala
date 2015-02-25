package acceptance.products

import fixture.{AbstractAcceptanceTest, SomeProducts}
import spray.http.StatusCodes

import scala.util.{Failure, Try}

class GetProductsSpec extends AbstractAcceptanceTest with SomeProducts {

  feature("Getting products") {
    scenario("Getting existing product") {
      Given(s"a stored product with id ${productIds(0)}")
      createProduct(products(0)).head.status should be (StatusCodes.OK)

      When(s"the product with id ${productIds(0)} is fetched")
      val productOption = getProduct(productIds(0))

      Then("it should be equal to the one posted")
      productOption should be (Some(products(0)))
    }

    scenario("Getting a non-existing product") {
      val nonexist = nextId()
      Given(s"no stored product with id $nonexist")

      When(s"the product with id $nonexist is fetched")
      val p = Try(getProduct(nonexist))

      Then("the reply should be 404 Not Found")
      p shouldBe 'failure
      p match {
        case Failure(e) =>
          println(e)
          e.getMessage should include ("404 Not Found")
        case _ =>
      }

    }

    scenario("Getting all products") {
      Given("some stored products")
      createProduct(products.take(3) : _*).map {_.status}.toSet should be (Set(StatusCodes.OK))

      When("all products are fetched")
      val allProducts = getProducts()

      Then("the products should be returned sorted by book title")
      allProducts should be (List(products(1), products(0), products(2)))
    }
  }

}
