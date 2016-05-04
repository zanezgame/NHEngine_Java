#!/bin/bash

backup_dir=/data/backup/db
cd $backup_dir

nowTime=/data/$(date '+%Y%m%d%H%M%S')
data_dir=$backup_dir$nowTime

mkdir $data_dir

mysqldump -h127.0.0.1 -uroot -proot --default-character-set=utf8 --comments=TRUE --no-data=TRUE --add-drop-database=TRUE --add-drop-table=TRUE --single-transaction --databases wind_account | sed 's/AUTO_INCREMENT=[0-9]*\s//g' > $data_dir/wind_account.sql
mysqldump -h127.0.0.1 -uroot -proot --default-character-set=utf8 --comments=TRUE --no-data=TRUE --add-drop-database=TRUE --add-drop-table=TRUE --single-transaction --databases wind_game_001 | sed 's/AUTO_INCREMENT=[0-9]*\s//g' > $data_dir/wind_game.sql
mysqldump -h127.0.0.1 -uroot -proot --default-character-set=utf8 --comments=TRUE --no-data=TRUE --add-drop-database=TRUE --add-drop-table=TRUE --single-transaction --databases wind_log_001 | sed 's/AUTO_INCREMENT=[0-9]*\s//g' > $data_dir/wind_log.sql
mysqldump -h127.0.0.1 -uroot -proot --default-character-set=utf8 --comments=TRUE --no-data=TRUE --add-drop-database=TRUE --add-drop-table=TRUE --single-transaction --databases wind_manage | sed 's/AUTO_INCREMENT=[0-9]*\s//g' > $data_dir/wind_manage.sql
