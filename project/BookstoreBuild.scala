import sbt._

object BookstoreBuild extends Build {
  lazy val root = Project(id = "cqrs-bookstore",
    base = file(".")) aggregate(productCatalog)

  lazy val cqrslib = Project(id = "cqrs-lib",
    base = file("cqrs-lib"))

  lazy val productCatalog = Project(id = "product-catalog-context",
    base = file("product-catalog-context")) dependsOn(cqrslib)
}
