#! /bin/bash
for file in *; do
	TYPE=`file $file --mime-type | tr -d " " | cut -d ":" -f2`
	if [ $TYPE = "application/gzip" ]; then mv $file $file.tar.gz 
	elif [ $TYPE = "image/jpeg" ]; then	mv $file $file.jpeg 
	elif [ $TYPE = "text/html" ]; then mv $file $file.html
	elif [ $TYPE = "text/x-c" ]; then mv $file $file.cpp
	fi
done
ls | grep --regexp=".cpp$" | tr "\n" " " | xargs g++ -w
./a.out >& masterball 
subl masterball