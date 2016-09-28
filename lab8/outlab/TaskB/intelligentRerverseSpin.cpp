#include "includes.h"
#include <iostream>
#include <cstdlib>

using namespace std;
using namespace glm;

class BitBeast{
	public:
	int attack;	// Attack power of the bit beast
	int defense;	// Defense power of the bit beast
	int hp;		// Remaining hit points of bit beast
	int mp;		// Remaining magical points of the bit beast
	vec3 color;	// Bit beast color
	string name;	// Duh...Name of the bit beast
};
	
 
class Bey{
	public:
	BitBeast bitbeast;	// Bit beast associated with the bey
	vec3 location;		// 3d location of the bey
	vec3 roation;		// Rotation axis of the bey
	int rotationSpeed;	// Spead of rotation
	int energy;		// Energy left for rotation
	bool bladesOut;		// Are the awesome blades out?
};


vec4 analyzeSituation(Bey& bey){
	vec4 result((rand()%100)/ 100.0, (rand()%100)/ 100.0, (rand()%100)/ 100.0, (rand()%100)/ 100.0);
	// Do some complex context aware analysis
	// More complex analysis
	// Generate compressed representation of analysis
	return result;
}


int main(){
	srand(0xdeaddeed);
	BitBeast dragoon;
	dragoon.name = "Dragoon";
	dragoon.color = vec3(0.16,0.396,0.686);
	dragoon.attack = 98;
	dragoon.defense = 90;

	//Initialize hp an mp to full
	dragoon.hp = 100;
	dragoon.mp = 100;

	Bey bey;
	bey.bitbeast = dragoon;
	bey.location = vec3(0,0,0);
	// Bey blades rotate about their Y-axis
	bey.roation = vec3(0,1,0);
	bey.energy = 100;
	bey.bladesOut = true;
	


	for(int i=0;i<100;i++){
		vec4 result = analyzeSituation(bey);
		cout<<(char)processSituation(result,result.x + result.y + result.z + result.w,1,1,2);
	}

	return 0;
}
