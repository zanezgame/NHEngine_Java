mysqldump -h127.0.0.1 -uroot -proot --default-character-set=utf8 --comments=TRUE  --create-options=TRUE --add-drop-database=TRUE --add-drop-table=TRUE --single-transaction --databases wind_account   > ./wind_account.sql
mysqldump -h127.0.0.1 -uroot -proot --default-character-set=utf8 --comments=TRUE  --create-options=TRUE --add-drop-database=TRUE --add-drop-table=TRUE --single-transaction --databases wind_game_001  > ./wind_game.sql
mysqldump -h127.0.0.1 -uroot -proot --default-character-set=utf8 --comments=TRUE  --create-options=TRUE --add-drop-database=TRUE --add-drop-table=TRUE --single-transaction --databases wind_log_001  > ./wind_log.sql
mysqldump -h127.0.0.1 -uroot -proot --default-character-set=utf8 --comments=TRUE  --create-options=TRUE --add-drop-database=TRUE --add-drop-table=TRUE --single-transaction --databases wind_manage  > ./wind_manage.sql
mysqldump -h127.0.0.1 -uroot -proot --default-character-set=utf8 --comments=TRUE  --create-options=TRUE --add-drop-database=TRUE --add-drop-table=TRUE --single-transaction --databases wind_gm  > ./wind_gm.sql

PAUSE
