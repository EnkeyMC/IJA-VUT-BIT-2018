#!/bin/sh

ANTLR="http://www.antlr.org/download/antlr-4.7.1-complete.jar"
JUINT="http://central.maven.org/maven2/junit/junit/4.12/junit-4.12.jar"
HAMCREST="http://central.maven.org/maven2/org/hamcrest/hamcrest-all/1.3/hamcrest-all-1.3.jar"

dir=`echo $0 | rev | cut -d"/" -f2- | rev`
if [ "$dir" != "$0" ]; then
	cd "$dir"
fi

if ! wget $ANTLR; then
	echo "ERROR: Cannot download library ANTLR" >&2
	exit 1
fi

if ! wget $JUNIT; then
	echo "ERROR: Cannot download library JUnit" >&2
	exit 1
fi

if ! wget $HAMCREST; then
	echo "ERROR: Cannot download library Hamcrest" >&2
	exit 1
fi
