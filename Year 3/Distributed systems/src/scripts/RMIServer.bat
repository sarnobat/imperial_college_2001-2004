@echo off
set SECPOLICY="file:./policy"

REM set CODEBASE="http://www.doc.ic.ac.uk/~dc/tmp/ IServer.jar"
REM The CODEBASE is only required when
REM  1) the registry is not started by the server
REM     AND
REM  2) the RMIServer_Stub.class is not copied into a directory in the classpath of the client

REM java -cp rmi-server;rmi-common -Djava.rmi.server.codebase=%CODEBASE% -Djava.security.policy=%SECPOLICY% RMIServer
java -cp rmi-server;rmi-common -Djava.security.policy=%SECPOLICY% RMIServer
