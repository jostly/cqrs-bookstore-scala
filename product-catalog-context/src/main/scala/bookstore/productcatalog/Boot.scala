package bookstore.productcatalog

import java.io.File

import akka.actor.ActorSystem
import bookstore.productcatalog.application.ProductApplication
import com.typesafe.config.{Config, ConfigFactory}

object Boot extends App {

  private val config = ConfigFactory.load(ConfigFactory.parseFile(new File("application.conf")))

  new ProductApplication(ActorSystem("on-spray-can"), port = config.getInt("product-catalog-context.port"))

}
