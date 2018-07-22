def removeSnapshot(str: String): String = if (str.endsWith("-SNAPSHOT")) str.substring(0, str.length - 9) else str
def katLibDependecy(module: String) =
  "net.katsstuff" % s"katlib-$module" % "3.0.0-SNAPSHOT"

lazy val commonSettings = Seq(
  organization := "net.katsstuff",
  version := "0.1",
  scalaVersion := "2.12.6",
  addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.7"),
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding",
    "utf-8",
    "-explaintypes",
    "-feature",
    "-unchecked",
    "-Xcheckinit",
    "-Xlint",
    "-Yno-adapted-args",
    "-Ypartial-unification",
    "-Ywarn-dead-code",
    "-Ywarn-infer-any",
    "-Ywarn-nullary-override",
    "-Ywarn-nullary-unit",
    "-Ywarn-unused",
    "-language:higherKinds"
  ),
  crossPaths := false,
  resolvers += Resolver.sonatypeRepo("snapshots")
)

lazy val snowSponge =
  crossProject(SpongePlatform("5.1.0"), SpongePlatform("6.0.0"), SpongePlatform("7.0.0"))
    .crossType(CrossType.Pure)
    .settings(
      commonSettings,
      name := s"snowwar-sponge${removeSnapshot(spongeApiVersion.value)}",
      assemblyShadeRules in assembly := Seq(
        ShadeRule.rename("cats.**"                     -> "net.katsstuff.katlib.shade.cats.@1").inAll,
        ShadeRule.rename("fastparse.**"                -> "net.katsstuff.katlib.shade.fastparse.@1").inAll,
        ShadeRule.rename("io.circe.**"                 -> "net.katsstuff.katlib.shade.circe.@1").inAll,
        ShadeRule.rename("jawn.**"                     -> "net.katsstuff.katlib.shade.jawn.@1").inAll,
        ShadeRule.rename("machinist.**"                -> "net.katsstuff.katlib.shade.machinist.@1").inAll, //Zap?
        ShadeRule.rename("scala.**"                    -> "net.katsstuff.katlib.shade.scala.@1").inAll,
        ShadeRule.rename("shapeless.**"                -> "net.katsstuff.katlib.shade.shapeless.@1").inAll,
        ShadeRule.rename("sourcecode.**"               -> "net.katsstuff.katlib.shade.sourcecode.@1").inAll,
        ShadeRule.rename("net.katsstuff.scammander.**" -> "net.katsstuff.katlib.shade.scammander.@1").inAll,
        ShadeRule.rename("net.katsstuff.minejson.**"   -> "net.katsstuff.katlib.shade.minejson.@1").inAll,
        ShadeRule.rename("net.katsstuff.typenbt.**"    -> "net.katsstuff.katlib.shade.typenbt.@1").inAll,
        ShadeRule.zap("macrocompat.**").inAll,
      ),
      assemblyJarName := s"${name.value}-assembly-${version.value}.jar",
      spongePluginInfo := spongePluginInfo.value.copy(
        id = "snowwar",
        name = Some("SnowWar"),
        version = Some(s"${version.value}-${removeSnapshot(spongeApiVersion.value)}"),
        authors = Seq("Katrix"),
        dependencies = Set(
          DependencyInfo(LoadOrder.None, "spongeapi", Some(removeSnapshot(spongeApiVersion.value)), optional = false)
        )
      ),
      oreDeploymentKey := (oreDeploymentKey in Scope.Global).?.value.flatten,
      //https://github.com/portable-scala/sbt-crossproject/issues/74
      Seq(Compile, Test).flatMap(inConfig(_) {
        unmanagedResourceDirectories ++= {
          unmanagedSourceDirectories.value
            .map(src => (src / ".." / "resources").getCanonicalFile)
            .filterNot(unmanagedResourceDirectories.value.contains)
            .distinct
        }
      })
    )
    .spongeSettings("5.1.0")(libraryDependencies += katLibDependecy("sponge_sponge5.1"))
    .spongeSettings("6.0.0")(libraryDependencies += katLibDependecy("sponge_sponge6.0"))
    .spongeSettings("7.0.0")(libraryDependencies += katLibDependecy("sponge_sponge7.0"))

lazy val snowSpongeV500 = snowSponge.spongeProject("5.1.0")
lazy val snowSpongeV600 = snowSponge.spongeProject("6.0.0")
lazy val snowSpongeV700 = snowSponge.spongeProject("7.0.0")

lazy val snowRoot = (project in file("."))
  .disablePlugins(AssemblyPlugin)
  .aggregate(snowSpongeV500, snowSpongeV600, snowSpongeV700)
