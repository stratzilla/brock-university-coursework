package assign3;

import static java.lang.Math.floor;
import static java.lang.Math.max;
import java.util.ArrayList;

public class algorithms {
    
    //
    // Heap Sort
    //
    
    /**
     * constructs a heap from input and utilizes heap properties
     * to maintain a max-heap. Divides input into sorted vs. unsorted and
     * eventually decreases the unsorted size
     * @param arr = array to be sorted
     * @return sorted array
     */
    public int[] heapSort(int[] arr) {
        arr = makeHeap(arr); //create max-heap
        int endPoint = arr.length-1; //end is final element in arr
        while (endPoint > 0) { //until sorted
            int tempInt = arr[0]; //init to temp
            arr[0] = arr[endPoint]; //swap
            arr[endPoint] = tempInt; //restore
            endPoint--; //dec end to make new endpoint
            moveDown(arr,0,endPoint); //refactor heap
        }
        return arr;
    }
    
    private int[] makeHeap(int[] arr) {
        int child = (int) floor((arr.length-2)/2); //child property of heap
        while (child >= 0) {
            moveDown(arr,child,arr.length-1); //refactor to make sorted reg.
            child--; //dec child
        }
        return arr;
    }
    
    private void moveDown(int[] arr, int child, int length) {
        int tempVar = child; //init temp
        while ((tempVar * 2 + 1) <= length) { //parent propety
            int leftChild = ((tempVar*2)+1); //subchild of child
            int toSwap = tempVar; //need later to swap; contain child in temp
            if (arr[toSwap] < arr[leftChild]) {
                toSwap = leftChild;
            }
            //if right child bigger, init swap as left
            if (leftChild+1 <= length && arr[toSwap] < arr[leftChild+1]) {
                toSwap = leftChild+1;
            }
            //swap
            if (toSwap != tempVar) {
                int tempInt = arr[tempVar]; //temp
                arr[tempVar] = arr[toSwap]; //swap
                arr[toSwap] = tempInt; //restore
                tempVar = toSwap; //for subsequent children
            } else {
                break; //terminate
            }
        }
    }
    
    //
    // Merge Sort
    //
    
    /**
     * recursively splits arr
     * into child arrays and then sorts and merges
     * @param arr = the array to sort
     * @return sorted array
     */        
    public int[] mergeSort(int[] arr) {
        //base case
        if (arr.length <= 1) {
            return arr;
        }
        int midPoint = arr.length/2; //midpoint
        int[] left = new int[midPoint]; //left-heavy
        int[] right = new int[arr.length-midPoint]; //right-light 
        //insertion into left and right subarrays
        for (int i = 0; i < left.length; i++) {
            left[i] = arr[i];
        }
        for (int j = 0; j < right.length; j++) {
            right[j] = arr[j+midPoint];
        }
        //resplit and sort left
        mergeSort(left);
        //resplit and sort right
        mergeSort(right);       
        //combine sorted left and right
        return merge(left,right,arr);
    }
    
    /**
     * merges subarrays in a sorted order
     * @param left = left subarray
     * @param right = right subarray
     * @param arr = arr reference
     * @return sorted
     */  
    private int[] merge(int[] left, int[] right, int[] arr) {
        //pointers for left + right childs
        int leftPoint = 0;
        int rightPoint = 0;
        int i = 0; //increment this
        //so no OOB errors
        while (leftPoint < left.length && rightPoint < right.length) {
            //if left is smaller than right, insert into array
            if (left[leftPoint] < right[rightPoint]) {
                //also increment left so not indexed again
                arr[i++] = left[leftPoint++];
            //same but right smaller than left
            } else {
                //also increment right so not indexed again
                arr[i++] = right[rightPoint++];
            }
        }
        //any stragglers ensure is in array
        while (leftPoint < left.length) {
            arr[i++] = left[leftPoint++];
        }
        while (rightPoint < right.length) {
            arr[i++] = right[rightPoint++];
        }
        return arr;
    }
    
    //
    // Quick Sort
    //
    
    /**
     * splits array into partitions via pivoting
     * puts elements less than pivot behind it and vice-versa
     * and realigns pivot to proper place and picks another pivot, etc
     * @param arr = array to be sorted
     * @return sorted array
     */        
    public int[] quickSort(int[] arr) {
        return quickSort(arr, 0, arr.length-1); //chained
    }
    
    //iterative solution
    private int[] quickSort(int[] arr, int left, int right) {
        if (right > left) {
            int pivot = arr[right]; //pivot is last element
            int leftPoint = left; //leftmost
            int rightPoint = right; //rightmost
            //while the two ends are apart
            while (leftPoint <= rightPoint) {
                while (arr[leftPoint] < pivot) {
                    //move point closer to pivot
                    leftPoint++;
                }
                while (arr[rightPoint] > pivot) {
                    //same
                    rightPoint--;
                }
                //when as close as possible
                if (leftPoint <= rightPoint) {
                    int tempInt = arr[leftPoint]; //place into temp
                    arr[leftPoint] = arr[rightPoint]; //swap
                    arr[rightPoint] = tempInt; //restore
                    leftPoint++; rightPoint--;
                }
            }
            //redo with elements left of pivot
            if (left < rightPoint) {
                quickSort(arr,left,rightPoint);
            //same but right of pivot
            } else if (right > leftPoint) {
                quickSort(arr,leftPoint,right);
            }
        }
        return arr;
    }
    
