/**
  * Created by S-Shimotori on 2/20/16.
  */

import scalaz._
import Scalaz._

case class DetectedRenaming(
                             rawData: String,
                             projectName: String,
                             directoryPath: String,
                             fileName: String,
                             revisions: (String, String),
                             lines: Option[(Int, Int)],
                             kinds: (Kind, Kind),
                             names: (String, String),
                             types: (String, String),
                             accessModifiers: (String ,String)
                           ) {
}

object DetectedRenaming {
  def apply(rawData: String): Option[DetectedRenaming] = {
    val data = rawData.split(':')
    if (data.length < 4) {
      return None
    }

    val (fileData, nameData, identifierData) = (
      data(0).replaceAll("^/repository/software/FileLevelCommits/", "").split("_DiffLine_"),
      data(2).split("->"),
      data(3).split("->").map { (i) => i.split(';') }
      )
    if (fileData.length < 2 || nameData.length < 2) {
      return None
    }

    //fileData
    val (projectPath, revisions) = (fileData(0), fileData(1).split('-'))
    if (revisions.length < 2) {
      return None
    }
    val projectPathArguments = projectPath.split('/')
    if (projectPathArguments.length < 3) {
      return None
    }
    val projectName = projectPathArguments(0)
    val directoryPath = "/.*/".r.findFirstIn(projectPath).get

    if (projectPath.last == '/') {
      val fileName = ""

      //identifierData
      val lines = None
      val kinds = (Kind.unapply(identifierData(0)(0)), Kind.unapply(identifierData(1)(0))) match {
        case (oldKind, newKind) if oldKind.nonEmpty && newKind.nonEmpty => Option((oldKind.get, newKind.get))
        case _ => None
      }
      if (kinds.isEmpty) {
        return None
      }
      val names = (nameData(0), nameData(1))
      val types = ("", "")
      val accessModifiers = ("", "")

      val result = DetectedRenaming(
        rawData, projectName, directoryPath, fileName, (revisions(0), revisions(1)),
        lines, kinds.get, names, types, accessModifiers
      )
      Option(result)
   } else {
      val fileName = projectPath.split('/').last
      if (revisions(0) == "" || revisions(1) == "") {
        return None
      }

      //identifierData
      if (identifierData.length < 2 || !identifierData.forall(element => element.length >= 5)) {
        return None
      }

      val lines = (identifierData(0)(0).parseInt.toOption, identifierData(1)(0).parseInt.toOption) match {
        case (oldLine, newLine) if oldLine.nonEmpty && newLine.nonEmpty => Option((oldLine.get, newLine.get))
        case _ => None
      }
      if (lines.isEmpty) {
        return None
      }
      val kinds = (Kind.unapply(identifierData(0)(1)), Kind.unapply(identifierData(1)(1))) match {
        case (oldKind, newKind) if oldKind.nonEmpty && newKind.nonEmpty => Option((oldKind.get, newKind.get))
        case _ => None
      }
      if (kinds.isEmpty) {
        return None
      }
      val names = (identifierData(0)(2), identifierData(1)(2))
      val types = (identifierData(0)(3), identifierData(1)(3))
      val accessModifiers = (identifierData(0)(4), identifierData(1)(4))

      val result = DetectedRenaming(
        rawData, projectName, directoryPath, fileName, (revisions(0), revisions(1)),
        lines, kinds.get, names, types, accessModifiers
      )
      Option(result)
    }
  }
}
