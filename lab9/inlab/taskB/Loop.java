import java.util.Random;


public class Loop implements Runnable{
	
	int innerCell[];
	int cell[];
	int randomNum;
	int i;
	
	Loop(int innerCell[], int cell[], int randomNum, int i)
	{
		this.innerCell = innerCell;
		this.cell = cell;
		this.randomNum = randomNum;
		this.i = i;
	}
	
	public void run()
	{
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
}