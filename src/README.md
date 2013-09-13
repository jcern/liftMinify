
** Minify Resources on the Fly **

To use, you would add the following line to `Boot.scala`:

    ResourceMinify.init()

This relies on the YUI Compressor library, which can be found by adding the following to your sbt config:

    libraryDependencies += "com.yahoo.platform.yui" % "yuicompressor" % "2.4.7"