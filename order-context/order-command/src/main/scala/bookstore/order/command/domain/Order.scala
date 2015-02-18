package bookstore.order.command.domain

import java.lang.reflect.Method
import bookstore.GenericId
import bookstore.domain.AggregateRoot
import bookstore.event.DomainEvent
import bookstore.order.event.OrderPlacedEvent
import bookstore.order.{OrderId, CustomerInformation => CustomerInformationTO, OrderLine => OrderLineTO}

sealed trait OrderStatus

object OrderStatus {
  case object Undeclared extends OrderStatus
  case object Placed extends OrderStatus
  case object Activated extends OrderStatus
}

class Order extends AggregateRoot[OrderId] {
  private var status: OrderStatus = OrderStatus.Undeclared

  private def assertHasNotBeenPlaced() =
    if (status != OrderStatus.Undeclared) throw new IllegalStateException("Order has already been placed")

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

  def handleEvent(event: OrderPlacedEvent) {
    this.id = event.aggregateId
    this.version = event.version
    this.timestamp = event.timestamp
    this.status = OrderStatus.Placed
  }

  override def toString() = {
    s"Order($id, $version, $timestamp, $status)"
  }
}
