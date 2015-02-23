package bookstore.ordercontext

import java.io.File

import akka.actor.ActorSystem
import bookstore.ordercontext.application.OrderApplication
import com.typesafe.config.ConfigFactory

object Boot extends App {
  private val config = ConfigFactory.load(ConfigFactory.parseFile(new File("application.conf")))

  new OrderApplication(ActorSystem("order-context"), port = config.getInt("order-context.port")).start()
}
