package main;

import java.util.Arrays;

public class Sirius {
	
	public static void main (String[] args) {
		int[] intArray = {4, 12, 3, 15, 134, 67, 232, 45};
		int[] sortedArray = {3, 4, 12, 15, 45, 67, 134, 232};
		Sort.mergeSort(intArray, 0, 7);
		if(Arrays.equals(intArray, sortedArray)) {
			System.out.println("Sirius is innocent");
		} else {
			System.out.println("Sirius is a criminal");
		}
	}
}
