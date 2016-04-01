#Makefile for Assignment 03 - edited from lecture slides
#Mitch Myburgh (MYBMIT001)
#2013/03/03
SHELL := /bin/bash
JAVAC = javac
JFLAGS = -g -cp '.:lib/org.bouncycastle.jar:lib/commons-codec-1.10.jar'

SOURCES = client/Client.java server/Server.java keys/KeyChain.java hash/Hash.java

# define general build rule for java sources
.SUFFIXES:  .java  .class

.java.class:
	$(JAVAC)  $(JFLAGS)  $<

#default rule - will be invoked by make

client/Client.class: common/zipFile.class common/Hash.class common/KeyChain.class client/Client.java

server/Server.class: common/zipFile.class common/Hash.class common/KeyChain.class server/Server.java

common/KeyChain.class: common/KeyChain.java

common/Hash.class: common/Hash.java

common/AES.class: common/AES.java

common/zipFile.class: common/zipFile.java

#string substitute .java for .class in SOURCES
#to get dependency class files for def rule
#will force time check on all .class/.java files
 
# explicit rules
clean:
	@rm */*.class

#Run the code
run-client:
	@java -cp '.:lib/org.bouncycastle.jar:lib/commons-codec-1.10.jar' client/Client

run-server:
	@java -cp '.:lib/org.bouncycastle.jar:lib/commons-codec-1.10.jar' server/Server

run-keygen:
	@java -cp '.:lib/org.bouncycastle.jar:lib/commons-codec-1.10.jar' common/KeyChain
