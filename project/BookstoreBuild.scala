import sbt._

object BookstoreBuild extends Build {
  lazy val root = Project(id = "cqrs-bookstore",
    base = file(".")) aggregate (productCatalogContext, orderContext)

  lazy val acceptance = Project(id = "acceptance",
    base = file("acceptance")) dependsOn (productCatalogContext, orderContext)

  lazy val cqrslib = Project(id = "cqrs-lib",
    base = file("cqrs-lib"))

  lazy val productCatalogContext = Project(id = "product-catalog-context",
    base = file("product-catalog-context")) dependsOn (cqrslib)

	lazy val eventbusContract = Project(id = "eventbus-contract",
		base = file("order-context/eventbus-contract")) dependsOn (cqrslib)

	lazy val orderApplication = Project(id = "order-application",
		base = file("order-context/order-application")) aggregate (orderCommand, orderQuery, publisherContractCommand, sagas)

	lazy val orderCommand = Project(id = "order-command",
		base = file("order-context/order-command")) dependsOn (eventbusContract)

	lazy val orderQuery = Project(id = "order-query",
		base = file("order-context/order-query")) dependsOn (eventbusContract)

  lazy val publisherContractCommand = Project(id = "publisher-contract-command",
    base = file("order-context/publisher-contract-command")) dependsOn (eventbusContract)

  lazy val sagas = Project(id = "sagas",
    base = file("order-context/sagas")) dependsOn (publisherContractCommand, orderCommand)

  lazy val orderContext = Project(id = "order-context",
    base = file("order-context")) aggregate (orderApplication)

}
