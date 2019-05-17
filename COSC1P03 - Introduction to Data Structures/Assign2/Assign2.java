package assign2;

/* The only confusion in this assignment was what an xref table was
 * I asked my peers what they thought and consensus was just to print to
 * System.out. A friend of mine had asked his lab instructor and this was what
 * he said.
 */

import BasicIO.*;
import java.util.StringTokenizer;

public class Assign2 {

    //files to work with
    ASCIIDataFile sourceCode = new ASCIIDataFile();
    ASCIIDataFile reservedFile = new ASCIIDataFile();
    //nodes declared globally because many methods utilize each
    Node outputList = null;
    Node wordList;
    //for concatenating multiple lines together
    String lastElement;
    
    //constructor class
    public Assign2 () {
        scanFile();
        getWords();
        outputTable();
    }
    
    //takes reservedFile and puts it in wordList in alphabetical order
    public void scanFile () {
        //for sorting
        Node p;
        Node q;
        //turn first line of the file into tokenizer readable string
        String fileString = reservedFile.readLine()+" ";
        while (true) {
            //do all of the other lines, concatenated together
            fileString = fileString + reservedFile.readLine()+" ";
            //it's done separately so can catch isEOF()
            if (reservedFile.isEOF()) {
                break;
            }
        }
        //create tokenizer for string
        StringTokenizer st = new StringTokenizer(fileString);
        //create linked list from first string element
        wordList = new Node(st.nextToken(),0,null);
        while (st.hasMoreTokens()) {
            //similar to the original fileString, do all of the other strings
            String str = st.nextToken();
            p = wordList;
            q = null;
            //iterate through list until find the spot where q < element > p
            while (p != null && p.item.compareToIgnoreCase(str) < 0) {
                q = p;
                p = p.next;
            } //break out of loop when the position is right
            //if end of list
            if (q == null) {
                //append to end of list
                wordList = new Node(str,0,p);
            //else append in front of next element
            } else {
                q.next = new Node(str,0,p);
            }            
        }
    }
    
    //get words from sourceCode, put them in a list as well
    private void getWords () {
        //initial line number, incremented later per word
        int lineNum = 1;
        while (true) {
            //similar to how I read the reservedWords file, turn into strings
            String lineString = sourceCode.readLine();
            if (sourceCode.isEOF()) {
                break;
            }
            /* get rid of garbage such as symbols and numbers
             * I kept "_" as is because Lab_x would otherwise be split into
             * two words. I don't know if this was intended by the professor
             */
            lineString = lineString.replaceAll("[^a-zA-Z_]"," ");
            StringTokenizer st = new StringTokenizer(lineString);
            while (st.hasMoreTokens()) {
                //make string from token to pass to insertion method
                String word = st.nextToken();
                //check if the word is on the reserved list; only inserts if F
                if (!checkValid(word)) {
                    //insertion
                    insertList(word,lineNum);
                }
            }
            lineNum++;
        }
    }
    
    //cross references the passed word with the set of reserved words
    private Boolean checkValid (String word) {
        Node p = wordList;
        Node q;
        while (p != null) {
            //check if they equal, and if so, return true
            if (word.equals(p.item)) {
                //method ends here if true is returned
                return true;
            }
            p = p.next;
        }
        //otherwise, return false
        return false;
    }
    
    //sorted insertion, done alphabetically
    private void insertList (String word, int line) {
        Node p;
        Node q;
        //for the first word only
        if (outputList == null) {
            //create new list from empty list
            outputList = new Node(word,line,outputList);
        //for subsequent words
        } else {
            p = outputList;
            q = null;
            //similar to before, find spot where q < element > p
            while (p != null && word.compareToIgnoreCase(p.item) >= 0) {
                q = p;
                p = p.next;
            }
            //append to end of list
            if (q == null) {
                outputList = new Node(word,line,p);
            //append before next element
            } else {
                q.next = new Node(word,line,p);
            } 
        }
    }
    
    //output list to System, organized by element name and linenum
    private void outputTable () {
        Node p = outputList;
        Node q;
        while (p != null) {
            //I know tabs are ugly, but it's the most even spacing I could get
            System.out.print(p.item+"\t\t"+p.line);
            while (true) {
                //if the next item exists, and is equal to item
                if (p.next != null && p.next.item.equals(p.item)) {
                    //only print subsequent line# if they're different
                    if (p.next.line != p.line) {
                        System.out.print(", "+p.next.line);
                        //increment to next element
                        p = p.next;
                    } else {
                        p = p.next;
                    }
                //if no matches, line break
                } else {
                    System.out.print("\n");
                    break;
                }
            }
            //increment to next element
            p = p.next;
        }
    }
    
    //from lab, create class to handle linked lists
    class Node {
        
        String item;
        int line;
        Node next;
        
        public Node (String item, int line, Node next) {
            this.item = item;
            this.line = line;
            this.next = next;
        }
    }

    public static void main (String[] args) {
        Assign2 a = new Assign2();
    }    
}