#!/bin/bash

curl --request POST -sL \
     --url 'http://localhost:9000/api/project/add'\
     --data @../data/HelloProject.json
