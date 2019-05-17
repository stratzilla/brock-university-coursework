package assign1;

/* I'm not sure if this assignment was done properly. The handout advised to
 * use an individual method for each 8 direction, but I only needed three:
 * one each for vertical, horizonal, diagonal (\-shaped), the latter of which
 * was called twice. I decided to have my methods check both directions (so
 * up and down in one method for example) to conserve lines.
 *
 * You'll see some weirdness when I do diagonals. I spent too long trying to
 * do diagonals in a / shape (although in a \ shape it was easy). I ended up
 * reversing the crossword and just redoing diagonal checks, so when I put
 * it the right way again, it was solved properly.
 *
 * I've commented the assignment heavily, I hope you don't mind. */

import BasicIO.*;

public class Assign1 {

    /* Relevant arrays declared globally so no passing arrays
     * between functions is necessary. These are all right-sized arrays. */
    final char[][] wordArray = new char[21][]; //word array
    //the below array is so the algorithm does not recheck found words
    boolean[] solvedWords = new boolean[21]; //solved word array
    final char[][] crossWordArray = new char[25][25]; //crossword array
    //not declared final because will be constantly updated
    char[][] solvedArray = new char[25][25]; //solved crossword array
    int successes; //the number of words found

    //constructor class
    public Assign1 () {
        insertData(); //take data from file to put in arrays
        checkForWords(); //check for words in the puzzle
        printSolved(); //print results
    }

    //inserts the data from the file into the arrays
    private void insertData () {
        //input file (the .dat supplied)
        ASCIIDataFile inputFile = new ASCIIDataFile();
        String lineString;
        //for lines 1 to 21 (ie. the words themselves)
        for (int i = 0; i < 21; i++) {
            //read the line
            lineString = inputFile.readLine().toUpperCase();
            //store the line
            wordArray[i] = lineString.toCharArray();
        }
        //for lines 22 and beyond (the word search)
        for (int j = 0; j < 25; j++) {
            //read the line
            lineString = inputFile.readLine();
            //store the line
            crossWordArray[j] = lineString.toCharArray();            
        }
        inputFile.close(); //close file stream
    }

    /* blanket method for all search indexes; this is the part I wasn't sure
     * if it was okay to do like this. Instead of moving along each array
     * element sequentially, I found only a few basic checks were needed. For
     * example: you don't need to check up and down on elements other than the
     * ones in row 0 (since checking up/down on row 1 or beyond will give
     * the same result). The same is done for left and right, only using 
     * column 0. I used strings extensively since it was a) faster, b) cleaner,
     * and c) easier to code. Doing individual char elements would have
     * resulted in many redundant checks. */
    private void checkForWords () {
        //checks up and down for words
        for (int i = 0; i < crossWordArray.length; i++) { //x-val loop
            for (int j = 0; j < wordArray.length; j++) { //word loop
                if (!solvedWords[j]) { //boolean check for word
                    checkUpDown(j,i);
                }
            }
        }
        //checks left and right for words
        for (int k = 0; k < crossWordArray.length; k++) { //y-val loop
            for (int m = 0; m < wordArray.length; m++) { //word loop
                if (!solvedWords[m]) { //boolean check for word
                    checkLeftRight(m,k);
                }
            }
        }
        //checks diagonally for words
        for (int n = 0; n < crossWordArray.length; n++) { //y-val loop
            for (int o = 0; o < crossWordArray.length; o++) { //x-val loop
                for (int p = 0; p < wordArray.length; p++) { //word loop
                    if (!solvedWords[p]) { //boolean check for word
                        checkDiagonal(p,n,o);
                    }
                }
            }
        }
        //checks reverse diagonally for words
        reverseCrossword(); //reverse crossword for "/" shaped diagonal checks
        reverseSolved(); //need to reverse this, too
        for (int q = 0; q < crossWordArray.length; q++) { //y-val loop
            for (int r = 0; r < crossWordArray.length; r++) { //x-val loop
                for (int s = 0; s < wordArray.length; s++) { //word loop
                    if (!solvedWords[s]) { //boolean check for word
                        checkDiagonal(s,q,r);
                    }
                }
            }
        }
        reverseSolved(); //put it back to normal for printing later
    }

    /* I could have taken the below three methods and made them into one
     * because a lot of the code is identical in each (only vars initialized
     * differently). For the sake of simplicity, I've left it as is, as other-
     * -wise it is difficult to read. */
    
