package bookstore.event

import akka.actor.Actor

abstract class DomainEventListener(supportsReplay: Boolean) extends Actor {

  context.system.eventStream.subscribe(self, classOf[DomainEvent[_]])

  if (supportsReplay) context.system.eventStream.subscribe(self, classOf[ReplayEvent])

  def listen: Receive

  final def replay: Receive = {
    case ReplayEvent(event) if supportsReplay => self forward (event)
  }

  final def receive = replay orElse listen
}
