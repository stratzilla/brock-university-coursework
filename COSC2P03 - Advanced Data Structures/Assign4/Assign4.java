package assign4;

import java.util.Scanner;

public class Assign4 extends graph {
    
    private Scanner in; //for scanning user input
    private vertex[][] graph; //the adjacency matrix to be created
    private char[][][] maze; //the maze itself
    
   /**
    * NB: I could have skipped using a char[][][] array and instead
    * populate a char[] array from user input, but it seemed smarter to do it
    * this way instead. I wanted to include an option to print the user 
    * inputted maze, and the structure for printing it cleanly wasn't there. 
    * Using a char[][][] array is cleaner to read not only for the purpose of
    * reviewing my code, but also easier to read by the program for printing
    * and for converting into an adjacency matrix. At no point is the 
    * char[][][] array being used other than to transcribe into a graph. All
    * traversal algorithms are on the graph only.
    */
    
    public Assign4() {
        int choice = -1; //set initial choice to outside scope of do/while
        in = new Scanner(System.in);
        do {
            choice = menu(); //pop menu
            switch(choice) {
                case 1: { //insert maze
                    maze = scanMaze(); //get maze from user
                    graph = createGraph(maze); //initialize maze into graph
                    if (graph != null) { //if scanMaze() hasn't failed
                        System.out.print("\nMaze successfully loaded.\n");
                    }
                    break;
                }
                case 2: { //print maze
                    if (maze != null) { //if maze exists
                        printMaze(maze);
                    } else { //if scanMaze() failed or hasn't been called yet
                        System.out.print("\nNo maze has yet been loaded.");
                    }
                    break;
                }
                case 3: { //print adj matrix
                    if (graph != null) { //if graph exists
                        printGraph(graph);
                    } else { //if scanMaze() failed or hasn't been called yet
                        System.out.print("\nNo maze has yet been loaded.");
                    }
                    break;
                }
                case 4: { //find subopt path with DFS algorithm
                    if (graph != null) { //if graph exists
                        findSub(graph);
                    } else { //if scanMaze() failed or hasn't been called yet
                        System.out.print("\nNo maze has yet been loaded.");
                    }
                    break;
                }
                case 5: { //find opt path with BFS algorithm
                    if (graph != null) { //if graph exists
                        findOpt(graph);
                    } else { //if scanMaze() failed or hasn't been called yet
                        System.out.print("\nNo maze has yet been loaded.");
                    }
                    break;
                }
            }
        //while there is a valid choice, keep popping the menu
        } while (choice > 0 && choice < 6);
    }
    
    /**
     * displays the user options
     */   
    private int menu() {
        System.out.print("\n1. Enter a Maze");
        System.out.print("\t\t2. Print maze");
        System.out.print("\t\t3. Print graph of maze");
        System.out.print("\n4. Solve suboptimally");
        System.out.print("\t5. Solve optimally");
        System.out.print("\t6. Quit Program\n");
        System.out.print("\nOption: ");
        return in.nextInt();
    }
    
    public static void main(String[] args) {
        Assign4 a = new Assign4();
    }    
}