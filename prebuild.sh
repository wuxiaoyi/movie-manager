#!/bin/sh
set -e

mv ./src/main/resources/application.yml.example ./src/main/resources/application.yml

mvn clean compile package -Dmaven.test.skip=true
