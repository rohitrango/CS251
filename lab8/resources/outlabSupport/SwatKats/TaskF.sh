#!/bin/sh
for f in *; do
	if [[ "$f"==12 ]]; then
		echo "s"
	fi
	x=$({ time -p ./$f -l >/dev/null; } 2>&1 | grep real | tr -d a-z[:space:])
	arr+=("$x $f")
done
echo ${arr[@]}
