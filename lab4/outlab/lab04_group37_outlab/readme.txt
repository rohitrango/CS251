Group Number 37

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

Citations -
None

----------------------------------------------------------------------------------------
Task A
----------------------------------------------------------------------------------------


git commands:-

git init
git commit -a -m "part 1 done" 				//after task1
git checkout -b euclidean 					//creates branch euclidean
git commit -a -m "Task A2 euclidean dist"   //after 2(i)
git checkout -b Pearson 					//creates branch pearson
git commit -a -m "Task A2 Pearson distance" //after 2(ii)
git checkout master							//switch to master
git merge pearson 							//merges pearson
git branch -d pearson 						//deletes pearson, 2(iii) done
git commit -a -m "added code to find top unseen movies as per pearson coeff" //final

We made some intermediate commits to correct some mistakes.

We used git log to find commit messages and numbers. Commit numbers and commit messages:-

(1) Commit after task 1

commit f80e17c29fdf66e7e992edd4fc416b78276ce5a8
Author: Meet <meet11061997@gmail.com>
Date:   Thu Aug 25 04:15:16 2016 +0530

    part 1 done


(2) Commit after task 2(i)

commit 78ff4ad775621fc2666baf01b83b11667de5d306
Author: Meet <meet11061997@gmail.com>
Date:   Thu Aug 25 19:06:36 2016 +0530

    Task A2 euclidean dist

(3) Commit after task 2(ii)

commit 957299546a54727ac499ae45c352fa9705821d00
Author: Meet <meet11061997@gmail.com>
Date:   Sun Aug 28 00:43:52 2016 +0530

    Task A2 Pearson distance

Why is pearson coeff better?

Pearson coeff is a normalized measure. All the quantities used for defining it are normalized, ie they don't change if we double the data by duplication. Hence pearson coeff does not depend on number of common seen movies.

Whereas number of common seen movies directly affects euclidean distance. Two people with lots of common movies with slight differences in ratings will have more euclidean distance between them then two people with one common seen movie and a large difference in their ratings.

However pearson coeff is not defined when all ratings of either critic are same. This problem does not lie with euclidean dist. So we used MODIFIED ALGORITHM to account for when all the ratings of either critic are same.

Cool features:-

used filter to filter out unseen movies
used map and sum to avoid for loops for 'pythonic' way
used csv module to parse csv files
used lambda functions for writing short functions

NOTE THAT we did not assume any particular order for input of movies. Our code works for any permutaion or even a subset of all movies. Only the movies need to declared earlier in movie-ratings.csv



----------------------------------------------------------------------------------------
Task B
----------------------------------------------------------------------------------------
1.
Commit ID - cbb62682cade12e934c337e334a393374947b010
Name of commit - `Initial commit`   (we had no better name to think of :P )

2.
The definition of `duck typing` is what caught our attention.
Citing it from an unknwon source - `If it quacks like a duck and flies like a duck, then it must be a duck`. So int are supposed to behave like ints, floats like floats and so on.

The sum functions did not work initially when we tried to add real numbers and complex numbers because we had not overloaded the operators to handle arithmetic with int and float (since int and float types do not have attributes 'a' and 'b').
The solution was to take conditional arguments to determine the type of the other argument and then perform the suitable operation. This also enabled us to write a `universal` myexponent() function which will take int, float and ComplexNumber types without any conditions (since we already defined all the arithmetic).

3.
As expected, there was a merge conflict because we had changed the same portions of the same file in both the branches. After fixing the merge conflict in the master branch (which was a piece of cake for us), both the functions worked smoothly.

4.
After typing the command `git merge electricity!`, we got a merge conflict message.
Steps to fix all merge conflicts (will work in general) -
	1. type `git status` to see which files are affected.
	2. Go to all affected files and choose yourself which parts to keep and what to discard.
	3. Add the files for staging using `git add <file1> ...` or `git add .` to add them all.
	4. Type `git commit -m "<Message>"` to save the merge conflict.

The screenshots can be found in the `screenshots` folder inside the repository `taskB`.


Reflection essay -

TaskA - We started out by using a dictionary that maps critic name to list of ratings. But it became inconvenient to access ratings each time using critic name. So we switched to using tuples instead.

TaskB - The task was a fairly simple one. Frankly, we expected more from the task like using rebase, revert, reset, and others. The python tasks were also good. We won't give many citations this time because we are slowly learning to digest the documentation content and help pages and we built these tasks pretty much on our own.
