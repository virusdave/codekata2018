import Dependencies._
import Settings._

lazy val day1 = (project in file("day1"))
  .settings(Settings.settings: _*)
  .settings(Settings.day1Settings: _*)
  .settings(libraryDependencies ++= day1Dependencies.toSeq)

lazy val day2 = (project in file("day2"))
  .settings(Settings.settings: _*)
  .settings(Settings.day2Settings: _*)
  .settings(libraryDependencies ++= day2Dependencies.toSeq)

lazy val all = (project in file("all"))
  .settings(Settings.settings: _*)
  .settings(Settings.allSettings: _*)
  .dependsOn(
    day1,
    day2,
  )
  .configs(Test)

lazy val root = (project in file("."))
  .aggregate(
    day1,
    day2,

    all,
  )
  .settings(libraryDependencies ++= rootDependencies.toSeq)
