package fixture

import bookstore.productcatalog.api.{BookDto, ProductDto}

trait SomeProducts {
  self: UUIDGenerator =>

  val productId1 = nextId()
  val productId2 = nextId()
  val productId3 = nextId()

  val bookId1 = nextId()
  val bookId2 = nextId()
  val bookId3 = nextId()

  val publisherContractId1 = nextId()
  val publisherContractId2 = nextId()

  val product1 = ProductDto(
    productId = productId1,
    BookDto(
      bookId = bookId1,
      isbn = "0321125215",
      title = "Domain-Driven Design",
      description = "Tackling complexity in the heart of software"
    ),
    price = 2917,
    publisherContractId = publisherContractId1
  )
  val product2 = ProductDto(
    productId = productId2,
    BookDto(
      bookId = bookId2,
      isbn = "0321616936",
      title = "Agile Testing",
      description = "A Practical Guide for Testers and Agile Teams"
    ),
    price = 2545,
    publisherContractId = publisherContractId1
  )
  val product3 = ProductDto(
    productId = productId3,
    BookDto(
      bookId = bookId2,
      isbn = "1934356085",
      title = "Programming Ruby",
      description = "The Pragmatic Programmer's Guide, Second Edition"
    ),
    price = 2499,
    publisherContractId = publisherContractId2
  )



}
