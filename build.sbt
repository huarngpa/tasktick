organization in ThisBuild := "app.huarngpa"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.13.0"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.3.3" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.8" % Test
val jwt = "com.pauldijou" %% "jwt-play-json" % "2.1.0"
val jsonx = "ai.x" %% "play-json-extensions" % "0.40.2"

lazy val `tasktick` = (project in file("."))
  .aggregate(`projectmanager-api`, `projectmanager-impl`)

lazy val `projectmanager-api` = (project in file("projectmanager-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `projectmanager-impl` = (project in file("projectmanager-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker,
      lagomScaladslTestKit,
      macwire,
      scalaTest,
      jsonx
    )
  )
  .settings(lagomForkedTestSettings)
  .dependsOn(`projectmanager-api`)
