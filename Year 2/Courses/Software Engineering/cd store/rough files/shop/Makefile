CONTEXT	=	/homes/ss401/public_html/shop
DIR     =       $(CONTEXT)/WEB-INF/lib
JARS    =       shop.jar

all:    $(JARS)

clean:
	/bin/rm -f $(JARS) */*.class

install:	$(JARS)
	install -m644 $(JARS) $(DIR)

sh .jar:	shop.class shop.mf
	jar cfm shop.jar shiop.mf shop.class
	chmod 600 shop.jar

shop.class:	shop.java
	javac -d . shop.java
	chmod g-w shop.class

shop.jar:	shop.java
	$(MKBEAN) shop