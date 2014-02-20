#!/bin/bash
MAIN=TestBinaryTreeSet
MAIN2=TestTreapSet
CP="junit-4.11.jar:hamcrest-core-1.3.jar:."
javac -cp $CP *.java
java -ea -cp $CP org.junit.runner.JUnitCore $MAIN
java -ea -cp $CP org.junit.runner.JUnitCore $MAIN2