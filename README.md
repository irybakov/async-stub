# async-stub

[![Build Status](https://snap-ci.com/irybakov/async-stub/branch/master/build_image)](https://snap-ci.com/irybakov/async-stub/branch/master)

This project is stub of Ping-Pong & Echo  Services. Async and Message Driven.

Service will receive message from RabbitMQ Queue, process it and send response through response exchange.


Part of Infrastructure prototype.

## Quick launch

    docker pull irybakov/async-stub
    
    docker run -d irybakov/async-stub
    

## Build

    Build and push to your own docker hub

### sbt build 
    sbt clean compile dist
    
### docker build
    
    docker build -t async-stub
    docker tag async-stub irybakov/async-stub
    

    
