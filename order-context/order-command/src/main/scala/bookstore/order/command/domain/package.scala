package bookstore.order.command

import bookstore.order.ProductId

package object domain {
  case class CustomerInformation(customerName: String, email: String, address: String)
  case class OrderLine(productId: ProductId, title: String, quantity: Int, unitPrice: Long)
}
