name := "lc_play_start"

version := "1.0"

lazy val `lc_play_start` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
//  jdbc,
  cache,
  ws,
  specs2 % Test,
  "mysql" % "mysql-connector-java" % "5.1.18",
  "com.typesafe.play" %% "play-slick" % "1.1.0",
  "com.typesafe.slick" %% "slick" % "3.1.0",
  "org.slf4j" % "slf4j-nop" % "1.7.10"
)

unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")