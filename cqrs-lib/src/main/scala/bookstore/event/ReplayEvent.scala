package bookstore.event

import bookstore.GenericId

/**
 * Wraps a domain event so we can tell the difference (on the event bus)
 * between live events and replayed ones
 */
case class ReplayEvent(event: DomainEvent[_ <: GenericId])
