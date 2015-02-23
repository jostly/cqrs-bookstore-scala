package bookstore.ordercontext.order.domain

import bookstore.domain.AggregateRoot
import bookstore.ordercontext.order.domain.OrderStatus._
import bookstore.ordercontext.order.event.{OrderActivatedEvent, OrderPlacedEvent}
import bookstore.ordercontext.order.{OrderId, ProductId}
import bookstore.ordercontext.order.{CustomerInformation => CustomerInformationTO, OrderLine => OrderLineTO}

sealed trait OrderStatus
object OrderStatus {
  case object Undeclared extends OrderStatus
  case object Placed extends OrderStatus
  case object Activated extends OrderStatus
}

case class CustomerInformation(customerName: String, email: String, address: String)
case class OrderLine(productId: ProductId, title: String, quantity: Int, unitPrice: Long)

class Order extends AggregateRoot[OrderId] {

  private var status: OrderStatus = Undeclared

  private def assertHasNotBeenPlaced() =
    if (status != Undeclared) throw new IllegalStateException("Order has already been placed")

  private def assertMoreThanZeroOrderLines(lines: List[OrderLine]) =
    if (lines.isEmpty) throw new IllegalArgumentException("Cannot place an order without any order lines")

  def toCustomerInformationTO(ci: CustomerInformation) =
    CustomerInformationTO(ci.customerName, ci.email, ci.address)

  def toOrderLineTO(line: OrderLine) =
    OrderLineTO(line.productId, line.title, line.quantity, line.unitPrice)

  def place(orderId: OrderId, customerInformation: CustomerInformation, orderLines: List[OrderLine], totalAmount: Long) {
    assertHasNotBeenPlaced()
    assertMoreThanZeroOrderLines(orderLines)
    applyChange(OrderPlacedEvent(orderId, nextVersion(), now(), toCustomerInformationTO(customerInformation), orderLines.map(toOrderLineTO), totalAmount))
  }

  def activate(): Unit = if (orderIsPlaced) {
    applyChange(new OrderActivatedEvent(id, nextVersion(), now()))
  }

  private def orderIsPlaced: Boolean = status == Placed

  def handleEvent(event: OrderPlacedEvent) {
    this.id = event.aggregateId
    this.version = event.version
    this.timestamp = event.timestamp
    this.status = Placed
  }

  def handleEvent(event: OrderActivatedEvent) {
    this.id = event.aggregateId
    this.version = event.version
    this.timestamp = event.timestamp
    this.status = Activated
  }

  override def toString = {
    s"Order($id, $version, $timestamp, $status)"
  }
}
