package bookstore.order.command.domain

import bookstore.domain.AggregateRoot
import bookstore.order.event.{OrderActivatedEvent, OrderPlacedEvent}
import bookstore.order.{OrderId, CustomerInformation => CustomerInformationTO, OrderLine => OrderLineTO}

sealed trait OrderStatus

object OrderStatus {
  case object Undeclared extends OrderStatus
  case object Placed extends OrderStatus
  case object Activated extends OrderStatus
}

class Order extends AggregateRoot[OrderId] {
  import OrderStatus._

  private var status: OrderStatus = Undeclared

  private def assertHasNotBeenPlaced() =
    if (status != Undeclared) throw new IllegalStateException("Order has already been placed")

  private def assertMoreThanZeroOrderLines(lines: List[OrderLine]) =
    if (lines.isEmpty) throw new IllegalArgumentException("Cannot place an order without any order lines")

  def toCustomerInformation(ci: CustomerInformation): CustomerInformationTO =
    CustomerInformationTO(ci.customerName, ci.email, ci.address)

  def toOrderLine(line: OrderLine): OrderLineTO =
    OrderLineTO(line.productId, line.title, line.quantity, line.unitPrice)

  def place(orderId: OrderId, customerInformation: CustomerInformation, orderLines: List[OrderLine], totalAmount: Long) {
    assertHasNotBeenPlaced()
    assertMoreThanZeroOrderLines(orderLines)
    applyChange(OrderPlacedEvent(orderId, nextVersion(), now(), toCustomerInformation(customerInformation), orderLines.map(toOrderLine), totalAmount))
  }

  def activate(): Unit = if (orderIsPlaced) {
      applyChange(new OrderActivatedEvent(id, nextVersion(), now()))
    }

  private def orderIsPlaced: Boolean = {
    return status == Placed
  }

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

  override def toString() = {
    s"Order($id, $version, $timestamp, $status)"
  }
}
