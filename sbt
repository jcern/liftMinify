if test -f .sbtconfig; then
  . .sbtconfig
fi
#exec /Library/Java/JavaVirtualMachines/jdk1.7.0_07.jdk/Contents/Home/bin/java ${SBT_OPTS} -Xmx1024M -jar sbt-launch.jar "$@"

exec /Library/Java/Home/bin/java ${SBT_OPTS} -XX:MaxPermSize=1024m -Xmx1024M -jar sbt-launch.jar "$@"
