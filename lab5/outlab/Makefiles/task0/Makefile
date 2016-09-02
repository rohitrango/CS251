CPP_FILES := $(wildcard *.cpp)
OBJ_FILES := $(CPP_FILES:.cpp=.o)
DEPS := $(OBJ_FILES:.o=.d)

.PHONY: all clean

all: out

out: $(OBJ_FILES)
	g++ -MMD -o $@ $^

%.o: %.cpp
	gcc -c -MMD  -o $@ $<

clean: $(OBJ_FILES) $(DEPS) out
	rm -f $^

-include $(DEPS)
