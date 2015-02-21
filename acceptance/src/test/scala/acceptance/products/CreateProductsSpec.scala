package acceptance.products

import fixture.{AbstractAcceptanceTest, SomeProducts}
import spray.http.StatusCodes

class CreateProductsSpec extends AbstractAcceptanceTest with SomeProducts {

  feature("Creating products") {
    scenario("New products") {
      Given(s"no stored products with id $productId1")

      When(s"a product with id $productId1 is posted")
      val status = createProduct(product1).status

      Then("the service responds with OK")
      status should be (StatusCodes.OK)

      When(s"the product with id $productId1 is fetched")
      val productOption = getProduct(productId1)

      Then("it should be equal to the one posted")
      productOption should be (Some(product1))
    }

    scenario("Updating products") {
      Given(s"a stored product with id $productId2")
      createProduct(product1.copy(productId = productId2)).status should be (StatusCodes.OK)

      When(s"a different product with id $productId2 is posted")
      val status = createProduct(product2).status

      Then("the service responds with OK")
      status should be (StatusCodes.OK)

      When(s"the product with id $productId2 is fetched")
      val productOption = getProduct(productId2)

      Then("it should be equal to the one posted")
      productOption should be (Some(product2))
    }
  }

}
