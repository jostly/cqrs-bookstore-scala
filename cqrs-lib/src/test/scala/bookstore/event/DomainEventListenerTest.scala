package bookstore.event

import akka.actor.ActorSystem
import akka.testkit.{TestActorRef, TestKit}
import akka.util.Timeout
import bookstore.GenericId
import org.scalatest.{Matchers, FunSuiteLike}
import akka.pattern.ask

import scala.util.Success
import scala.concurrent.duration._


class DomainEventListenerTest extends TestKit(ActorSystem("DomainEventListenerTest"))
  with FunSuiteLike with Matchers {

  val replayListener = TestActorRef(new ReplayListener)
  val nonReplayListener = TestActorRef(new NonReplayListener)
  implicit val timeout: Timeout = 10.milliseconds

  test("a replay listener gets a normal event") {
    val future = replayListener ? TestEvent(null, 1, 2)
    future.value.get should be (Success(TestEvent(null, 1, 2)))
  }

  test("a replay listener gets a replay event") {
    val future = replayListener ? ReplayEvent(TestEvent(null, 3, 4))
    future.value.get should be (Success(TestEvent(null, 3, 4)))
  }

  test("a non-replay listener gets a normal event") {
    val future = nonReplayListener ? TestEvent(null, 1, 2)
    future.value.get should be (Success(TestEvent(null, 1, 2)))
  }

  test("a non-replay listener gets a replay event") {
    val future = nonReplayListener ? ReplayEvent(TestEvent(null, 3, 4))
    future.value should be (None)
  }

}

case class TestEvent(aggregateId: GenericId, timestamp: Long, version: Int) extends DomainEvent[GenericId]

class ReplayListener extends DomainEventListener(supportsReplay = true) {
  override def listen: Receive = {
    case any: DomainEvent[_] => sender ! any
  }
}

class NonReplayListener extends DomainEventListener(supportsReplay = false) {
  override def listen: Receive = {
    case any: DomainEvent[_] => sender ! any
  }
}