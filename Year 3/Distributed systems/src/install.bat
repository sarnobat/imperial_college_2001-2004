@echo off

echo Distributed Systems Coursework - Install Utility
echo (c) Arosha Bandara, DoC Imperial College London, Oct 2003
echo.

del  RMIClient.csh RMIServer.csh UDPClient.csh UDPServer.csh Makefile
xcopy /Y s ipts\*.bat .
xcopy /Y scripts\timer.exe .

echo.
echo Done!
echo.