Group Number 37
Lab8 (Inlab)

Members - (Meet Taraviya,150050002) (Akash Trehan,150050031) (Rohit Kumar Jena,150050061)

Meet Taraviya, Roll- 150050002
Honor Code-
I shall pledge to follow all the rules and guidelines issued by sir, while attempting the lab.

Akash Trehan, Roll- 150050031
Honor Code-
I pledge by my honor that I have not used unauthorized help in this assignment and have given my complete efforts.

Rohit Kumar Jena, Roll- 150050061
Honor Code-
I pledge to do my lab in a legitimate way, and not to provide or recieve any unauthorized help.

Contributions -
Meet Taraviya - 100%
Akash Trehan - 100%
Rohit Kumar Jena - 100%

TaskA
-------

Funny comment at line no. 36. Do laugh!


TaskC
------
We can see that in most of the labs, maximum ratings for 4 (there was one rating where maximum was 5).
This suggests that a majority of people who rate the individual labs on a 4/5 basis, also like the entire course on the same basis.


TaskD
------
Average time for running mergeSort, bubbleSort and selectionSort 
-----------------------------------------------------------------
For input1.txt

Function 			Time
---------			-----
mergeSort			0m1.351s
bubbleSort			0m1.490s
selectionSort		0m14.282s

For input2.txt

Function 			Time
---------			-----
mergeSort			0m1.480s
bubbleSort 			0m57.084s
selectionSort		0m14.336s

Comparison of algorithms
--------------------------
Using the flat profile of gprof, we found that following results.

For input1.txt

Function 			Time
---------			-----
mergeSort 	 		0.02s (including time for merge)
bubbleSort  		0.00s	
selectionSort		12.79s

As we can see, bubblesort and mergesort take significantly lower times than selectionsort.

For input2.txt

Function 			Time
---------			-----
mergeSort 	 		0.02s
bubbleSort  		37.53s (including time for swap,since its a part of algo)
selectionSort		13.15s

Here, we can see that the time required for bubbleSort increases by a great deal, although bubbleSort and selectionSort are both of O(n^2) complexity. 

Sequence of commands used to get call graph for mergeSort
./mergeSort < input2.txt
gprof mergeSort > mergeSortReport.txt

-------------------------------------------------------------------
Extra credits - Diamonds represent arrows