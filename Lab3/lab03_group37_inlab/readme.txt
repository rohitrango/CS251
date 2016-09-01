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

----------------------------------------------------------------------------------
Task A
----------------------------------------------------------------------------------
In the ~/.bashrc file, there is a `shopt` option that the script uses for custom optional behavior. `shopt` is for using default autocompletion. It uses the file -
`/usr/share/bash-completion/bash_completion` 
to show suggestions for autocomplete. 

The part in else contained other file that contains the path to autocomplete file. This is used for custom autocompletion.



----------------------------------------------------------------------------------
Task B
----------------------------------------------------------------------------------
In the /proc directory, we used the following line to find the relevant file. The relevant file name is /proc/meminfo
`ls | grep mem`
We got the meminfo file where we discovered the details. We used the following command to get the desired output.

cat meminfo | grep Mem | tr -d ' ' | cut -d ':' -f2 

----------------------------------------------------------------------------------
Task C
----------------------------------------------------------------------------------
The command used to ssh is -
`ssh atrehan@mars.cse.iitb.ac.in`

Now for getting the GUI in our local, we use the -X flag(for X Window system)

`ssh -X atrehan@mars.cse.iitb.ac.in`
`subl /tmp/lab3_inlab_C3.txt` -> Open the file in sublime text on mars(Also opens Sublime GUI on local machine)
`ps aux | grep subl` -> To check that subl is running on mars


----------------------------------------------------------------------------------
Task D
----------------------------------------------------------------------------------
3. To run infiniteLoop.sh in background -
`sh infiniteLoop.sh &`

We already had an idea about this. We read this through the links posted on Piazza.

4. To get Process ID of this running script -
`ps aux | grep infiniteLoop`

To kill the process -
`kill <Process ID>` (Process ID of the running script)

Through Sir's demonstrations in class, we came to know about the use of `ps`. Using the `man` command we found out the usage for `kill` command, which is very straight-forward.

5. cell.sh was trapping all signals which prevented us from exiting it using any normal key presses of the keyboard. Searching on the internet, we found that SIGKILL cannot be trapped and the process exits anyways. `kill` sends in SIGINT but `kill -9` sends SIGKILL signal.

`kill -9 <Process ID>` (Process ID of running cell.sh script)

----------------------------------------------------------------------------------
Citations
----------------------------------------------------------------------------------
1. ASCII Art - http://patorjk.com/software/taag/#p=display&f=Slant&t=hello%0Aworld

