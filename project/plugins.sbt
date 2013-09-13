// Comment to get more information during initialization
logLevel := Level.Warn

// The Typesafe repository 
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

// Sonatype repository
resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases"

// Use the Play sbt plugin for Play projects
addSbtPlugin("play" % "sbt-plugin" % "2.1.3")

// Auto reload plugin
addSbtPlugin("com.jamesward" %% "play-auto-refresh" % "0.0.4")
