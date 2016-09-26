all:
	g++ -pg monsterGrow.o utils.cpp monster.cpp
	time ./a.out
	gprof | gprof2dot > graph.dot
	dot -Tpng graph.dot -o graph.png
	display graph.png 

