
package assign3;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Assign3 extends algorithms{
    
    private Scanner input; //user input scanner
    private int[] userArray = new int[0]; //array for user integers
    private int[] randomArray = new int[0]; //array for random integers
    private int[] tempArr = null; //temp, used for displaying
    private int[] tempRandArr = null; //temp, used for displaying
    private Random randGen = new Random(); //random generator
    //used to time methods
    private long beginTime = 0;
    private long endTime = 0;
    private long totTime = 0; //time to sort
    private long reTime = 0; //time to resort
    
    public Assign3() {
        int choice = -1;
        input = new Scanner(System.in); //init scanner
        do {
            choice = menu(); //display menu for choices
            switch(choice) { //user choice
                case 1: {
                    System.out.println("\nInput integers to be sorted (-1 to stop).\n");
                    System.out.print("Integer: ");
                    while (input.hasNextLine()) { //while user keeps inputting
                        int theInt = input.nextInt(); //grab int
                        if (theInt == -1) { break; } //if -1, quit
                        //sneaky way of appending to end of array
                        userArray = Arrays.copyOf(userArray, (userArray.length+1));
                        userArray[userArray.length-1] = theInt; //init into array
                        System.out.print("Integer: ");
                    }
                    tempArr = userArray.clone(); //don't want to alter arr, so clone it
                    break;
                }
                case 2: {
                    String theArray = "";
                    System.out.println("\nHere are the user inputted integers: ");
                    for (int i = 0; i < userArray.length; i++) { //inc over elements
                        theArray = theArray + userArray[i]; //append to string
                        if (i != userArray.length-1) {
                            theArray = theArray + ", "; //print string
                        }
                    }
                    System.out.print("{"+theArray+"}\n");
                    break;
                }
                case 3: { 
                    if (tempArr == null) {
                        System.out.println("\nYou must first input integers to sort the input.");
                        break;
                    } else { 
                        printHeap(tempArr); 
                        tempArr = userArray.clone(); //reset to userArr
                        break; 
                    }
                } //heap sort
                case 4: { 
                    if (tempArr == null) {
                        System.out.println("\nYou must first input integers to sort the input.");
                        break;
                    } else { 
                        printMerge(tempArr); 
                        tempArr = userArray.clone();
                        break; 
                    }
                } //merge sort
                case 5: { 
                    if (tempArr == null) {
                        System.out.println("\nYou must first input integers to sort the input.");
                        break;
                    } else { 
                        printQuick(tempArr); 
                        tempArr = userArray.clone();
                        break; 
                    }
                } //quick sort
                case 6: { 
                    if (tempArr == null) {
                        System.out.println("\nYou must first input integers to sort the input.");
                        break;
                    } else { 
                        printRadix(tempArr); 
                        tempArr = userArray.clone();
                        break; 
                    }
                } //radix sort
                case 7: { 
                    if (tempArr == null) {
                        System.out.println("\nYou must first input integers to sort the input.");
                        break;
                    } else { 
                        printBubble(tempArr); 
                        tempArr = userArray.clone();
                        break; 
                    }
                } //bubble sort
                case 8: { 
                    System.out.println("\nHow many integers would you like in the array? (negative to cancel)\n");
                    System.out.print("Number of Array Elements: ");
                    int theInt = input.nextInt(); //determine how many ints user wants
                    if (theInt < 0) { break; } //if negative or 0, quit
                    timed(theInt);
                    break;
                }
            }            
        } while (choice > 0 && choice <= 8);
    } 
    
    //time sorting computation speed
    private void timed(int n) {
        //this method is just repeated multiple times for different sorts
        //so I'll only comment the first since it'll be identical to the others
        System.out.println("\nTiming a hundred sorts of "+n+" four digit (0-9,999) integers...\n");
        randomArray = new int[n]; //array of user defined size
        int it = 0; //to mean them later
        //iterate 100 times for more accurate data
        for (int j = 0; j < 100; j++, it++) {
            //iterate over length of array
            for (int i = 0; i < randomArray.length; i++) {
                randomArray[i] = randGen.nextInt(9999)+1; //init random int
            }
            tempArr = randomArray.clone(); //similar to insertion, use temps instead
            beginTime = System.nanoTime(); //start clock
            heapSort(tempArr); //do the sort
            endTime = System.nanoTime(); //stop clock
            totTime += endTime-beginTime; //append sort totals
            beginTime = System.nanoTime();
            heapSort(tempArr); //redo the sort
            endTime = System.nanoTime();
            reTime += endTime-beginTime; //append resort totals
        }
        //average the times over 100 passes and display
        System.out.println("Heapsort took "+(totTime/it)+"ns to sort.");
        System.out.println("Heapsort took "+(reTime/it)+"ns to resort.");
        totTime = 0; //reset
        reTime = 0; //reset
        it = 0; //reset
        for (int j = 0; j < 100; j++, it++) {
            for (int i = 0; i < randomArray.length; i++) {
                randomArray[i] = randGen.nextInt(9999)+1;
            }
            tempArr = randomArray.clone();
            beginTime = System.nanoTime();
            mergeSort(tempArr);
            endTime = System.nanoTime();
            totTime += endTime-beginTime;
            beginTime = System.nanoTime();
            mergeSort(tempArr);
            endTime = System.nanoTime();
            reTime += endTime-beginTime;
        }
        System.out.println("Mergesort took "+(totTime/it)+"ns to sort.");
        System.out.println("Mergesort took "+(reTime/it)+"ns to resort.");
        totTime = 0;
        reTime = 0;
        it = 0;
        for (int j = 0; j < 100; j++, it++) {
            for (int i = 0; i < randomArray.length; i++) {
                randomArray[i] = randGen.nextInt(9999)+1;
            }
            tempArr = randomArray.clone();
            beginTime = System.nanoTime();
            quickSort(tempArr);
            endTime = System.nanoTime();
            totTime += endTime-beginTime;
            beginTime = System.nanoTime();
            quickSort(tempArr);
            endTime = System.nanoTime();
            reTime += endTime-beginTime;
        }
        System.out.println("Quicksort took "+(totTime/it)+"ns to sort.");
        System.out.println("Quicksort took "+(reTime/it)+"ns to resort.");
        totTime = 0;
        reTime = 0;
        it = 0;
        for (int j = 0; j < 5; j++, it++) {
            for (int i = 0; i < randomArray.length; i++) {
                randomArray[i] = randGen.nextInt(9999)+1;
            }
            tempArr = randomArray.clone();
            beginTime = System.nanoTime();
            radixSort(tempArr);
            endTime = System.nanoTime();
            totTime += endTime-beginTime;
            beginTime = System.nanoTime();
            radixSort(tempArr);
            endTime = System.nanoTime();
            reTime += endTime-beginTime;
        }
        System.out.println("Radixsort took "+(totTime/it)+"ns to sort.");
        System.out.println("Radixsort took "+(reTime/it)+"ns to resort.");
        totTime = 0;
        reTime = 0;
        totTime = 0;
        reTime = 0;
        it = 0;
        for (int j = 0; j < 100; j++, it++) {
            for (int i = 0; i < randomArray.length; i++) {
                randomArray[i] = randGen.nextInt(9999)+1;
            }
            tempArr = randomArray.clone();
            beginTime = System.nanoTime();
            bubbleSort(tempArr);
            endTime = System.nanoTime();
            totTime += endTime-beginTime;
            beginTime = System.nanoTime();
            bubbleSort(tempArr);
            endTime = System.nanoTime();
            reTime += endTime-beginTime;
        }
        System.out.println("Bubblesort took "+(totTime/it)+"ns to sort.");
        System.out.println("Bubblesort took "+(reTime/it)+"ns to resort.");
    }
    
    //display the menu of options
    private int menu() {
        System.out.print("\nUser Inputted Integers\n");
        System.out.print("\t1. Add integers to be sorted\t");
        System.out.print("2. View inputted integers");
        System.out.print("\nSorting Algorithms\n");
        System.out.print("\t3. Heapsort\t");
        System.out.print("4. Mergesort\t");
        System.out.print("5. Quicksort\t");
        System.out.print("6. Radixsort\t");
        System.out.print("7. Bubblesort");
        System.out.print("\nTime Sorting Algorithms\n");
        System.out.print("\t8. Sort n random integers\n");
        System.out.print("\nOption (-1 to quit): ");
        return input.nextInt();
    }

    public static void main(String[] args) {
        Assign3 a = new Assign3();
    }    
}