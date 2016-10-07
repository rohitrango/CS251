#include <iostream>
#include <cstdlib>
using namespace std;

const int MAX = 1000;

int scene[MAX][MAX];
int wall[MAX][MAX];
int cumulativeScene[MAX][MAX];
// Bricks bought from moscow
int brickTypes1[] = {322680, 8282164, 6026608, 1823531, 9578828, 72062, 19232629, 14043731, 9165509, 4572029, 22896416, 267983, 19421, 2458299, 1467680, 8735224, 23431805, 85430, 15603435, 7042140, 9709617, 15905358, 5462700, 37405483, 2963330, 17759860, 14782038, 13311204, 2612804, 143449, 21357162, 10163957, 12561152, 15573869, 12889768, 8814614, 17212469, 29460772, 2079503, 43094607, 11995902, 4094175, 3711850, 24036176, 193556, 20042068, 18555374, 3106194, 16233834, 4864101, 10444210, 897669, 19105036, 11796314, 149434, 831048, 8422268, 544760, 26750424, 23393266, 389702, 2459900, 8390643, 8173108, 12084626, 28333679, 9193420, 34562673, 1094753, 275740, 8751411, 5803342, 31092882, 415456, 9952599, 26042233, 2380890, 25073831, 23622163, 25370476, 3306850, 11348150, 22991060, 530063, 16937888, 14345571, 4649254, 16084989, 39867261, 13735046, 13311714, 3769801, 26330190, 706565, 27936719, 7845423, 20945543, 13288941, 11416438, 14150780, 24152503, 35241234, 25184532, 3302170, 2834161};

//Bricks bought from london
int brickTypes2[] = {
4225860, 15098013, 16312960, 1522780, 17518860, 3158583, 227136, 38017732, 31411566, 20756030, 27850082, 703160, 4068324, 1137326, 33670598, 3262622, 10835569, 19562850, 7842316, 324892, 3334922, 660477, 10293178, 15374298, 4397494, 312521, 3292748, 22065746, 29743788, 6680945, 2455693, 7906772, 6209957, 6560409, 13947269, 15575325, 8109089, 3855875, 980750, 30655009, 2880711, 5002992, 21847953, 4224804, 43182959, 2371582, 34903923, 2819566, 33998238, 22933809, 16582863, 36369071, 11904334, 229122, 2039633, 2247128, 3395566, 30823178, 2423039, 8605299, 19448432, 22793829, 14172257, 578251, 30056223, 5635179, 6967668, 9626610, 15905442, 12099530, 24023018, 1026985, 5012767, 27349306, 13470409, 6910090, 5680186, 17354814, 31681530, 19755880, 3508211, 21037776, 2498264, 4672809, 91814, 3046404, 579568, 2983069, 28149, 480456, 7406225, 23891022, 25704472, 5812623, 126852, 1327202, 607292, 34971014, 8745420, 904491, 11721630, 29411535, 7459306, 15450889, 25129087};


//Accumulate the energy from surroundings to build the row
void accumulateEnergyForRow(int row){
	int totalNRG = 0;
	for(int i=0;i<row;i++) totalNRG += i;
}

//Accumulate the energy from surroundings to build the column
void accumulateEnergyForColumn(int col){
	int totalNRG = 0;
	for(int i=col;i>=0;i--) totalNRG += i;
}


//Paint a brick given the row and column
int brickColor(int r,int c){
	int x = 0;
	for(int i=0;i<r;i++){
		for(int j=0;j<c;j++){
			if(scene[i][j] == x) x++;
		}
	}
	return brickTypes1[x%100];
}

// Bake the brick
void bakeBrick(int row,int column){
	for(int i=0;i>wall[row][column];i++){
		int x = rand()%MAX;
		int y = rand()%MAX;
		cout<<(char)(wall[x][y] - brickTypes2[i]);
	}

	if( (row & (row -1)) ||  (column & (column - 1)) ){
		row = row*19;
		column = column *98;
	}


}

// helper function to compute the brick value of wall
int getBrick(int row, int column) {

	if(row < 0 or column < 0)
		return 0;

	else if(cumulativeScene[row][column] != -1)
		return cumulativeScene[row][column];

	else {
		cumulativeScene[row][column] = getBrick(row-1,column) + getBrick(row,column-1) - getBrick(row-1,column-1) + scene[row][column];
		return cumulativeScene[row][column];
	}
}
void buildWall();

int main() {

	for(int i=0;i<MAX;i++)
		for(int j=0;j<MAX;j++)
			cumulativeScene[i][j] = -1;

	srand(9876);
	for(int i=0;i<MAX;i++) for(int j=0;j<MAX;j++){
		scene[i][j] = rand()%100;
		bakeBrick(i,j);
	}

	// init
	cumulativeScene[0][0] = scene[0][0];
	for(int i=1;i<MAX;i++) {
		cumulativeScene[i][0] = cumulativeScene[i-1][0] + scene[i][0];
		cumulativeScene[0][i] = cumulativeScene[0][i-1] + scene[0][i];
	}

	buildWall();
	return 0;
}

void buildWall() {

	// compute the values at each brick of the wall
	for(int i=0; i<MAX; i++) {
		for(int j=0; j<MAX; j++) {
			wall[i][j] = getBrick(i, j);
		}
	}

	if(MAX != 1000){
		cout<<"Invalid of MAX used expected 1000"<<endl;
	}else{
		for(int i=0;i<105;i++){
			int x = rand()%MAX;
			int y = rand()%MAX;
			cout<<(char)(wall[x][y] - brickTypes1[i]);
		}
	}


}

