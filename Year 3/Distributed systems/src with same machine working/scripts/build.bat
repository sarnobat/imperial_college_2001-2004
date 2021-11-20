@echo off

echo Distributed Systems Coursework - Build Utility
echo (c) Arosha Bandara, DoC Imperial College London, Oct 2003
echo.

if "%1"=="" goto lUsage

REM Select target to be built
if "%1"=="all" goto lBuildStart
if "%1"=="server" goto lBuildServer
if "%1"=="client" goto lBuildClient
if "%1"=="udp" goto lBuildUDP
if "%1"=="clean" goto lClean

REM Missing a Target
	echo Target not recongnised
	goto lEnd

REM Targets
:lBuildStart
:lBuildServer
	echo Building RMI Server ...
	cd rmi-common
	javac -g -classpath . *.java
	cd ..\rmi-server
	javac -g -classpath .;../rmi-common *.java
	rmic -classpath .;../rmi-common RMIServer
	echo Copying RMI Stub Classes ...
	xcopy /Q /Y RMIServer_Stub.class ..\rmi-common
	cd ..
	echo.
	if not "%1"=="all" goto lEnd

:lBuildClient
	echo Building RMIClient...
	cd rmi-common
	javac -g -classpath . *.java
	cd ..\rmi-client
	javac -g -classpath .;../rmi-common *.java
	cd ..
	echo.
	if not "%1"=="all" goto lEnd

:lBuildUDP
	echo Building UDP Client / Server...
	cd udp
	javac -g -classpath . *.java
	cd ..
	echo.
	if not "%1"=="all" goto lEnd

:lBuildEnd
	goto lEnd
	
:lClean
	del rmi-common\*.class
	del rmi-server\*.class
	del rmi-client\*.class
	del udp\*.class
	echo.
	goto :lEnd

REM End Targets


:lUsage
echo Usage: build 'target' (server / client / udp)
goto lEnd



:lEnd
echo Done!
echo.