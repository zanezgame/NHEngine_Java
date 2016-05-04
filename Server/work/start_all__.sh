#!/bin/bash
ulimit -c unlimited
ulimit -n 65535

this_dir=`dirname $0`
cd $this_dir

##############################################
log_dir="/data/nhlog/start"
mkdir -p $log_dir
log_file=$log_dir/server_start.log
nowTime=$(date +%Y%m%d_%H%M%S)
base_name="NH"
##############################################
echo $nowTime server_starting... >> $log_file


./_start_manage.sh $base_name 700
sleep 3
./_start_world.sh $base_name 001 702
sleep 3
./_start_game.sh $base_name 001 703
sleep 3
./_start_proxy.sh $base_name 001 704
sleep 3
./_start_auth.sh $base_name 701