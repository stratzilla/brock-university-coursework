package assign4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class graph {
    
    private int w; //width of maze
    private int h; //height of maze
    private int d; //depth of maze
    private int startPoint; //which maze element contains 'S'
    private int endPoint; //which maze element contains 'E'
    private vertex[][] graph; //adjacency matrix of maze
    private String theMaze; //user inputted maze
    private boolean[] alreadyIndexed; //for DFS/BFS, equivalent to vertex.known
    
    public void findOpt(vertex[][] graph) {
        List thePath = breadthFirst(graph,startPoint); //list of moves
        if (thePath != null) { //if there is a path
            System.out.println("\nFinding optimal solution...");
            getCardinal(thePath); //convert moves into cardinal directions            
        } else { //no path is found between 'S' and 'E'
            System.out.println("\nThere are no paths between start and exit.");
        }
        Arrays.fill(alreadyIndexed,false); //reset so can traverse anew
    }
    
    /**
     * prints the path taken as well as the number of moves to get suboptimal
     * solution using DFS algorithm
     * @param graph the adjacency matrix to traverse
     */
    public void findSub(vertex[][] graph) {
        List thePath = depthFirst(graph,startPoint); //list of moves
        if (thePath != null) { //if there is a path
            System.out.println("\nFinding suboptimal solution...");
            getCardinal(thePath); //convert moves into cardinal directions
        } else { //no path is found between 'S' and 'E'
            System.out.println("\nThere are no paths between start and exit.");
        }
        Arrays.fill(alreadyIndexed,false); //reset so can traverse anew
    }
    
    /**
     * prints cardinal directions given move(n) and move(n+1)
     * as well as the number of moves the path takes
     * @param path the path to convert to cardinal directions
     */
    private void getCardinal(List path) {
        String directions = "(Begin),"; //create empty string to print later
        //we are indexing move(n) and move(n+1), so we need to iterate size-1x
        for (int i = 0; i < path.size()-1; i++) {
            //direction in form of integer from difference of move elements
            int dif = (int) path.get(i+1) - (int) path.get(i);
            //a downward movement is only possible when difference is width
            if (dif == w) { directions = directions + " South,"; }
            //an upward movement is only possible when difference is -width
            else if (dif == -w) { directions = directions + " North,"; }
            //a rightward movement is only possible when difference is 1
            else if (dif == 1) { directions = directions + " East,"; }
            //a leftward movement is only possible when difference is -1
            else if (dif == -1) { directions = directions + " West,"; }
            //a descension is only possible when difference is (width*height)
            else if (dif == w*h) { directions = directions + " Descend,"; }
            //an ascension is only possible when difference is -(width*height)
            else { directions = directions + " Ascend,"; }
            if (i % 6 == 0 && i != 0) { //append newline for readability
                directions = directions + "\n";
            }
        }
        directions = directions + " (End)";
        System.out.print("\nPath from Start to Exit: \n");
        System.out.println(directions);
        //path length is the size of the path less one
        System.out.println("\nPath Length: "+(path.size()-1));
    }
    
    /**
     * finds the optimal path through graph via BFS
     * @param graph the adjacency matrix to traverse
     * @param start the initial position on which to traverse
     * @return the optimal path
     */
    private List<Integer> breadthFirst(vertex[][] graph, int start) {
        List<Integer> temp = new LinkedList(); //temp queue of parent nodes
        List<Integer> path = new LinkedList(); //path list
        int[] prev = new int[w*d*h]; //for referential indices later
        temp.add(0,start); //add to queue
        alreadyIndexed[start] = true; //so not indexed again
        while (temp.size() > 0) { //while # of parents is > 0
            /**
             * finds all potential paths but ignores those
             * that a) don't result in exit and b) is not the optimal path
             * by referencing parent nodes
             */
            int checkIndex = temp.remove(0); //check adjacencies to element b
            if (checkIndex == endPoint) { //if the exit was found, prepare ret.
                int iter = endPoint; //work backwards
                while (iter != start) { //until hit start
                    path.add(0,iter); //append to optimal path
                    iter = prev[iter]; //go to nodal parent
                }
                path.add(0,start); //start point needs to be in path
                return path; //return the path
            }
            for (int i = 0; i < w*h*d; i++) { //iterate over every adjacency
                /**
                 * but only consider adjacencies that are true/1 and 
                 * that the exit has not yet been found (if it exists)
                 */
                if((!alreadyIndexed[i]) && (graph[checkIndex][i].val == 1)
                && (checkIndex != endPoint)) {
                    temp.add(i); //append potential move to temp queue
                    alreadyIndexed[i] = true; //don't re-index
                    prev[i] = checkIndex; //for self referencing
                }
            }
        }
        return null;
    }
    
    /**
     * finds suboptimal (potentially optimal) path through graph via DFS
     * @param graph the adjacency matrix to traverse
     * @param start the initial position on which to traverse
     * @return a path, but not optimal path
     */
    private List<Integer> depthFirst(vertex[][] graph, int start) {
        alreadyIndexed[start] = true; //do not traverse this row again
        if (start == endPoint) { //final case when found exit, seed list from this
            List<Integer> path = new LinkedList();  //seed
            path.add(0,start); //since a == exit, add to path
            return path; //return to DFS function
        }
        for (int i = 0; i < w*h*d; i++){
            //if not indexed and next element is adjacent
            if((!alreadyIndexed[i]) && (graph[start][i].val == 1)) {
                //recursively search for exit and construct path
                List<Integer> path = depthFirst(graph,i);
                //as long as there's a valid path, append the element to path
                if (path != null) {
                    path.add(0,start);
                    return path; //nothing else needs to be done, so return
                }
            }
        }
        return null; //if deadend, do nothing
    }
    
    /**
     * translates user inputted maze into an adjacency matrix
     * @param arr the maze to be converted
     * @return the adjacency matrix of arr
     */
    public vertex[][] createGraph(char[][][] arr) {
        if (arr != null) { //if no maze was inputted or error in maze
           /** 
            * since an adjacency matrix of an input maps the adjacency 
            * between input elements, the dimensions of the adjacency matrix 
            * are equal to the product of input dimensions 
            * (in this case height*width*depth), thus the adjacency matrix'
            * dimensions are (h*w*d) by (h*w*d)
            */
            vertex[][] A = new vertex[w*h*d][w*h*d];
            for (int i = 0; i < A[0].length; i++) {
                for (int j = 0; j < A.length; j++) {
                    A[j][i] = new vertex(); //create empty vertex (0/false)
                }
            }
           /**
            * maps adjacency with rules graph(x,y) = 1 iff
            * - neither x or y are walls
            * - x is adjacent to y (in any of the three axes)
            * - x does not equal y (diagonal)
            * my initial idea was to only compute graph(x,y) on the
            * upper triangular of the graph since an adjacency matrix will
            * always be symmetric along the diagonal, but it looked cleaner
            * this way. In any case, graph(x,y) always equals graph(y,x),
            * or at least in the case of a two-way pathed maze. Directional
            * maze, this is not always the case.
            */
            for (int z = 0; z < d; z++) {
                for (int y = 0; y < w; y++) {
                    for (int x = 0; x < h; x++) {
                        //if the array element is a wall, that row of graph is 0
                        if (arr[y][x][z] != 'X') {
                            /**
                             * a lot of repeated code with minute differens 
                             * below, so I'll only comment one of the ifs
                             * 
                             * as long as x is in bounds and adjacent isn't wall
                             */
                            if (x > 0 && arr[y][x-1][z] != 'X') {
                                //must be adjacent, so 1
                                A[(w*h*z)+(w*x)+y][(w*h*z)+(w*(x-1))+y].val = 1;
                            } if (x < h-1 && arr[y][x+1][z] != 'X') {
                                A[(w*h*z)+(w*x)+y][(w*h*z)+(w*(x+1))+y].val = 1;
                            } if (y > 0 && arr[y-1][x][z] != 'X') {
                                A[(w*h*z)+(w*x)+y][(w*h*z)+(w*x)+(y-1)].val = 1;
                            } if (y < w-1 && arr[y+1][x][z] != 'X') {
                                A[(w*h*z)+(w*x)+y][(w*h*z)+(w*x)+(y+1)].val = 1;
                            } if (z > 0 && arr[y][x][z-1] != 'X') {
                                A[(w*h*z)+(w*x)+y][(w*h*(z-1))+(w*x)+y].val = 1;
                            } if (z < d-1 && arr[y][x][z+1] != 'X') {
                                A[(w*h*z)+(w*x)+y][(w*h*(z+1))+(w*x)+y].val = 1;
                            } 
                        }
                    }
                }
            }
            return A; //return the newly formed adjacency matrix
        }
        return null; //if maze wasn't initialize, do nothing
    }
    
    /**
     * prompts user for dimensions of the maze as well as the maze itself
     * @return user inputted maze in form of char[][][] array
     */
    public char[][][] scanMaze() {
        Scanner input = new Scanner(System.in);
        Scanner maze = new Scanner(System.in);
        String temp;
        System.out.print("\nWidth: ");
        w = input.nextInt(); //width
        System.out.print("Height: ");
        h = input.nextInt(); //height
        System.out.print("Depth: ");
        d = input.nextInt(); //depth
        System.out.println("\nPlease input the maze.\n");
        theMaze = "";
        for (int i = 0; i < (h*d); i++) { //iterate over expected length
            temp = maze.nextLine();
            if (temp.length() == w) { //if a line is length w
                theMaze = theMaze + temp; //append
            } else { i--; } //else ignore, but decrement i so can scan more
        }
        if (h*w*d == theMaze.length()) { //dimensions must match maze
            //there must be a start and exit
            if (theMaze.indexOf('E') != -1 || theMaze.indexOf('S') != -1) {
                startPoint = theMaze.indexOf('S'); //find element # of start
                endPoint = theMaze.indexOf('E'); //find element # of exit
                alreadyIndexed = new boolean[w*h*d]; //used later
                char[][][] mazeArr = new char[w][h][d];
                for (int z = 0; z < mazeArr[0][0].length; z++) {
                    for (int y = 0; y < mazeArr[0].length; y++) {
                        for (int x = 0; x < mazeArr.length; x++) {
                            /**
                             * since the user input is in the form of a long
                             * string, the elements correspond to the index
                             * of a character inside the string. For example,
                             * position (0,0,0) is the character at position
                             * 0 in the string. Note that the char[][][] array
                             * is never used in traversal, just for readability
                             * and to more easily map an adjacency matrix. It
                             * is functionally identical to just scanning
                             * characters at a given index to put into graph
                             */
                            mazeArr[x][y][z] = theMaze.charAt((w*h*z)+(w*y)+x);
                        }
                    }
                }
                return mazeArr; //we're done
            } else { //there must be a start and exit
                String s = "\nThe maze does not have a start and/or end.";
                System.out.println(s);
                return null; //maze not initialized, do nothing
            }
        } else { //the maze must match the user defined dimensions
            String s = "\nThe maze does not match dimensions.";
            System.out.println(s);
            return null; //maze not initialized, do nothing
        }
    }
    
    /**
     * displays the adjacency matrix of the maze to the user
     * @param graph the graph to be printed
     */
    public void printGraph(vertex[][] graph) {
        System.out.println("\nThe adjacency matrix for the maze is below:\n");
        for (int y = 0; y < graph[0].length; y++) {
            for (int x = 0; x < graph.length; x++) {
                System.out.print(graph[x][y].val+" ");
            }
            System.out.println();
        }    
    }
    
    /**
     * displays the maze to the user
     * @param mazeArr the maze to be printed
     */
    public void printMaze(char[][][] mazeArr) {
        System.out.println("\nThe inputted maze is below:\n");
        for (int z = 0; z < mazeArr[0][0].length; z++) {
            for (int y = 0; y < mazeArr[0].length; y++) {
                for (int x = 0; x < mazeArr.length; x++) {
                    System.out.print(mazeArr[x][y][z]+" ");
                }
                System.out.println();
            }
            if (z != mazeArr[0][0].length-1) {
                System.out.println();
            }
        }
    }
    
    /**
     * the vertex class, the type of node found in the adjacency matrix.
     * the slides and text instead use something like a boolean array, but
     * when representing in the slides and text, it's a 0/1 matrix. I've
     * decided to forego booleans and instead just use 1s and 0s
     */
    class vertex {
        int val; //the truth value represented by 0 or 1
        
        /**
         * construct a vertex
         */
        public vertex() {
            this.val = 0; //set initial to false/0
        }
    }
}