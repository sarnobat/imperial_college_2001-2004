#!/bin/csh

echo Distributed Systems Coursework - Install Utility
echo \(c\) Arosha Bandara, DoC Imperial College London, Oct 2003

rm -f RMIClient.bat RMIServer.bat UDPClient.bat UDPServer.bat build.bat timer.exe
cp scrip /Makefile .
cp scripts/*.csh .
dos2unix -o policy
dos2unix -0 *.csh
chmod u+x *.csh

echo Done!
