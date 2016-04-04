#Makefile for Assignment 03 - edited from lecture slides
#Mitch Myburgh (MYBMIT001)
#2013/03/03
SHELL := /bin/bash
JAVAC = javac
JFLAGS = -g -cp '.:lib/org.bouncycastle.jar:lib/commons-codec-1.10.jar:lib/junit-4.12.jar:./test:./lib/hamcrest-core-1.3.jar'

SOURCES = client/Client.java server/Server.java keys/KeyChain.java hash/Hash.java

# define general build rule for java sources
.SUFFIXES:  .java  .class

.java.class:
	$(JAVAC)  $(JFLAGS)  $<

#default rule - will be invoked by make

client/Client.class: test/TestRunner.class server/Server.class common/AES.class common/Zipfile.class common/Hash.class common/KeyChain.class client/Client.java

server/Server.class: common/Zipfile.class common/Hash.class common/KeyChain.class server/Server.java

common/KeyChain.class: common/KeyChain.java

common/Hash.class: common/Hash.java

common/AES.class: common/AES.java

common/Zipfile.class: common/Zipfile.java

test/TestRunner.class: test/TestSuite.class test/TestRunner.java

test/TestSuite.class: test/TestAES.class test/TestKeyChain.class test/TestHash.class test/TestZipfile.class test/TestSuite.java

test/TestAES.class: test/TestAES.java

test/TestKeyChain.class: test/TestKeyChain.java

test/TestHash.class: test/TestHash.java

test/TestZipfile.class: test/TestZipfile.java

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

run-test:
	@java -cp '.:lib/org.bouncycastle.jar:lib/commons-codec-1.10.jar:lib/junit-4.12.jar:./test:./lib/hamcrest-core-1.3.jar' test/TestRunner
