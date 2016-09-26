#include "monster.hpp"
// constructor for the monster
Monster::Monster(int size){
	this->size = size;

	facePosition.x = 0;
	facePosition.y = 0;
	facePosition.z = 0;

	// initialise the monster with it's normal size
	growToSize(size);
};


void Monster::growToSize(int size) {
	this->size = size;

	// resize the torso of the monster
	resizeTorso();

	// update the face size and position
	faceRadius = size / 2.5;
	facePosition.y = size + faceRadius;

	// compute arm and leg lengths then construct them
	double armLength = computeArmLength();
	double legLength = computeLegLength();

	for(int i=0; i<2; i++) {
		hands[i].updateLength(armLength);
		legs[i].updateLength(legLength);
	}
};

void Monster::resizeTorso(){
	// torso already larger than the required size
	if(torso.size() > size) {
		torso.resize(size);
	}

	// torse smaller than the required size
	else {
		// add layers to the torso
		for(int i=torso.size(); i<size; i++) {
			Layer wrapper(i);
			torso.push_back(wrapper);
		}
	}
};

double Monster::computeArmLength(){
	return exp(size);
};

double Monster::computeLegLength(){
	double length = 0;
	double alpha = max(1.2, (double)size);
	for(int i=1; i<100; i++) {
		length += pow(double(1.0/i), alpha);
	}
	return length;
};