#!/usr/bin/env bash
# clean & build sbt project
sbt clean test

# create jar file
sbt dist

# build docker
docker build -t async-stub .
docker tag -f async-stub:latest irybakov/async-stub
docker push irybakov/async-stub

