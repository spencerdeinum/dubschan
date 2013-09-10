import sbt._
import Keys._
import play.Project._

import net.litola.SassPlugin

object ApplicationBuild extends Build {

  val appName         = "dubschan"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    SassPlugin.sassSettings:_*
    // Add your own project settings here      
  )

}
