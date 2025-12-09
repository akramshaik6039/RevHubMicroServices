@echo off
echo Creating databases...
mysql -u root -proot -e "CREATE DATABASE IF NOT EXISTS revhub; CREATE DATABASE IF NOT EXISTS revhub_post; CREATE DATABASE IF NOT EXISTS revhub_follow;"
echo Databases created!
pause