    /* checks each column for words. Does both forwards and backwards in the
     * same method so as to reduce the number of functions. */
    private void checkUpDown(int wordId, int xpos) {
        //strings for indexing
        String searchWord = "";
        String searchWordRev = "";
        String searchLine = "";
        int position; //position of index
        int positionRev; //position of reverse word index
        int wordLength = wordArray[wordId].length;
        //take words and put in a string, as with the reverse of the word
        for (int i = 0; i < wordLength; i++){
            searchWord = searchWord + wordArray[wordId][i];
            searchWordRev = searchWordRev + wordArray[wordId][wordLength-1-i];
        }
        /*make strings of every vertical line in the crossword, then check
         * for the words. */
        for (int j = 0; j < crossWordArray.length; j++){
            searchLine = searchLine + crossWordArray[j][xpos]; //along y-axis
        }
        //the position of the word, if found, will be initialized below
        position = searchLine.indexOf(searchWord);
        positionRev = searchLine.indexOf(searchWordRev);
        if (position != -1) { //if word found (-1 = not found)
            successes++; //increment # of successes
            //change bool value in boolarray so that the word is not rechecked
            solvedWords[wordId] = true;
            //insert into solution array
            for (int k = 0; k < wordLength; k++) {
                int sH = position+k; //here so the below doesn't go off page
                solvedArray[sH][xpos] = crossWordArray[sH][xpos];
            }
            //the below is the same methodology as above
        } else if (positionRev != -1) {
            successes++;
            solvedWords[wordId] = true;
            for (int m = 0; m < wordLength; m++) {
                int sHR = positionRev+m;
                solvedArray[sHR][xpos] = crossWordArray[sHR][xpos];
            }
        }
    }

    /* similar to the aforementioned method, this also does both directions to
     * conserve space. */
    private void checkLeftRight(int wordId, int ypos) {
        /* see the comments in checkUpDown() since they're pretty much
         * the same for this method. All of the logic is reused. */
         String searchWord = "";
         String searchWordRev = "";
         String searchLine = "";
         int position;
         int positionRev;
         int wordLength = wordArray[wordId].length;
         for (int i = 0; i < wordLength; i++){
             searchWord = searchWord + wordArray[wordId][i];
             searchWordRev = searchWordRev + wordArray[wordId][wordLength-1-i];
         }
         for (int j = 0; j < crossWordArray.length; j++){
             searchLine = searchLine + crossWordArray[ypos][j]; //along x-axis
         }
         position = searchLine.indexOf(searchWord);
         positionRev = searchLine.indexOf(searchWordRev);
         if (position != -1) {
             successes++;
             solvedWords[wordId] = true;
             for (int k = 0; k < wordLength; k++) {
                 int sH = position+k;
                 solvedArray[ypos][position+k] = crossWordArray[ypos][sH];
             }
         } else if (positionRev != -1) {
             successes++;
             solvedWords[wordId] = true;
             for (int m = 0; m < wordLength; m++) {
                 int sHR = positionRev+m;
                 solvedArray[ypos][positionRev+m] = crossWordArray[ypos][sHR];
             }
         }
     }

    private void checkDiagonal(int wordId, int ypos, int xpos) {
         //again, see comments for checkUpDown()
         String searchWord = "";
         String searchWordRev = "";
         String searchLine = "";
         int position;
         int positionRev;
         int wordLength = wordArray[wordId].length;
         for (int i = 0; i < wordLength; i++){
             searchWord = searchWord + wordArray[wordId][i];
             searchWordRev = searchWordRev + wordArray[wordId][wordLength-1-i];
         }
         for (int j = 0; j < crossWordArray.length-xpos-ypos; j++){
             //below along both axes
             searchLine = searchLine + crossWordArray[ypos+j][xpos+j];
         }
         position = searchLine.indexOf(searchWord);
         positionRev = searchLine.indexOf(searchWordRev);
         if (position != -1) {
             successes++;
             solvedWords[wordId] = true;
             for (int k = 0; k < wordLength; k++) {
                 int sHa = ypos+position+k;
                 int sHb = xpos+position+k;
                 solvedArray[sHa][sHb] = crossWordArray[sHa][sHb];
             }
         } else if (positionRev != -1) {
             successes++;
             solvedWords[wordId] = true;
             for (int m = 0; m < wordLength; m++) {
                 int sHRa = ypos+positionRev+m;
                 int sHRb = xpos+positionRev+m;
                 solvedArray[sHRa][sHRb] = crossWordArray[sHRa][sHRb];
             }
         }
     }

    //used only for diagonal checks, reverse the crossword for indexing
    private void reverseCrossword() {
        String line = "";
        for (int i = 0; i < crossWordArray.length; i++) {
            for (int j = crossWordArray[i].length; j > 0; j--) {
                //concatenate chars in reverse order into a string
                line = line + crossWordArray[i][j-1];
            }
            //place it crossword at its original position, albeit backwards
            crossWordArray[i] = line.toCharArray();
            line = ""; //reset string for next line
        }
    }

    //used only for diagonal checks, reverse the solved array for insertion
    private void reverseSolved() {
        String line = "";
        for (int i = 0; i < solvedArray.length; i++) {
            for (int j = solvedArray[i].length; j > 0; j--) {
                //concatenate chars in reverse order into a string
                line = line + solvedArray[i][j-1];
            }
            //place it in the solved array as done with the crossword
            solvedArray[i] = line.toCharArray();
            line = ""; //reset string for next line
        }
    }

    /* prints an array of solved words, along with the total number
     * of successes. */
    private void printSolved () {
        System.out.println("Found: " + successes); //# of successes
        for (int i = 0; i < solvedArray.length; i++) {
            String line = new String(solvedArray[i]); //make string of solution
            System.out.println(line); //print to system panel
        }
    }

    public static void main(String[] args) {
        Assign1 a = new Assign1();
    }    

}