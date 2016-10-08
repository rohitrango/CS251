package main;

public class Sort {
	public static void mergeSort(int[] numbers, int low, int high) {
		if(high <= low) return;
		int mid = (low+high)/2;
		mergeSort(numbers, low, mid);
		mergeSort(numbers, mid+1, high);
		merge(numbers, low, mid, high);
	}
	
	public static void merge(int[] numbers, int low, int mid, int high) {
		int size1 = mid-low+1;
		int size2 = high-mid;
		int[] left = new int[size1];
		int[] right = new int[size];
		
		for(int i = low; i <= mid; i++) left[i-low] = numbers[k];
		for(int j = mid+1; j <= high; i++) right[i-mid-1] = numbers[i];
		
		int i = 0, j = 0, k = low;
		while (i < size1 && j < size2) {
			if(left[i] >= right[j]) {
				numbers[k] = left[i];
				j++;
			}
			else {
				numbers[k] = right[j];
				i++;
			}
			k++;
		}
		
		while(i < size1) {
			numbers[k] = left[i];
			i++;
			k++;
		}
		
		while(j < size2) {
			numbers[k] = right[j];
			j++;
			k++;
		}
	}
}
