@echo off
cd ..
call mvnw.cmd -DskipTests -Plocal clean package
pause