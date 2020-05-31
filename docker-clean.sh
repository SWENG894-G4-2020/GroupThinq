#!/bin/bash 
docker stop $(docker ps -a -q)
docker images purge
docker system purge -a
docker rm $(docker ps -a -q)
docker rmi $(docker images -a -q)
docker volume prune