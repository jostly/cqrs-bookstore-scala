package bookstore.domain

import bookstore.GenericId

trait Repository {
  def save[ID <: GenericId, AR <: AggregateRoot[ID]](aggregateRoot: AR)

  def load[ID <: GenericId, AR <: AggregateRoot[ID]](id: ID, aggregateType: Class[AR]): AR
}