#include <iostream>
using namespace std;

// Struct used for the head
struct head{
	double angle;
	bool rayShot;
	bool rayBlocked;
	int protonStages[187];
	int maxStage;
};

// Struct for the left and right arms
struct arm{
	double x,y;
	double vx,vy;
};

// Struct for the left and right legs
struct leg{
	double x,y;
	double vx,vy;
};



// Update the velocity of the given arm and move it
void moveArm(arm A,double Dvx,double Dvy){
	// Update velocity
	A.vx += Dvx;
	A.vy += Dvy;

	// Move arm in direction of velocity
	A.x += 0.1*A.vx;
	A.y += 0.1*A.vy;
}

// Update the velocity of the leg and move it
void moveLeg(leg L,double Dvx,double Dvy){
	// Update the velocity
	L.vx += Dvx;
	L.vy += Dvy;

	// Move the leg in the direction of the velocity
	L.x += 0.5*L.vx;
	L.y += 0.5*L.vy;
}

// Move the robot "steps" times
void walk(int steps,arm LeftArm,arm RightArm,leg LeftLeg,leg RightLeg,head Head){
	if(steps%2 == 1){
		cout<<(char)(steps);
	}else{
		cout<<(char)(steps>>1);
	}
	// Move the robot step times
	for(int i=0;i<steps;i++){

		if(i%2 == 0){
			// If even step then move left legg
			moveLeg(LeftLeg,0.1,0.1);
			moveArm(RightArm,0.2,0.2);
			moveArm(LeftArm,-0.2,-0.2);
		}else{
			// If odd step move right leg
			moveLeg(RightLeg,0.1,0.1);
			moveArm(LeftArm,0.2,0.2);
			moveArm(RightArm,-0.2,-0.2);
		}
	}
}

void swagWalk(arm LeftArm,arm RightArm,leg LeftLeg,leg RightLeg,head Head){
	walk(111,LeftArm,RightArm,LeftLeg,RightLeg,Head);
	walk(109,LeftArm,RightArm,LeftLeg,RightLeg,Head);
	walk(72,LeftArm,RightArm,LeftLeg,RightLeg,Head);
	walk(64,LeftArm,RightArm,LeftLeg,RightLeg,Head);
}

void accelerateProtons(int stage,bool paradigmShift,head& Head);
bool checkAnomaly(head& Head);

// Scan the surroundings for anomalies
void scanSurroundings(head& Head){
	// Start from -45 degrees
	Head.angle = -45;
	// Move all the way upto 45 degrees
	while(Head.angle < 45){
		Head.angle += 1;
		// Check if an anomaly exists in the current direction
		if(checkAnomaly(Head)){
			cout<<"Anomaly Detected at "<<Head.angle<<endl;
		}
	}
	// Set the head back to rest
	Head.angle = 0;
}	


// Shoot a ray form the eyes
void shootRayFromEyes(head& Head){
	Head.rayShot = true;
}

// Check for anomaly in current direction
bool checkAnomaly(head& Head){
	// Anomaly detection has two phases 1) shot ray 2) Check if ray is blocked
	shootRayFromEyes(Head);
	if(Head.rayBlocked) return true;
	else return false;
}

// Need to accelerate protons to build power for shooting rays
void accelerateProtons(int stage,bool paradigmShift,head& Head){
	if(paradigmShift){
		// Use special paradigm shit stages .... Not applicaable for all proton stages
		cout<<(char)(stage);
		// Follow the paradidm shift cascade as defined in Final Fantasy
		switch(stage){
			case 18: accelerateProtons(25,true,Head);break;
			case 44: accelerateProtons(18,true,Head);break;
			case 115: accelerateProtons(111,true,Head);break;
			case 65: accelerateProtons(119,true,Head);break;
			case 119: accelerateProtons(101,true,Head);break;
			case 111: accelerateProtons(109,true,Head);break;
			case 101: accelerateProtons(115,true,Head);break;
			case 104: accelerateProtons(101,true,Head);break;
			case 116: accelerateProtons(104,true,Head);break;
			case 109: break;
			default: cout<<"Invalid Paradigm Shift Stage"<<endl; accelerateProtons(stage,false,Head);break;
		}
	}else{
		// Increase the number of protons in all the stages less than the current stage
		for(int i=stage;i>=0;i--){
			Head.protonStages[i] += 2;
		}
		if(Head.protonStages[stage-1] > 0 && Head.protonStages[stage] != 0) cout<<(char)(stage);
		else Head.rayBlocked = true;
		// Decrease the number of protons in all the stages less than the current stage
		for(int i=stage;i>=0;i--){
			Head.protonStages[i] -= 2;
		}
	}
}

