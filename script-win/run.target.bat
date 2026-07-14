@echo off
cd ../target
call java -cp "OrzRepacker.jar.original;lib\*" orange.wz.OrangeWzApplication
pause