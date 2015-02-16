import sbt._

object BookstoreBuild extends Build {
  lazy val root = Project(id = "cqrs-bookstore",
    base = file(".")) aggregate (productCatalogContext, orderContext)

  lazy val cqrslib = Project(id = "cqrs-lib",
    base = file("cqrs-lib"))

  lazy val productCatalogContext = Project(id = "product-catalog-context",
    base = file("product-catalog-context")) dependsOn (cqrslib)

  lazy val orderContext = Project(id = "order-context",
    base = file("order-context")) dependsOn (cqrslib)

}
