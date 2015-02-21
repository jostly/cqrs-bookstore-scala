package bookstore.order.application.infrastructure

import akka.event.EventStream
import bookstore.GenericId
import bookstore.domain.{AggregateRoot, Repository}
import bookstore.event.DomainEventStore

class DefaultRepository(eventBus: EventStream, domainEventStore: DomainEventStore) extends Repository {

  override def save[ID <: GenericId, AR <: AggregateRoot[ID]](aggregateRoot: AR): Unit = {
    if (aggregateRoot.hasUncommittedEvents) {
      val newEvents = aggregateRoot.uncommittedEvents()

      domainEventStore.save(aggregateRoot.id, aggregateRoot.getClass, newEvents)

      newEvents.foreach(eventBus.publish(_))

      aggregateRoot.markChangesAsCommitted()
    }
  }

  override def load[ID <: GenericId, AR <: AggregateRoot[ID]](id: ID, aggregateType: Class[AR]): AR = {
    try {
      val aggregateRoot: AR = aggregateType.newInstance
      aggregateRoot.loadFromHistory(domainEventStore.loadEvents(id))
      aggregateRoot
    }
    catch {
      case iae: IllegalArgumentException =>
        throw new IllegalArgumentException(s"Aggregate of type ${aggregateType.getSimpleName} does not exist, ID: $id", iae)
    }
  }
}
