@echo off
cd ..
call java -cp "target\OrzRepacker.jar.original;target\lib\*" orange.wz.OrangeWzApplication
pause