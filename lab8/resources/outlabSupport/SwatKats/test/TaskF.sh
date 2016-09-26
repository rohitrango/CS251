#!/bin/sh
for f in `find | grep [0-9]`; do
	x=$({ time -p ./$f -l >/dev/null; } 2>&1 | grep real | tr -d a-z[:space:])
	arr+=("$x $f")
done
echo ${arr[@]}
