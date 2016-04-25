
lazy val root = (project in file("."))
  .enablePlugins(SbtWeb)
  .settings(
    name := "hello",
    version := "1.0",
    scalaVersion := "2.11.8",
    //unmanagedResourceDirectories in Compile += resourceManaged.value,
    //resourceGenerators in Runtime += compileCoffeeScriptTask.taskValue,
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion,
      "com.typesafe.akka" %% "akka-http-xml-experimental" % akkaVersion,
      "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaVersion
    ),
    (managedClasspath in Runtime) += (packageBin in Assets).value
  )

lazy val compileCoffeeScriptTask = taskKey[Seq[File]]("Compile .coffee files.")
compileCoffeeScriptTask := WebKeys.assets.value :: Nil

lazy val akkaVersion = "2.4.4"

/*
lazy val testModule = {
  Project("testModule", file("testModule"))
    .settings()
}
*/


