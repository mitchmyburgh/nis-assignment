#Makefile for Assignment 03 - edited from lecture slides
#Mitch Myburgh (MYBMIT001)
#2013/03/03
SHELL := /bin/bash
JAVAC = javac
JFLAGS = -g -cp '.:lib/org.bouncycastle.jar'

SOURCES = client/Client.java server/Server.java keys/KeyChain.java hash/Hash.java

# define general build rule for java sources
.SUFFIXES:  .java  .class

.java.class:
	$(JAVAC)  $(JFLAGS)  $<

#default rule - will be invoked by make

client/Client.class: common/Hash.class keys/KeyChain.class client/Client.java

server/Server.class: common/Hash.class keys/KeyChain.class server/Server.java

keys/KeyChain.class: keys/KeyChain.java

common/Hash.class: common/Hash.java

common/AES.class: common/AES.java

#string substitute .java for .class in SOURCES
#to get dependency class files for def rule
#will force time check on all .class/.java files
 
# explicit rules
clean:
	@rm */*.class

#Run the code
run-client:
	@java -cp '.:lib/org.bouncycastle.jar' client/Client

run-server:
	@java -cp '.:lib/org.bouncycastle.jar' server/Server
