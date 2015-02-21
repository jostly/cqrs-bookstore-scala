package acceptance.products

import fixture.{AbstractAcceptanceTest, SomeProducts}
import spray.http.StatusCodes

class CreateProductsSpec extends AbstractAcceptanceTest with SomeProducts {

  feature("Creating products") {
    scenario("New products") {
      Given(s"no stored products with id ${productIds(0)}")

      When(s"a product with id ${productIds(0)} is posted")
      val status = createProduct(products(0)).status

      Then("the service responds with OK")
      status should be (StatusCodes.OK)

      When(s"the product with id ${productIds(0)} is fetched")
      val productOption = getProduct(productIds(0))

      Then("it should be equal to the one posted")
      productOption should be (Some(products(0)))
    }

    scenario("Updating products") {
      Given(s"a stored product with id ${productIds(1)}")
      createProduct(products(0).copy(productId = productIds(1))).status should be (StatusCodes.OK)

      When(s"a different product with id ${productIds(1)} is posted")
      val status = createProduct(products(1)).status

      Then("the service responds with OK")
      status should be (StatusCodes.OK)

      When(s"the product with id ${productIds(1)} is fetched")
      val productOption = getProduct(productIds(1))

      Then("it should be equal to the one posted")
      productOption should be (Some(products(1)))
    }
  }

}
