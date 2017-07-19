import java.util.*;
import java.util.Arrays;

/*
Solution for https://www.hackerrank.com/challenges/sorted-subsegments/problem
*/

public class Solution {
	// variables to keep track of the previously sorted range within the array
	private static int prevStartingIndex = 0;
	private static int prevEndingIndex = 0;

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);

		// read in data
		int arraySize = in.nextInt();
		int numOfQueries = in.nextInt();
		int index = in.nextInt();

		// read in the array
		int[] array = new int[arraySize];
		for (int i = 0; i < arraySize; i++) {
			array[i] = in.nextInt();
		}

		// read in each query and process
		for (int j = 0; j < numOfQueries; j++) {
			// the starting and ending indexes from the query
			int startingIndex = in.nextInt();
			int endingIndex = in.nextInt();
			
			// if this is the first time thru this loop then there is nothing to compare
			// to, so justSort the range and continue...
			if (j == 0) {
				justSort(array, startingIndex, endingIndex);
				continue;
			} 
			
			// otherwise, lets check if the new query falls with in the previous query so 
			// that we can determine if we should use a binary search or just sort
			else {
				// variables to calculate what % of the new range is already sorted
				double amountOfSortedElements = 0;
				double amountOfElements = (endingIndex - startingIndex) + 1;

				// initial values
				int overlappingStartingRange = -1;
				int overlappingEndingRange = -1;

				// There are 6 possible scenarios for comparing the new range to the previous one.
				// 1. both startingIndex and endingIndex is < prevStartingIndex
				// 		in this case, there is no overlap, so justSort
				// 2. both startingIndex and endingIndex is > prevEndingIndex
				// 		in this case, there is no overlap, so justSort
				// 3. startingIndex is > previousStartingIndex and endingIndex < prevEndingIndex
				//      the new range is fully within the previous range, so its completely sorted
				//      there's no need to do anything
				// 4. startingIndex is out of range, but endingIndex is within the range
				//      there are overlapping sorted elements, but the there are also non sorted
				//		elements that are left bound
				// 5. endingIndex is out of range, but startingIndex is within the range
				//      there are overlapping sorted elements, but the there are also non sorted
				//		elements that are right bound
				// 6. startingIndex and endingIndex are both out of range but, the previous range
				//      is fully within the new range. In this case we have elements that are both
				//      left and right bound
				
				// 1. both startingIndex and endingIndex is < prevStartingIndex
				// 		in this case, there is no overlap, so justSort
				if (endingIndex < prevStartingIndex) {
					justSort(array, startingIndex, endingIndex);
					continue;
				}
				
				// 2. both startingIndex and endingIndex is > prevEndingIndex
				// 		in this case, there is no overlap, so justSort
				else if (startingIndex > prevEndingIndex) {
					justSort(array, startingIndex, endingIndex);
					continue;
				}
				
				// 3. startingIndex is > previousStartingIndex and endingIndex < prevEndingIndex
				//      the new range is fully within the previous range, so its completely sorted
				//      there's no need to do anything
				else if (startingIndex > prevStartingIndex && endingIndex < prevEndingIndex) {
					continue;
				} 
				
				// 4. startingIndex is out of range, but endingIndex is within the range
				//      there are overlapping sorted elements, but the there are also non sorted
				//		elements that are left bound
				else if (startingIndex < prevStartingIndex && endingIndex <= prevEndingIndex) {
					overlappingStartingRange = prevStartingIndex;
					overlappingEndingRange = endingIndex;
				} 

				// 5. endingIndex is out of range, but startingIndex is within the range
				//      there are overlapping sorted elements, but the there are also non sorted
				//		elements that are right bound
				else if (startingIndex >= prevStartingIndex && endingIndex > prevEndingIndex) {
					overlappingStartingRange = startingIndex;
					overlappingEndingRange = prevEndingIndex;
				} 

				// 6. startingIndex and endingIndex are both out of range but, the previous range
				//      is fully within the new range. In this case we have elements that are both
				//      left and right bound
				else if (startingIndex < prevStartingIndex && endingIndex > prevEndingIndex) {
					overlappingStartingRange = prevStartingIndex;
					overlappingEndingRange = prevEndingIndex;
				}
				
				// if for whatever reason, none of the conditions above are met then justSort
				// ** this should never happen, but just in case :-) **
				else {
					justSort(array, startingIndex, endingIndex);
					continue;
				}

				// check that the overlapping values were set
				if (overlappingStartingRange > -1 && overlappingEndingRange > -1
						&& overlappingEndingRange != overlappingStartingRange) {
					
					// calculate the percent of elements that are already sorted				
					amountOfSortedElements = (overlappingEndingRange - overlappingStartingRange) + 1;
					
					// if the percent is greater than 88% then use a binary insertion sort algorithm to 
					// add the unsorted elements to the new the range in their correct location
					// ** 88% was decided upon after some trial and error to figure when it's best to use
					// a binary insertion sort versus a regular Arrays.sort(...)
					if ((double) (amountOfSortedElements/amountOfElements) > 0.88) {

						// perform a Binary Insertion Sort on all the indexes that are left bound
						if (overlappingStartingRange > startingIndex) {
							// loop thru all the elements that are left bound starting at the first index where the new and previous 
							// range overlap minus 1, and loop until the left most element (startingIndex)
							for (int m = (overlappingStartingRange - 1); m >= startingIndex; m--) {
								// perform a binary search to the get the index of where to insert this element
								// calculate the BinarySearch parameters
								int bsFromIndex = m + 1;
								int bsToIndex = overlappingEndingRange + 1;
								int bsIndex = Arrays.binarySearch(array, bsFromIndex, bsToIndex, array[m]);
								
								// if the index returned from Arrays.binarySearch is negative, this means that the element was not found
								// in the array, and instead it returns (-(insertion point) - 1).
								// so derive the insertion point from the return value.
								if (bsIndex < 0) {
									bsIndex = (bsIndex + 1) * -1;
								}
								
								// need to subtract 1 from the index because we're shifting everything over to the left
								bsIndex--;
								
								// insert the element
								binaryInsertionSortLeft(array, bsIndex, m);
							}
						}
						
						// perform a Binary Insertion Sort on all the indexes that are right bound
						if (overlappingEndingRange < endingIndex) {
							// loop thru all the elements that are right bound starting at the first index where the new and previous 
							// range overlap plus 1, and loop until the left most element (endingIndex)
							for (int m = (overlappingEndingRange + 1); m <= endingIndex; m++) {
								// perform a binary search to the get the index of where to insert this element
								// calculate the BinarySearch parameters
								int bsFromIndex = overlappingStartingRange;
								int bsToIndex = m;
								int bsIndex = Arrays.binarySearch(array, bsFromIndex, bsToIndex, array[m]);
								
								// if the index returned from Arrays.binarySearch is negative, this means that the element was not found
								// in the array, and instead it returns (-(insertion point) - 1).
								// so derive the insertion point from the return value.
								if (bsIndex < 0) {
									bsIndex = (bsIndex + 1) * -1;									
								}
								
								// insert the element
								binaryInsertionSortRight(array, bsIndex, m);
							}
						}
					}
					else {
						justSort(array, startingIndex, endingIndex);
						continue;
					}
				}
			}
			prevStartingIndex = startingIndex;
			prevEndingIndex = endingIndex;
		}

		System.out.println(array[index]);
		in.close();
	}
	
	public static void justSort(int[] a, int from, int to) {
		Arrays.sort(a, from, to + 1);
		prevStartingIndex = from;
		prevEndingIndex = to;
	}
	
	// i.e. 
	// a = 1 9 [4 7 12 15 17] 13 63
	// newIndex = 3
	// oldIndex = 1
	// a will become:
	//     1 [4 7 9 12 15 17] 13 63
	public static void binaryInsertionSortLeft(int[] a, int newIndex, int oldIndex ) {
		// insert the new value into the correct location and shift everything
		// first check if the value is already in the correct location, if so do nothing
		if (newIndex != oldIndex) {
			// get the element we're inserting
			int tmp = a[oldIndex];
			
			// shift every element to the left from where the new element will be inserted
			for (int n = oldIndex + 1; n < newIndex + 1; n++) {
				a[n - 1] = a[n];
			}
			
			// insert the new element
			a[newIndex] = tmp;
		}
	}
	
	// i.e. 
	// a = 1 9 [4 7 12 15 17] 13 63
	// newIndex = 5
	// oldIndex = 7
	// a will become:
	//     1 9 [4 7 12 13 15 17] 63
	public static void binaryInsertionSortRight(int[] a, int newIndex, int oldIndex) {
		// insert the new value into the correct location and shift everything
		// first check if the value is already in the correct location, if so do nothing
		if (newIndex != oldIndex) {
			
			// get the element we're inserting
			int tmp = a[oldIndex];
			
			// shift every element to the right from where the new element will be inserted
			for (int n = oldIndex - 1; n >= newIndex; n--) {
				a[n + 1] = a[n];
			}
			
			// insert the new element
			a[newIndex] = tmp;
		}
	}
}
