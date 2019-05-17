#!/usr/bin/env bash

if [ -f $1 ]; then
	if [ -f Source/output ]; then
		./Source/output $1
	else
		g++ -Wall -Wextra Source/PartB.cpp -o Source/output
		./Source/output $1
	fi
else
	echo "$1 does not exist."
fi
