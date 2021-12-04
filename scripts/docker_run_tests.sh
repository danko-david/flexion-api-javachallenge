#!/bin/bash

cd "$(dirname `readlink -f "$0"`)"

cd ../
docker build -t flexion-javachallenge -f ./scripts/docker/Dockerfile .
docker run -e FLEXION_JAVACHALLENGE_DEV_ID -e FLEXION_REST_REPOSITORY_URL flexion-javachallenge

