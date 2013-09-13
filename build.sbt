name := "h2"

version := "1.0"

scalaVersion := "2.10.2"

libraryDependencies ++= {
  val liftVersion = "2.5"
  val squerylVersion = "0.9.6-SNAPSHOT"
  val luceneVersion = "3.0.3"
  val poiVersion = "3.9"
  Seq(
    "com.yahoo.platform.yui" % "yuicompressor" % "2.4.7",
    "net.liftweb" %% "lift-webkit" % liftVersion withSources(),
    "javax.servlet" % "servlet-api" % "2.5" % "provided->default")
}

transitiveClassifiers := Seq("sources")

resolvers += "Sonatype Snapshots" at " https://oss.sonatype.org/content/groups/public"

resolvers += "Java.net Repo" at "http://download.java.net/maven/2/"

resolvers += "Terracota Repo" at "http://repo.terracotta.org/maven2/"


