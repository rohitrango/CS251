#include <iostream>
#include <cmath>

// Limbs are stored only in terms of their attributes
class Limb {
	protected:
	double length;			// length of the limb
	double diameter;		// diameter of the limb

	public:
	void updateLength(double l);
	void construct();
};

// Represents the leg of the monster
class Leg : public Limb {
	double kneeHeight;
	public:
	Leg();
	void construct();
};


// Represents the hand of the monster
class Hand : public Limb {
	double elbowPosition;
	public:
	Hand();
	void construct();
};

// struct to represent the points of a layer and points of a face
struct Point {
	double x, y, z;
};

// A layer is a cube centered at origin
class Layer {
	Point vertices[8];

	public:
	Layer();
	Layer(double size);
};