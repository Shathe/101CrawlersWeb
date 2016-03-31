#!/bin/sh

cd butler
gradle build
cd ..
cp butler/build/libs/butler.jar butler/butler.jar
java -jar butler/butler.jar
