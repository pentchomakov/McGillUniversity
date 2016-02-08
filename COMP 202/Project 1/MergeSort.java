import java.util.Arrays;

public class MergeSort {
	
	//Here is a test array. You may use it or change it to test with other examples.
	public static void main(String[] args){
		String[] a = {"apple", "orange", "banana", "pear", "grapefruit"};
		System.out.println(Arrays.toString(a));
		System.out.print(a = mergeSort(a));
		System.out.println(Arrays.toString(a));
	}
	
	/*
	 * This is the recursive sorting method, mergeSort.
	 * Your task is to implement the merge method described below.
	 */
	public static String[] mergeSort(String[] a){
		if(a.length<2)
			return a;
		int middle = a.length/2;
		String[] left = new String[middle];
		String[] right = new String[a.length-middle];
		int i=0;
		for(i=0; i<middle; i++)
			left[i] = a[i];
		for(int j=0; j<right.length; j++){
			right[j] = a[i];
			i++;
		}
		
		left = mergeSort(left);
		right = mergeSort(right);
		String[] result = merge(left,right);
		return result;
	}
	
	/*
	 * This method merges two sorted arrays. They might be of slightly different lengths.
	 * The resulting array should be sorted and should contain all values (including duplicates)
	 * from the original two input arrays.
	 */
	public static String[] merge(String[] l, String[] r)
	{
		String[] MergedArray = new String[l.length+r.length];
		int i = 0;
		int j = 0;
		int k = 0;
		
		//If compareTo returns a value lower than 0, therefore,
		while (i < l.length && j < r.length)
	    {
	        if (l[i].compareTo(r[j])<0) 
	        {
	            MergedArray[k++] = l[i++];
	        }
	        else
	        {
	            MergedArray[k++] = r[j++]; 
	        }
	    }

	    while (i < l.length)  
	    {
	    	MergedArray[k++] = l[i++];
	    }

	    while (j < r.length)  
	    {
	        MergedArray[k++] = r[j++];
	    }   
		return MergedArray;

	}
}
