package bookstore.productcatalog

import akka.actor.ActorSystem
import bookstore.productcatalog.application.ProductApplication

object Boot extends App {

  new ProductApplication(ActorSystem("on-spray-can"))

}
