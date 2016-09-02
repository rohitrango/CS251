#include "dependency1.h"
#include "dependency2.h"

int f(int a) {
	return g(a+10);
}