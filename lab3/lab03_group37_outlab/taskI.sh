#!/bin/bash
# check from inside Earth
cd Asia/India/Maharashtra/Mumbai/
WORDS=`cat dictionary.txt`
for i in $WORDS ; do
	response=`yes | unzip -P $i -qq legendaryPokemon.zip &> /dev/null`
	if [ $? = 0 ]; then
		echo $i
	fi
done