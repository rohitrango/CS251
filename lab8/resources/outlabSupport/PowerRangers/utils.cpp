#include "utils.hpp"

void Limb::updateLength(double l) {
	length = l;
}

// construtor
Leg::Leg() {
	length = 100;
	construct();
}

// overrided function
void Leg::construct() {
	kneeHeight = length / 2;
	diameter = 0.5 * pow(length, 0.7) + 0.3;
	diameter = diameter / kneeHeight;
}

// construtor
Hand::Hand() {
	this->length = 90;
	construct();
}

// overrided function
void Hand::construct() {
	elbowPosition = length / 2;
	diameter = 0.2 * pow(length, 0.4) + 0.1;
	diameter = diameter * log(elbowPosition);
}

// constructor
Layer::Layer() {
	for(int i=0; i<8; i++) {
		vertices[i].x = 0;
		vertices[i].y = 0;
		vertices[i].z = 0;
	}
}

// constructor with argument
Layer::Layer(double size){
	int multiplier[2] = {-1, 1};
	for(int i=0; i<8; i++) {
		vertices[i].x = size * multiplier[i & 1];
		vertices[i].y = size * multiplier[i & 2];
		vertices[i].z = size * multiplier[i & 4];
	}
}