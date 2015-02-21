package fixture

import bookstore.order.command.api.{LineItemDto, CartDto, PlaceOrderRequest}

trait SomeOrders extends SomeProducts { self: UUIDGenerator =>
  val orderId = nextId()
  val cartId = nextId()

  val orderRequest = PlaceOrderRequest(
    orderId,
    "customer name",
    "customer email",
    "customer address",
    CartDto(
      cartId,
      10000,
      3,
      List(
        LineItemDto(productId1, "title1", 5000, 1, 5000),
        LineItemDto(productId2, "title2", 2500, 2, 5000)
      )
    )
  )

}
