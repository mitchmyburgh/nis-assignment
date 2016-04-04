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

client/Client.class: server/Server.class common/AES.class common/Zipfile.class common/Hash.class common/KeyChain.class client/Client.java

server/Server.class: common/Zipfile.class common/Hash.class common/KeyChain.class server/Server.java

common/KeyChain.class: common/KeyChain.java

common/Hash.class: common/Hash.java

common/AES.class: common/AES.java

common/Zipfile.class: common/Zipfile.java

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

run-aes:
	@java -cp '.:lib/org.bouncycastle.jar:lib/commons-codec-1.10.jar' common/AES

run-hash:
	@java -cp '.:lib/org.bouncycastle.jar:lib/commons-codec-1.10.jar' common/Hash

run-zipfile:
	@java -cp '.:lib/org.bouncycastle.jar:lib/commons-codec-1.10.jar' common/Zipfile

run-keychain:
	@java -cp '.:lib/org.bouncycastle.jar:lib/commons-codec-1.10.jar' common/KeyChain
