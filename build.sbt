
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
    unmanagedResourceDirectories in Compile += (WebKeys.public in Assets).value
    //(managedClasspath in Runtime) += (packageBin in Assets).value
  )

lazy val compileCoffeeScriptTask = taskKey[Seq[File]]("Compile .coffee files.")
compileCoffeeScriptTask := WebKeys.assets.value :: Nil

lazy val akkaVersion = "2.4.4"

lazy val someTask = taskKey[Unit]("")
someTask := {
}

// "assets" task is run before "run" task
run <<= (run in Compile) dependsOn (WebKeys.assets in Assets)


