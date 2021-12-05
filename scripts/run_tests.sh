#!/bin/bash

cd "$(dirname `readlink -f "$0"`)"
cd ../source/JavaChallenge/
mvn test "$@"

