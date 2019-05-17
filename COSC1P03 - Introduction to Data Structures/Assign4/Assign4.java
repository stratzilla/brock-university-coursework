package assign4;

import BasicIO.*;

public class Assign4 {
    
    //global variables, initialized globally because used by many methods
    ASCIIDataFile theMaze = new ASCIIDataFile(); //input file for the maze
    int mazeY = theMaze.readInt(); //maze's # rows
    int mazeX = theMaze.readInt(); //maze's # cols
    ASCIIDisplayer printer = new ASCIIDisplayer(mazeY*2,mazeX*2); //solution print
    char[][] mazeArray = new char[mazeX][mazeY]; //array used for indexing
    int[] userIn = new int[2]; //user defined initial position
    
    public Assign4() {
        promptUser(); //get initial x,y-val from user
        loadMaze(); //grab maze file and put in array
        findPath(userIn[0],userIn[1]); //recursive search algorithm
        drawNewMaze(); //related to findPath(), get rid of marked areas
        printMaze(); //display solution
    }
    
    //uses basic form to prompt user for starting position
    private void promptUser() {
        BasicForm prompter = new BasicForm(); //initialize prompter
        int[] input = new int[2]; //actual position
        //fields
        prompter.setTitle("Starting Position");
        prompter.addTextField("x","x: ");
        prompter.addTextField("y","y: ");
        prompter.accept();
        //temporary variables
        int x = prompter.readInt("x");
        int y = prompter.readInt("y");
        if ((x <= 0 || x >= mazeX) //if prompted values are out of range, redo
        || (y <= 0 || y >= mazeY)) {
            prompter.close();
            System.out.println("The values you have put in are out of range.");
            promptUser();
        } else { //otherwise, initialize input into concrete variables
            userIn[0] = x;
            userIn[1] = y;
            prompter.close();
        }
    }
    
    //load the maze into an array
    private void loadMaze() {
        for (int a = 0; a < mazeY; a++) {
            String mazeLine = theMaze.readLine(); //take y line for charAt index
            for (int b = 0; b < mazeX; b++) {
                mazeArray[b][a] = mazeLine.charAt(b); //put in array element x,y
            }
        }
        theMaze.close();
    }
    
    /* standard BFS algorithm, index a given x,y val (seed value user defined)
    and check each adjacent cell (array element) recursively. Marks checked
    areas with an 's', so the algorithm doesn't recheck them. Once the exit has
    been found from the initial position, mark the taken route with a dot.
    Unfortunately, I wasn't able to implement directions (v, ^, <, >) in a
    breadth-first implementation. However, it does find a relatively short path. */
    private boolean findPath(int x, int y) {
        if (mazeArray[x][y] == 'E') { 
            return true; //if found exit, return true
        } else if (mazeArray[x][y] == '#') {
            return false; //if hit a wall, return false
        } else if (mazeArray[x][y] == 's') {
            return false; //if encounter a previously indexed cell, return false
        }
        mazeArray[x][y] = 's'; //set cell initially to already indexed
        /* it's kind of confusing, as recursion is, but it checks adjacent cells
        to (x,y), and adjacent cells or those adjacent cells, then adjacent
        cells of THOSE adjacent cells, etc until a wall, a previously indexed
        cell, or the exit is found. */
        if (x < mazeX && findPath(x+1,y) //checks adjacent cells to a max x-val
        || (y > 0 && findPath(x,y-1)) //same for y, but min y-val
        || (x > 0 && findPath(x-1,y)) //same, min x-val
        || (y < mazeY && findPath(x,y+1))) { //same, max y-val
            mazeArray[x][y] = 183; //set correct path as a dot
            return true; 
        }
        return false;
    }
  
    /* this just replaces all 's' and spaces. I was hoping to also iterate over
    correct cells and replace them with the proper direction, but it was too
    difficult. I would have indexed where prior correct cells were, then make
    a direction from that. The problem is there was no seed value, and setting
    a seed value was impossible as the starting position is user-defined. Addi-
    tionally, it would have been several dozen lines long. */
    private void drawNewMaze() {
        for (int a = 0; a < mazeY; a++) {
            for (int b = 0; b < mazeX; b++) {
                if (mazeArray[b][a] == 's') {
                    mazeArray[b][a] = ' ';
                }
            }
        }
    }
    
    //print the maze
    private void printMaze() {
        printer.setLabel("Maze Solution");
        for (int a = 0; a < mazeY; a++) {
            for (int b = 0; b < mazeX; b++) {
                printer.writeChar(mazeArray[b][a]);
            }
            printer.newLine();
        }
        printer.show();
        printer.close();
    }

    public static void main(String[] args) {
        Assign4 a = new Assign4();
    }    
}