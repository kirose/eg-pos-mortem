#!/bin/bash
date
maquina="$(hostname)"
echo HOST: $maquina
ussr="$(whoami)"
echo FIMET Server Stoping..
ps -efu $ussr | grep $ussr | grep eg-pos-mortem | grep java |
for pid in $(ps -efu $ussr | grep $ussr | grep eg-pos-mortem | grep java | awk '{print $2}'); do kill -9 $pid; done
