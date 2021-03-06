package bookstore.domain

import java.lang.reflect.Method

import bookstore.GenericId
import bookstore.event.DomainEvent

abstract class AggregateRoot[T <: GenericId] {

  var id: T = _
  var version: Int = 0
  var timestamp: Long = 0

  private var _uncommittedEvents: List[DomainEvent[T]] = Nil

  def nextVersion(): Int = version + 1

  def now(): Long = System.currentTimeMillis

  def loadFromHistory(history: List[DomainEvent[T]]) {
    for (event <- history) {
      applyChange(event, isNew = false)
    }
  }

  def applyChange(event: DomainEvent[T], isNew: Boolean = true) {
    invokeHandlerMethod(event)
    if (isNew) _uncommittedEvents ::= event
  }

  private def invokeHandlerMethod(event: DomainEvent[T]) {
    try {
      val method: Method = getClass.getDeclaredMethod("handleEvent", event.getClass)
      method.setAccessible(true)
      method.invoke(this, event)
    }
    catch {
      case e: Exception =>
        throw new RuntimeException("Unable to call event handler method for " + event.getClass.getName, e)

    }
  }

  def hasUncommittedEvents: Boolean = {
    _uncommittedEvents.nonEmpty
  }

  def markChangesAsCommitted() {
    _uncommittedEvents = Nil
  }

  def uncommittedEvents() = _uncommittedEvents.reverse
}

