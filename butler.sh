#!/bin/sh

gradle :butler:build
cp butler/build/libs/butler.jar butler.jar
java -jar butler.jar
