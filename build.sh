#!/bin/bash

rm -f Getafix.jar
mkdir tmp
mkdir tmp/getafix
javac src/getafix/*.java
mv src/getafix/*.class tmp/getafix/
cp Manifest.txt tmp/Manifest.txt
cd tmp
jar cfm Getafix.jar Manifest.txt getafix/*.class
mv Getafix.jar ../
cd ../
rm -rf tmp

