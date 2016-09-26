#!/bin/sh
for f in ` find | grep '[0-9]' | sed -r 's/\.\/([0-9]*)/\1/'`; do
	x=$({ time -p ./$f -l >/dev/null; } 2>&1 | grep real | tr -d a-z[:space:])
	arr+=("$x $f")
done
printf '%s\n' "${arr[@]}" | sort | cut -c6-7