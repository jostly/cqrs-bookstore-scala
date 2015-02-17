package bookstore.order

import java.io.File

import akka.actor.ActorSystem
import bookstore.order.application.OrderApplication
import com.typesafe.config.ConfigFactory

object Boot extends App {
  private val config = ConfigFactory.load(ConfigFactory.parseFile(new File("application.conf")))

  new OrderApplication(ActorSystem("order-context"), port = config.getInt("order-context.port"))
}
