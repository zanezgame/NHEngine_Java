#!/bin/bash
ulimit -c unlimited
ulimit -n 65535

this_dir=`dirname $0`
cd $this_dir

base_name=$1
area_id=$2
server_name=$3

name=""$base_name"_"$server_name"_"$area_id
nowTime=$(date +%Y%m%d_%H%M%S)
jstack_log_dir="/data/windlog/jstack/"$name
mkdir -p $jstack_log_dir

##############################################
/usr/lib/jvm/java/bin/jstack -l $(ps x | grep $name | grep $server_name | grep -v grep | awk '{print $1}') > $jstack_log_dir/$nowTime"_"$server_name".log"
ps x | grep $name | grep $server_name | grep -v grep | awk '{print $1}' | xargs kill -15