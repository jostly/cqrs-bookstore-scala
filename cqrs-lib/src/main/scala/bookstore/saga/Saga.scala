package bookstore.saga

import bookstore.event.DomainEventListener

abstract class Saga extends DomainEventListener(supportsReplay = false) {

}
