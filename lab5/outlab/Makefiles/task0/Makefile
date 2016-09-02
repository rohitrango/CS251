CPP_FILES := $(wildcard *.cpp)
OBJ_FILES := $(CPP_FILES:.cpp=.o)
DEPS := $(OBJ_FILES:.o=.d)

.PHONY: all clean

all: out

out: $(OBJ_FILES)
	g++ -MMD -o $@ $^

%.o: %.cpp
	gcc -c -MMD  -o $@ $<

clean:
	rm -f *.o *.d out

-include $(DEPS)
