import java.util.Random;

public class thought_thread {
    public static void main(String[] args) {
	int[] cell = new int[10000000];
	int[] innerCell = new int[1000000];
	Random randomObject = new Random(123); 
	// seed DO NOT CHANGE
	for (int i=0; i<1000; i++) {
	    int randomNum=randomObject.nextInt(100000)+1;

	    Loop A = new Loop(innerCell, cell, randomNum, i);
	    Thread t;
		
		t = new Thread(A, "TaskB");
		
		t.start();
	}
    } // end main
} // end class