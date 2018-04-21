#!/bin/sh

ANTLR="http://www.antlr.org/download/antlr-4.7.1-complete.jar"

if ! wget $ANTLR; then
	echo "ERROR: Cannot download library ANTLR" >&2
	exit 1
fi
