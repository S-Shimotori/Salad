/**
  * Created by S-Shimotori on 2/20/16.
  */

object Kind {
  case object PACKD extends Kind("PACKD") //package
  case object CD extends Kind("CD")  //class
  case object ID extends Kind("ID")  //interface
  case object EN extends Kind("EN")  //enumeration
  case object COD extends Kind("COD") //constructor
  case object FD extends Kind("FD")  //field
  case object ENC extends Kind("ENC")  //enumeration constant
  case object MD extends Kind("MD") //method
  case object PD extends Kind("PD")  //parameter
  case object LVD extends Kind("LVD")  //local variable

  val values = Array(PACKD, CD, ID, EN, COD, FD, ENC, MD, PD, LVD)
  val types = Array(CD, ID, EN)

  def unapply(s: String): Option[Kind] = values.find(s == _.toString)
}

sealed abstract class Kind(val name: String) {
}
