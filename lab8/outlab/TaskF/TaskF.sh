#!/bin/sh
## TAKING USER+SYS TIME AND PRINTING ORDER
for f in ` find | grep '[0-9]' | sed -r 's/\.\/([0-9]*)/\1/'`; do
	times=$({ time -p ./$f -l >/dev/null; } 2>&1)
	utime=$(echo $times | cut -d' ' -f4 | sed -r 's/\.//')
	stime=$(echo $times | cut -d' ' -f6 | sed -r 's/\.//')
	total=$(echo $utime + $stime | bc)
	arr+=("$total $f")
done
printf '%s\n' "${arr[@]}" | sed -r 's/(^[0-9]{2} )/0\1/' | sort | cut -d' ' -f2

for f in `printf '%s\n' "${arr[@]}" | sed -r 's/(^[0-9]{2} )/0\1/' | sort | cut -d' ' -f2`; do
	./$f
done

## TAKING REAL TIME AND PRINTING ORDER
# for f in ` find | grep '[0-9]' | sed -r 's/\.\/([0-9]*)/\1/'`; do
# 	times=$({ time -p ./$f -l >/dev/null; } 2>&1)
# 	rtime=$(echo $times | cut -d' ' -f2 | sed -r 's/\.//')
# 	arr+=("$rtime $f")
# done
# printf '%s\n' "${arr[@]}" | sed -r 's/(^[0-9]{2} )/0\1/' | sort | cut -d' ' -f2

# for f in `printf '%s\n' "${arr[@]}" | sed -r 's/(^[0-9]{2} )/0\1/' | sort | cut -d' ' -f2`; do
# 	./$f
# done


## IF BACKGROUND PROCESSES ALLOWED AND WE DONT HAVE TO PRINT ORDER
# for f in ` find | grep '[0-9]' | sed -r 's/\.\/([0-9]*)/\1/'`; do
# 	./$f &
# done