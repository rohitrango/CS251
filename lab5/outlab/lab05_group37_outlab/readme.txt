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

----------------------------------------------------------------
Task A
----------------------------------------------------------------

Instructions to run (Linux/Mac) :-

0. Put the makefile in the appropriate testing directory(containing all .cpp and .h files) and run using `make` command.

1. Put the CMakeLists.txt file in the appropriate testing directory(containing all .cpp and .h files). Then using `cmake .` and `make` command in the respective order to get the `out` executable.

Instructions to run (Windows) :-

Put CMakeLists.txt along with main.cpp and all its dependencies. Run 'cmake .' in command prompt in that directory. (We could do this because we added the cmake binaries to path at the time of installation.) Open the folder in Visual studio using 'Open Project...'. Click on 'Build solution' in Build menu. Executable 'out.exe' is created in Debug directory. Run it.

NOTE : In our group, everybody had used their free trial of Visual Studio. So we did it on the pc of Sourabh Singh.

----------------------------------------------------------------
Task B
----------------------------------------------------------------
Files changed -

1. callbacks.cpp (edit explained in answers)

2. dominos.cpp (implemented functions declared in dominos.hpp)

3. dominos.hpp (defined structs Magnet,Slider,Ball; declared keyboard,step and mouse_down functions)

(These functions are declared in the parent class. Redeclaring in the dominos_t class overrides them to achieve desired function.)

Instructions to run :-

Replace the cs251_base_code/src/ directory in the inlab code (for Goldberg machine) with the given src directory, and follow similar instructions to compile.

----------------------------------------------------------------
Citations
----------------------------------------------------------------
1. http://www.iforce2d.net/ - For syntax of various commands

----------------------------------------------------------------
Reflection Essay
----------------------------------------------------------------

Task A -
Automating stuff is pretty cool. So this task was very interesting. Inlab had given us a fair idea of how to use make. But Outlab took it one level up. We had to generate dependencies dynamically for improving the efficiency of compilation. That was the part we like the best. Cmake, though pretty interesting and useful from what we read online, had only a very simple question associated to it. But we were amazed on seeing the Makefile it generated and how versatile cmake was - going good across all platforms.

Task B -
The code structure in the inlab was very complicated. We could not think of writing it from scratch. So we started from inlab and started by modifying dominos.cpp. We came to know about use of step function inherited from parent by reading code for testbeds. Testbeds were very useful. We read about detecting collisions from given manual and ibox2d.org. Also, we could easily assign 'q' and 't' keys to respective purposes. But assigning arrow keys was harder. We searched for the code that moves the window on pressing arrow keys and found it as well. Then some time was spent in figuring out how to call it in dominos.cpp. We made some finishing changes and we were done.

