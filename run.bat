@echo off
:: Game Runner
cd ./runner-publish/
start "" dotnet GameRunner.dll

:: Game Engine
cd ../engine-publish/
timeout /t 1
start "" dotnet Engine.dll

:: Game Logger
cd ../logger-publish/
timeout /t 1
start "" dotnet Logger.dll

:: Bots

cd ../
timeout /t 3
start "" java -jar target/NAN.jar

timeout /t 3
start "" java -jar target/Alternatif1.jar

timeout /t 3
start "" java -jar target/Alternatif2.jar

timeout /t 3
start "" java -jar target/OldBot.jar



cd ../

pause

