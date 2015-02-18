package bookstore.order.application.infrastructure

import bookstore.GenericId
import bookstore.domain.AggregateRoot
import bookstore.event.DomainEvent
import org.scalatest.{FlatSpec, Matchers}

class InMemoryDomainEventStoreTest extends FlatSpec with Matchers {

  case class TestId(id: String) extends GenericId

  case class TestEvent(aggregateId: TestId, version: Int, timestamp: Long) extends DomainEvent[TestId]

  class TestDomainObject extends AggregateRoot[TestId]

  "InMemoryDomainEventStore" should "return events in same order as recorded" in {
    val store = new InMemoryDomainEventStore()

    val foo1 = TestEvent(TestId("foo"), 1, 2)
    val foo2 = TestEvent(TestId("foo"), 2, 4)

    val events = List(foo1, foo2)

    store.save(TestId("foo"), classOf[TestDomainObject], events)

    store.loadEvents(TestId("foo")) should equal (events)

  }

  it should "return only events matching the id" in {
    val store = new InMemoryDomainEventStore()

    val foo1 = TestEvent(TestId("foo"), 1, 2)
    val bar1 = TestEvent(TestId("bar"), 1, 2)

    store.save(TestId("foo"), classOf[TestDomainObject], List(foo1))
    store.save(TestId("bar"), classOf[TestDomainObject], List(bar1))

    store.loadEvents(TestId("foo")) should equal (List(foo1))
  }

  it should "return all events in reversed order of recording" in {

    val store = new InMemoryDomainEventStore()

    val foo1 = TestEvent(TestId("foo"), 1, 2)
    val foo2 = TestEvent(TestId("foo"), 2, 4)
    val bar1 = TestEvent(TestId("bar"), 1, 2)

    store.save(TestId("foo"), classOf[TestDomainObject], List(foo1))
    store.save(TestId("bar"), classOf[TestDomainObject], List(bar1))
    store.save(TestId("foo"), classOf[TestDomainObject], List(foo2))

    store.getAllEvents should equal (List(foo2, bar1, foo1))
  }

}
