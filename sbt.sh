#!/bin/sh
java -Xss2m -Xms2g -Xmx2g -XX:MaxPermSize=512m -jar project/strap/gruj_vs_sbt-launch-0.13.x.jar "$*"
