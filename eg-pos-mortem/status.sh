#!/bin/bash
ussr="$(whoami)"
ps -efu $ussr| grep $ussr| grep java | grep eg-pos-mortem