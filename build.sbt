val scala3Version = "3.0.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "rubiks-cube",
    version := "0.1.0",
    scalaVersion := scala3Version,
    libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test",
    libraryDependencies += ("org.scala-js" %%% "scalajs-dom" % "1.1.0") cross CrossVersion.for3Use2_13
  )

enablePlugins(ScalaJSPlugin)
scalaJSUseMainModuleInitializer := true
