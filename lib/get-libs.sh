#!/bin/sh

ANTLR="http://www.antlr.org/download/antlr-4.7.1-complete.jar"

dir=`echo $0 | rev | cut -d"/" -f2- | rev`
if [ "$dir" != "$0" ]; then
	cd "$dir"
fi

if ! wget $ANTLR; then
	echo "ERROR: Cannot download library ANTLR" >&2
	exit 1
fi