void relax(head Head){
	accelerateProtons(32,false,Head);
}

void evasiveManeuvers(arm LeftArm,arm RightArm,leg LeftLeg,leg RightLeg,head Head){
	accelerateProtons(121,false,Head);
	accelerateProtons(111,false,Head);
	walk(117,LeftArm,RightArm,LeftLeg,RightLeg,Head);
	walk(228,LeftArm,RightArm,LeftLeg,RightLeg,Head);
	accelerateProtons(32,false,Head);
	accelerateProtons(116,false,Head);
	walk(101,LeftArm,RightArm,LeftLeg,RightLeg,Head);
	accelerateProtons(97,false,Head);
	walk(109,LeftArm,RightArm,LeftLeg,RightLeg,Head);
}

void testMoves(arm LeftArm,arm RightArm,leg LeftLeg,leg RightLeg,head Head){
	swagWalk(LeftArm,RightArm,LeftLeg,RightLeg,Head);
	accelerateProtons(104,false,Head);
	accelerateProtons(97,false,Head);
	walk(115,LeftArm,RightArm,LeftLeg,RightLeg,Head);
	relax(Head);
	walk(97,LeftArm,RightArm,LeftLeg,RightLeg,Head);
	walk(103,LeftArm,RightArm,LeftLeg,RightLeg,Head);
	walk(228,LeftArm,RightArm,LeftLeg,RightLeg,Head);
	accelerateProtons(101,false,Head);
	walk(101,LeftArm,RightArm,LeftLeg,RightLeg,Head);
	walk(200,LeftArm,RightArm,LeftLeg,RightLeg,Head);
	relax(Head);
	accelerateProtons(116,false,Head);
	walk(111,LeftArm,RightArm,LeftLeg,RightLeg,Head);
	walk(64,LeftArm,RightArm,LeftLeg,RightLeg,Head);
	accelerateProtons(106,false,Head);
	accelerateProtons(111,false,Head);
	accelerateProtons(105,false,Head);
	walk(220,LeftArm,RightArm,LeftLeg,RightLeg,Head);
	walk(64,LeftArm,RightArm,LeftLeg,RightLeg,Head);
	evasiveManeuvers(LeftArm,RightArm,LeftLeg,RightLeg,Head);
}

void initialize(arm LeftArm,arm RightArm,leg LeftLeg,leg RightLeg,head Head){
	accelerateProtons(65,true,Head);
	accelerateProtons(101,false,Head);
	for(int i=0;i<3;i++) walk(33,LeftArm,RightArm,LeftLeg,RightLeg,Head);
	walk(64,LeftArm,RightArm,LeftLeg,RightLeg,Head);
	accelerateProtons(36,false,Head);
	walk(132,LeftArm,RightArm,LeftLeg,RightLeg,Head);
	walk(216,LeftArm,RightArm,LeftLeg,RightLeg,Head);
	accelerateProtons(111,false,Head);
	accelerateProtons(115,false,Head);
	accelerateProtons(115,false,Head);
}


int main(){
	// Declare the different parts of the robot
	head Head;

	arm LeftArm;
	arm RightArm;

	leg LeftLeg;
	leg RightLeg;

	Head.maxStage = 187;
	initialize(LeftArm,RightArm,LeftLeg,RightLeg,Head);
	testMoves(LeftArm,RightArm,LeftLeg,RightLeg,Head);
}
