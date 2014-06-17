import sbt._
import Keys._
import play.Project._
import com.github.play2war.plugin._

object ApplicationBuild extends Build {

  val appName         = "calendar"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
	"postgresql" % "postgresql" % "9.1-901-1.jdbc4",
    "commons-io" % "commons-io" % "2.4"
  )

  val main = play.Project(appName, appVersion, appDependencies)
  	.settings(Play2WarPlugin.play2WarSettings: _*)
  	.settings(
	 Play2WarKeys.servletVersion := "3.0",
	 resolvers += "JBoss repository" at "https://repository.jboss.org/nexus/content/repositories/",
	 resolvers += "Scala-Tools Maven2 Snapshots Repository" at "http://scala-tools.org/repo-snapshots"
  )

}
