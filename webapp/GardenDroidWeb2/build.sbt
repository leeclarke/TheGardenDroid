import play.Project._

name := """GardenDroidWeb2"""

version := "1.0-SNAPSHOT"


//herokuAppName in Compile := Map(
//  "test" -> "garden-droid-test",
//  "prod"  -> "garden-droid"
//).getOrElse(sys.props("appEnv"), "garden-droid")

//herokuJdkVersion in Compile := "1.8"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  "org.webjars" %% "webjars-play" % "2.2.0",
  "org.webjars" % "bootstrap" % "2.3.1")


playJavaSettings

libraryDependencies += "org.postgresql" % "postgresql" % "9.3-1100-jdbc41"

initialize := {
  val _ = initialize.value
  if (sys.props("java.specification.version") != "1.8")
    sys.error("Java 8 is required for this project.")
}