package bookstore.validation

import java.lang.reflect.Method

trait Validatable {

  def validate(implicit path: PropertyPath = PropertyPath("")): Unit

  def requireUUID(property: String)(implicit path: PropertyPath): Unit =
    require(
      prop[String](path + property).matches("""^(([0-9a-fA-F]){8}-([0-9a-fA-F]){4}-([0-9a-fA-F]){4}-([0-9a-fA-F]){4}-([0-9a-fA-F]){12})$"""),
      (path + property).property + " is not a valid UUID")

  def requireNonEmptyString(property: String)(implicit path: PropertyPath): Unit =
    require(!prop[String](path + property).isEmpty, (path + property).path + " is empty")

  def requireNonEmptyList(property: String)(implicit path: PropertyPath): Unit =
    require(prop[List[_]](path + property).nonEmpty, (path + property).path + " is empty")

  def requireValid(property: String)(implicit path: PropertyPath): Unit =
    prop[Validatable](path + property).validate(path + property)

  def requireMoreThan(property: String, v: Int)(implicit path: PropertyPath): Unit =
    require(prop[Int](path + property) > v, (path + property).path + " is not greater than " + v)

  def requireMoreThan(property: String, v: Long)(implicit path: PropertyPath): Unit =
    require(prop[Long](path + property) > v, (path + property).path + " is not greater than " + v)

  private[this] def prop[T](path: PropertyPath)(implicit mf: Manifest[T]): T = {
    try {
      val method: Method = getClass.getDeclaredMethod(path.property)
      method.setAccessible(true)
      method.invoke(this) match {
        case t: T => t
        case _ => throw new IllegalArgumentException(path.path + " is not " + mf.runtimeClass.getSimpleName)
      }
    } catch {
      case _: NoSuchMethodException => throw new IllegalArgumentException(path.path + " is not defined for " + getClass.getSimpleName)
    }

  }
}

case class PropertyPath(path: String) {
  def +(next: String) = path match {
    case "" => PropertyPath(next)
    case _ => PropertyPath(path + "." + next)
  }

  def property: String = {
    val a = path.split('.')
    a(a.length-1)
  }
}
