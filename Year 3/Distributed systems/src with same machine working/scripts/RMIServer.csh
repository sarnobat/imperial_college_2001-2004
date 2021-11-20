#!/bin/csh

setenv SECPOLICY "file:./policy"
#setenv CODEBASE "http://www.doc.ic.ac.uk/~dc/ p/RMIServer.jar"
# The CODEBASE is only required when
#  1) the registry is not started by the server
# AND
#  2) the RMIServer_Stub.class is not copied into a directory in the classpath of the client

#java -cp rmi-server:rmi-common -Djava.rmi.server.codebase=$CODEBASE -Djava.security.policy=$SECPOLICY RMIServer
java -cp rmi-server:rmi-common -Djava.security.policy=$SECPOLICY RMIServer
