#!/usr/bin/env bash

cd fluent-validator && mvn leanpackage jacoco:report
cd fluent-validator-jsr303 && mvn clean package jacoco:report
cd fluent-validator-spring && mvn clean package jacoco:report


