package bookstore

package object validation {
  def uuid(s: String) = s != null && s.matches("""^(([0-9a-fA-F]){8}-([0-9a-fA-F]){4}-([0-9a-fA-F]){4}-([0-9a-fA-F]){4}-([0-9a-fA-F]){12})$""")

  def nonEmpty(s: String) = s != null && !s.isEmpty

  def nonEmpty(s: Seq[_]) = s != null && s.nonEmpty
}
