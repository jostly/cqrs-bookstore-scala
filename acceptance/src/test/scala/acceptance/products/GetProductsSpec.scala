package acceptance.products

import fixture.{AbstractAcceptanceTest, SomeProducts}
import spray.http.StatusCodes

import scala.util.{Failure, Try}

class GetProductsSpec extends AbstractAcceptanceTest with SomeProducts {

  feature("Getting products") {
    scenario("Getting existing product") {
      Given(s"a stored product with id $productId1")
      createProduct(product1).status should be (StatusCodes.OK)

      When(s"the product with id $productId1 is fetched")
      val productOption = getProduct(productId1)

      Then("it should be equal to the one posted")
      productOption should be (Some(product1))
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
          e.getMessage should include ("404 Not Found")
        case _ =>
      }

    }

    scenario("Getting all products") {
      Given("some stored products")
      createProduct(product1).status should be (StatusCodes.OK)
      createProduct(product2).status should be (StatusCodes.OK)
      createProduct(product3).status should be (StatusCodes.OK)

      When("all products are fetched")
      val allProducts = getProducts()

      Then("the products should be returned sorted by book title")
      allProducts should be (List(product2, product1, product3))
    }
  }

}
