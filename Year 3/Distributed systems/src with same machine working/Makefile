
.PHONY : all
all: server client udp

.PHONY : server
server:
	@echo "Building RMIServer..."; \
	cd rmi-server; \
	javac -g -classpath .:../rmi-common *.java; \
	rmic -classpath .:../rmi-common RMIServer; \
	cp RMIServer_Stub.class ../rmi-common
# Below is an alternative to copying the stub to the client classpath: providing the interface and stub over http
#	jar cf RMIServer.jar RMIServer_Stub.class ../rmi-common/RMIServerI.class; \
#	cp RMIServer.jar ~/public_html/tmp/RMIServer.jar; \
#	chmod 644 ~/public_html/tmp/RMIServer.jar

.PHONY : client
client:
	@echo "Building RMIClient..."; \
	cd rmi-client; \
	javac -g -classpath .:../rmi-common *.java; \

.PHONY : udp
udp:
	@echo "Building UDP Client / Server..."; \
	cd udp; \
	javac -g -classpath . *.java; \


.PHONY : clean
clean:
	rm rmi-common/*.class
	rm rmi-server/*.class
	rm rmi-client/*.class
	rm udp/*.class
