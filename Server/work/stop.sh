#!/bin/bash
ulimit -c unlimited
ulimit -n 65535

this_dir=`dirname $0`
cd $this_dir

base_name="NH"
group_type="normal"
area_id=$1
server_name=$4

############################################## base info
start_log_dir="/data/nhlog/start"
mkdir -p $start_log_dir
start_log_file=$start_log_dir/server_start.log
nowTime=$(date +%Y%m%d_%H%M%S)

############################################## do
echo $nowTime server_stoping... >> $start_log_file

./_stop_one.sh $base_name  unique AuthServer
./_stop_one.sh $base_name  001 ProxyServer
./_stop_one.sh $base_name  001 GameServer
./_stop_one.sh $base_name  001 WorldServer
./_stop_one.sh $base_name  unique ManageServer