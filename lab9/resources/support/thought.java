import java.util.Random;
public class thought {
    public static void main(String[] args) {
	int[] cell = new int[10000000];
	int[] innerCell = new int[1000000];
	Random randomObject = new Random(123); 
	// seed DO NOT CHANGE
	for (int i=0; i<1000; i++) {
	    int randomNum=randomObject.nextInt(100000)+1;

	    // inner loop.. you can't skip this  piece of code
	    // Voldemort (er, J K Rowling) has mandated it
	    for (int j = 0; j < 1000000; j++) { 
		Random innerrandomObject = new Random(200);
		int innerrandomNum = innerrandomObject.nextInt(4000)+1;
		innerCell[j] = i%innerrandomNum;
	    }

	    cell[i] = randomNum;
	    if (cell[i] == 3523) { // Voldemort key is 3523
		System.out.println(i);
	    }
	}
    } // end main
} // end class
