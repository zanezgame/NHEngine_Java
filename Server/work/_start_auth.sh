#!/bin/bash
ulimit -c unlimited
ulimit -n 65535

this_dir=`dirname $0`
cd $this_dir

base_name=$1
area_id="unique"
debug_port=$2

##############################################
server_name="AuthServer"
server_name_id="AuthServer"
log_path=/data/nhlog/game/$area_id
mkdir -p $log_path
##############################################
name="-Dname="$base_name"_"$server_name"_"$area_id
log="-Dlog_name="$server_name" -Dlog_path="$log_path
charset="-Dfile.encoding=utf-8"

javaParam="-XX:+HeapDumpOnOutOfMemoryError -Xmx300m -Xms50m -Xss256k"
javaDebug=""
if [ "$debug_port" != "" ] ;then
	javaDebug="-Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,address="$debug_port",server=y,suspend=n"
fi
javaJar="-server -cp ../jar/*:../jar/jar_third/*:../jar/jar_third/candy/*"
javaMain="nicehu.server.authserver.Main"
##############################################
java $name $log $charset $javaParam $javaDebug $javaJar $javaMain $server_name_id &
