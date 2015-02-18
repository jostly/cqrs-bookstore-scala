package bookstore.order.application.infrastructure

import bookstore.GenericId
import bookstore.domain.AggregateRoot
import bookstore.event.{DomainEvent, DomainEventStore}

class InMemoryDomainEventStore extends DomainEventStore {
  var events: List[DomainEvent[_ <: GenericId]] = Nil

  override def loadEvents[ID <: GenericId, DE <: DomainEvent[ID]](id: ID): List[DE] = this.synchronized {
    var loadedEvents: List[DE] = Nil
    for (e <- events) {
      if (e.aggregateId.equals(id)) loadedEvents ::= e.asInstanceOf[DE]
    }

    if (loadedEvents.isEmpty) throw new IllegalArgumentException(s"Aggregate does not exist: $id")

    loadedEvents
  }

  override def save[ID <: GenericId, AR <: AggregateRoot[ID], DE <: DomainEvent[ID]](id: ID, aggregateType: Class[AR], events: List[DE]): Unit = this.synchronized {
    for (e <- events) {
      this.events ::= e
    }
  }

  override def getAllEvents: List[DomainEvent[_ <: GenericId]] = events
}
