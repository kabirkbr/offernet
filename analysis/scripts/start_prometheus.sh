#!/bin/bash

# launch Prometheus in docker container 
docker run -p 9090:9090 -v $PWD/analysis/configs/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus