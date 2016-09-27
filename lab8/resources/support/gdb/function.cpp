#include <iostream>
#include <vector>
using namespace std;



int calc(int arg1,int arg2){	
	int minVal = min(arg1,arg2);
	int total = 0;
	vector<int> A(1);
	for(int i=0;A.size() < minVal;i++){
		total = total + i*i;
		minVal -= 1;
	}
	return total;
}
	
