#include <iostream>

using namespace std;

bool preFlightCheck1 = false;
bool preFlightCheck2 = false;
bool finalFlightCheck = false;

void printKey();

void explode(){
	cout<<"          _ ._  _ , _ ._"<<endl;
	cout<<"        (_ ' ( `  )_  .__)"<<endl;
	cout<<"      ( (  (    )   `)  ) _)"<<endl;
	cout<<"     (__ (_   (_ . _) _) ,__)"<<endl;
	cout<<"         `~~`\\ ' . /`~~`"<<endl;
	cout<<"              ;   ;"<<endl;
	cout<<"              /   \\"<<endl;
	cout<<"_____________/_ __ \\_____________"<<endl;
	exit(1);
}

void largeExplosion(){
	cout<<"     _.-^^---....,,--       "<<endl;
	cout<<" _--                  --_  "<<endl;
	cout<<"<                        >)"<<endl;
	cout<<"|                         | "<<endl;
	cout<<" \\._                   _./  "<<endl;
	cout<<"    ```--. . , ; .--'''       "<<endl;
	cout<<"          | |   |             "<<endl;
	cout<<"       .-=||  | |=-.   "<<endl;
	cout<<"       `-=#$%&%$#=-'   "<<endl;
	cout<<"          | ;  :|     "<<endl;
	cout<<" _____.,-#%&$@%#&#~,._____"<<endl;
	exit(2);
}

int main(){
	if(preFlightCheck1 == false) explode();

	if(preFlightCheck2 == false) explode();
 
	if(finalFlightCheck == false) largeExplosion();


	//Dexter dosen't remember what this prints
	printKey();

	return 0;
}

