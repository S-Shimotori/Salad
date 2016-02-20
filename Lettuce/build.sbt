name := "Lettuce"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq("org.specs2" %% "specs2-core" % "3.7" % "test")
libraryDependencies += "com.tecnoguru" %% "scuby" % "0.2.6"
libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.0"

scalacOptions in Test ++= Seq("-Yrangepos")