    //
    // Radix Sort
    //
    
    /**
     *
     * @param arr = array to be sorted
     * @return sorted array
     */        
    public int[] radixSort(int[] arr) {
        int maxDigits = 0;
        for (int i = 0; i < arr.length; i++) {
            //determine "longest" number, ie. one with most sig.figs.
            maxDigits = max(String.valueOf(arr[i]).length(),maxDigits);
        }
        //exp (0) means start from 1st sig.fig.
        return radixSort(arr, 0, maxDigits); //chained
    }
    
    private int[] radixSort(int[] arr, int exp, int maxDigits) {
        //base case, if reaches higher sig.fig.
        if (exp >= maxDigits) {
            return arr;
        }     
        //create string array to put in expanded numbers
        String[] stringArr = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            //a little janky, but turn, say, 10 into 0010 (with maxdig = 4)
            stringArr[i] = String.format("%0"+maxDigits+"d",arr[i]);
        }
        //initialize ten buckets for 0-9
        ArrayList<Integer>[] buckets = new ArrayList[10];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new ArrayList<Integer>();
        }
        //attempt to fill each bucket by indexing each array element
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < stringArr.length; j++) {
                //compare the value of the given sig.fig.
                //eg. 12 -> 012 -> _1_ for 2nd exp
                char c = stringArr[j].charAt(maxDigits-exp-1);
                //if it matches bucket, insert to bucket
                if (Character.getNumericValue(c) == i) {
                    buckets[i].add(Integer.parseInt(stringArr[j]));
                }
            }
        }
        int k = 0;
        int[] tempArr = new int[arr.length];
        //put back into array sorted by sig.fig and resort based on next sig.fig
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < buckets[i].size(); j++) {
                tempArr[k] = buckets[i].get(j);
                k++;
            }
            buckets[i].clear(); //necessary?
        }
        //redo with partially sorted
        return radixSort(tempArr,exp+1,maxDigits);
    }
    
    //
    // Bubble Sort
    //
    
    /**
     * sequentially goes through array and swaps element i and i+1
     * until fully sorted. will run until no more swaps are done
     * @param arr = array to be sorted
     * @return sorted array
     */        
    public int[] bubbleSort(int[] arr) {
        return bubbleSort(arr, true); //chained
    }
    
    private int[] bubbleSort(int[] arr, boolean pass) {
        //if no swap detected
        if (!pass) {
            return arr;
        }
        pass = false; //make swap false
        int temp;
        //iterate over entire array
        for (int i = 0; i < arr.length-1; i++) {
            //if true, then swap was made
            if (arr[i] > arr[i+1]) {
                pass = true; //make true
                temp = arr[i]; //put into placeholder
                arr[i] = arr[i+1]; //swap
                arr[i+1] = temp; //restore
            }
        }
        //recurisvely call until sorted
        return bubbleSort(arr,pass);
    }
    
    //
    // Printing Sorted Arrays
    //
    
    public void printHeap(int[] arr) {
        arr = heapSort(arr);
        String theArray = "";
        for (int i = 0; i < arr.length; i++) {
            theArray = theArray + arr[i];
            if (i != arr.length-1) {
                theArray = theArray + ", ";
            }
        }
        System.out.print("\nHere are the inputted integers sorted using Heapsort:\n");
        System.out.print("{"+theArray+"}\n");
    }
    
    public void printMerge(int[] arr) {
        arr = mergeSort(arr);
        String theArray = "";
        for (int i = 0; i < arr.length; i++) {
            theArray = theArray + arr[i];
            if (i != arr.length-1) {
                theArray = theArray + ", ";
            }
        }
        System.out.print("\nHere are the inputted integers sorted using Mergesort:\n");
        System.out.print("{"+theArray+"}\n");
    }
    
    public void printQuick(int[] arr) {
        arr = quickSort(arr);
        String theArray = "";
        for (int i = 0; i < arr.length; i++) {
            theArray = theArray + arr[i];
            if (i != arr.length-1) {
                theArray = theArray + ", ";
            }
        }
        System.out.print("\nHere are the inputted integers sorted using Quicksort:\n");
        System.out.print("{"+theArray+"}\n");
    }
    
    public void printRadix(int[] arr) {
        arr = radixSort(arr);
        String theArray = "";
        for (int i = 0; i < arr.length; i++) {
            theArray = theArray + arr[i];
            if (i != arr.length-1) {
                theArray = theArray + ", ";
            }
        }
        System.out.print("\nHere are the inputted integers sorted using Radixsort:\n");
        System.out.print("{"+theArray+"}\n");
    }
    
    public void printBubble(int[] arr) {
        arr = bubbleSort(arr);
        String theArray = "";
        for (int i = 0; i < arr.length; i++) {
            theArray = theArray + arr[i];
            if (i != arr.length-1) {
                theArray = theArray + ", ";
            }
        }
        System.out.print("\nHere are the inputted integers sorted using Bubblesort:\n");
        System.out.print("{"+theArray+"}\n");
    }
}