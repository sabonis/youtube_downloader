
lazy val root = (project in file("."))
  .enablePlugins(SbtWeb, sbtdocker.DockerPlugin, JavaServerAppPackaging)
  .settings(
    name := "hello",
    version := "1.0",
    scalaVersion := "2.11.8",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion,
      "com.typesafe.akka" %% "akka-http-xml-experimental" % akkaVersion,
      "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaVersion
    ),
    unmanagedResourceDirectories in Compile += (WebKeys.public in Assets).value
  )

lazy val akkaVersion = "2.4.4"

dockerfile in docker := {
  val appDir: File = stage.value
  val targetDir = "/app"

  new Dockerfile {
    from("java")
    //run("apt-get install -y curl")
    runRaw("curl https://yt-dl.org/latest/youtube-dl -o /usr/local/bin/youtube-dl")
    runRaw("chmod a+rx /usr/local/bin/youtube-dl")
    expose(8080)
    entryPoint(s"$targetDir/bin/${executableScriptName.value}")
    copy(appDir, targetDir)
  }
}

// "assets" task is run before "run" task
run <<= (run in Compile) dependsOn (WebKeys.assets in Assets)

// same as above
packageBin <<= (packageBin in Compile) dependsOn (WebKeys.assets in Assets)


