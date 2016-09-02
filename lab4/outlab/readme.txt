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

TaskA - 

TaskB - The task was a fairly simple one. Frankly, we expected more from the task like using rebase, revert, reset, and others. The python tasks were also good. We won't give many citations this time because we are slowly learning to digest the documentation content and help pages and we built these tasks pretty much on our own. 
