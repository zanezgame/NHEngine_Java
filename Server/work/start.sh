#!/bin/bash
ulimit -c unlimited
ulimit -n 65535

this_dir=`dirname $0`
cd $this_dir

##############################################
log_dir="/data/nhlog/start_log"
mkdir -p $log_dir
log_file=$log_dir/$(date +%Y%m%d_%H%M%S).log

##############################################
nohup bash ./start_all__.sh > $log_file 2>&1 &
