#include <iostream>
#include <vector>
#include <utility>
#include <algorithm>
#include "utils.hpp"
using namespace std;

class Monster{
	int size;				// an empirical value denoting the size of the monster
	Point facePosition;		// denotes the position of the face w.r.t origin
	double faceRadius;		// represents the radius of the face
	vector<Layer> torso;	// torso is a series of layers of increasing size
	Hand hands[2];			// array containing right and left hands
	Leg legs[2];			// array containing right and left legs

	public:
	Monster(int size);
	void growToSize(int size);		// function to adjust the size of the monster
		
	// member functions to adjust the face and torso
	void resizeTorso();
	void resizeFace();

	// member functions the compute the limb lengths
	double computeArmLength();
	double computeLegLength();
};