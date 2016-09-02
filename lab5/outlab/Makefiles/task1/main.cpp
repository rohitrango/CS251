#include <iostream>
#include <fstream>
#include <ctime>
#include "dependency1.h"
using namespace std;

int main() {
	ofstream out("clean",ios::out);
    time_t t = time(0);   // get time now
    struct tm * now = localtime( & t );
    out<<"I was made at "<<asctime(now);
	cout<<f(10)<<endl;
}
