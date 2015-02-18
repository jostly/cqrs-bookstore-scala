package bookstore.event

import bookstore.GenericId

trait DomainEvent[T <: GenericId] {

  val aggregateId: T
  val version: Int
  val timestamp: Long

}
