package assign3;
import BigNumbers.*;

public class Assign3 {   
    
    //for the sake of example, I've used some basic numbers
    BNum z = new ConBNum(); //empty number
    BNum a = new ConBNum(1972); //long number
    BNum b = new ConBNum("1994"); //string number
    BNum c = a.add(b); //addition
    BNum d = a.sub(b); //subtraction
    BNum e = a.add(b).sub(d); //multiple operations
    BNum f = new ConBNum(-1972); //negative long
    BNum g = new ConBNum("-1994"); //negative string
    BNum h = new ConBNum("999999999999999999999999999999999999999"); //very long
    BNum i = a.clone();
    
    public Assign3 () {
        TestHarness(); //testing functionality
    }
    
    public final void TestHarness () {
        /* the below many lines are just testing some of the functionality of
        the ADT */
        System.out.println("empty number = "+z.toString());
        System.out.println("long number = "+a.toString());
        System.out.println("string number = "+b.toString());
        System.out.println("long negative number = "+f.toString());
        System.out.println("string negative number = "+g.toString());
        System.out.println("number larger than long = "+h.toString());   
        System.out.println("clone of a = "+i.toString());
        System.out.println("sign a = "+a.getSign()); //get sign
        System.out.println("sign b = "+b.getSign()); //get sign
        System.out.println("equals = "+a.isEquals(b)); //check equality
        System.out.println("lessthan = "+a.lessThan(b)); //check less-than
        System.out.println("addition = "+c.toString());
        System.out.println("subtraction = "+d.toString());
        System.out.println("index of a at 3 = "+a.getDigit(3)); //grab digit
        System.out.println("multiple operations = "+e.toString());
        System.out.println("\nTrying string number with non-numeric character");
        BNum j = new ConBNum("19d5");
        System.out.println("\nTrying to index digit out of range");
        int q = a.getDigit(6);
    }

    //ConBNum implementation
    public class ConBNum implements BNum {
        /* I'm using an array of a fixed length (100) because it was difficult
        to implement a right-sized array. Each element has a maximum size of 18
        digits (recall longs are 19 digits long), giving something nearly 95x
        the length of a typical long. */
        private long[] lonArr = new long[100];
        private int sign; //sign of the object
        
        public ConBNum () {
            this(0); //if empty, do ConBNum(0) instead
        } //default constructor
        
        /* regarding this method, I'm not sure if this was intentional,
        but this can never accept anything longer than a long because of the 
        argument type (long). Putting in a very long number just goes beyond
        the limitation of long and throws. */
        public ConBNum (long l) {
            this.lonArr[0] = l; //initialize only the first element as it's Sm.
            this.sign = (l < 0) ? -1 : 1; //check for sign
        } //long constructor
        
        public ConBNum (String s) {
            try { //try complex, throws exception if failed
                for (int i = 0; i < s.length(); i++) {
                    char c = s.charAt(i); //check individual characters
                    //failure check; if non-numeric character found
                    if (c != 45 && (c < 48 || c > 57)) {
                        String m = "Please format the string without alphabetics.";
                        throw new BadNumberFormatException(m);
                    } 
                }
                this.sign = (s.charAt(0) == 45) ? -1 : 1; //sign of the object
                if (s.length() > 18) { //recall each array element has max = 18
                    String[] splitArr = s.split("(?<=\\G\\d{18})"); //split digits
                    for (int j = 0; j < splitArr.length; j++) {
                        this.lonArr[j] = Long.parseLong(splitArr[j]); //initialize
                    }
                } else { //if it's shorter than 2 elements, only init. 1 element
                    this.lonArr[0] = Long.parseLong(s);
                }
            } catch (BadNumberFormatException e) {
                System.out.println(e);
                this.lonArr[0] = 0; //set value to 0 if failed
            }
        } //string constructor

        /* I had to add this method as I came across a point where I couldn't
        reference n.lonArr. I spent a couple of hours trying to figure it out
        without adding an additional method, but I couldn't figure it out. This
        method only returns lonArr of a given object. */
        @Override public long[] grabVal() {
            return this.lonArr;
        }    
        
        //returns the sign of the object
        @Override public int getSign() {
            return this.sign;
        }
        
        //makes a clone of the BNum object
        @Override public BNum clone() {
            return new ConBNum(this.toString());
        }
        
        /* returns boolean check for if two obj. equal
        I had to rename this method else it would conflict with String.equals()
        which I am sure is real bad. */
        @Override public boolean isEquals(BNum n) {
            for (int i = 0; i < this.lonArr.length; i++) {
                if (this.lonArr[i] != n.grabVal()[i]) {
                    return false;
                }
            }
            return true;
        }
        
        //returns boolean check for if obj. a is < obj. b
        @Override public boolean lessThan(BNum n) {
            //turn them into strings
            String firstCompare = this.toString();
            String secondCompare = n.toString();
            if (this.sign < n.getSign()) {
                return true; //-a always < b
            } else if (this.sign > n.getSign()) {
                return false; //a always > -b
            } else {
                if (firstCompare.equals(secondCompare)) {
                    return false; //if a == b, then a !< b
                } else {
                    if (firstCompare.length() < secondCompare.length()) {
                        return true; //if a is less digits, then a < b
                    } else if (firstCompare.length() > secondCompare.length()) {
                        return false; //if a is more digits, then a > b
                    } else {
                        /* compare each digit between the objects. My methodol-
                        ogy is weird here, but it works. If a = 100 and b = 101
                        it will check that 1-- == 1--, then -0- == -0-, and then
                        --0 != --1, and see which is lower and if it belongs to
                        a, then return false. */
                        for (int i = 0; i < firstCompare.length(); i++) {
                            if (firstCompare.charAt(i) > secondCompare.charAt(i)) {
                                return false;
                            }
                        }
                    }
                }
            }
            return true; //if everything is smooth, true
        }
        
        //adds the lonArr values between a and b
        @Override public BNum add(BNum n) {
            BNum someArr = new ConBNum(0); //make new BNum for later
            for (int i = 0; i < this.lonArr.length; i++) { //add 'em
                someArr.grabVal()[i] = this.lonArr[i] + n.grabVal()[i];
            }
            return someArr; //return the object
        }
        
        //similar to BNum.add(), but subtracts
        @Override public BNum sub(BNum n) {
            BNum someArr = new ConBNum(0);
            if (this.toString().equals(n.toString())) {
                someArr.grabVal()[0] = 0; //only here since n - n = 0
            } else {
                for (int i = 0; i < this.lonArr.length; i++) {
                    someArr.grabVal()[i] = this.lonArr[i] - n.grabVal()[i];
                }
            }
            return someArr;
        }
        
        //grabs a digit of specified index
        @Override public int getDigit(int g) {
            try { //try complex, throws exception if failed
                String arrLength = this.toString();
                if (g > arrLength.length() || g <= 0) { //failure point
                    throw new DigitOutOfRangeException("You cannot index out of range.");
                } else {
                    /* we want to index actual numbers, not any symbols if they
                    exist, so if the sign is positive, the index is shifted -1.
                    Recall charAt() indexes actual number g-1 */
                    g = (this.sign > 0) ? g-1 : g;
                    return arrLength.charAt(g)-'0'; //way to change char to int
                }
            } catch (DigitOutOfRangeException ex) {
                System.out.println(ex);
                return 0; //returns 0 otherwise
            }
        } 
        
        //turns lonArr into a string for printing
        @Override public String toString () {
            String finishedString = "";
            int counter = 0;
            if (this.lonArr[0] == 0) {
                finishedString = finishedString + "0"; //if empty object, 0
            } else {
                /* iterate through entire array, stopping when 0. Most arrays
                will be something like {274892},{0},{0},{0},{0}..., we don't
                care for the trailing zeroes since it wastes time */
                for (int j = 0; j < this.lonArr.length; j++) {
                    if (this.lonArr[j] == 0) {
                        break; //once it finds a 0, break
                    }
                    counter++;
                }
                //iterate up until zeroes were found
                for (int i = 0; i < counter; i++) {
                    //turn into a string
                    finishedString = finishedString + this.lonArr[i];
                }
            }
            return finishedString; //return it
        }
    }
    
    /*public class LinkBNum implements BNum {
        private long lon;
        private int sign;
        
        public LinkBNum () {
            this(0);
        }        
        public LinkBNum (long l) {
            this.lon = l;
            this.sign = (lon < 0) ? -1 : 1;
        }
        public LinkBNum (String s) {
            try {
                for (int i = 0; i < s.length(); i++) {
                    char c = s.charAt(i);
                    if (c != 45 && (c < 48 || c > 57)) {
                        String m = "Please format the string without alphabetics.";
                        throw new BadNumberFormatException(m);
                    } 
                }
                this.sign = (this.lon < 0) ? -1 : 1;
                this.lon = Long.parseLong(s);
            } catch (BadNumberFormatException e) {
                System.out.println(e);
            }
        }

        @Override
        public long grabVal() {
            return this.lon;
        }        
        @Override
        public boolean isEquals(BNum num) {
            return this.lon == num.grabVal();
        }
        @Override
        public boolean lessThan(BNum n) {
            return this.lon < n.grabVal();
        }
        @Override
        public BNum add(BNum n) {
            return new ConBNum(this.lon + n.grabVal());
        }
        @Override
        public BNum sub(BNum n) {
            return new ConBNum(this.lon - n.grabVal());
        }
        @Override
        public int getSign() {
            return this.sign;
        }
        @Override
        public int getDigit(int g) {
            int check = g;
            if (this.sign == -1) { check++; }
            String numString = this.lon+"";
            try {
                if (g > numString.length() || g < 1) {
                    throw new DigitOutOfRangeException("Cannot index value out of range.");
                }
                int counter = 0;
                numString = numString.replaceAll(""," ");
                StringTokenizer st = new StringTokenizer(numString);
                Node list = null;
                Node p = list;
                Node q = null;
                while (st.hasMoreTokens()) {
                    while (p != null) {
                        q = p;
                        p = p.next;
                    }
                    if (q == null) {
                        list = new Node(st.nextToken(),null);
                        counter++;
                        if(counter == check) { break; }
                    } 
                }
                return Integer.parseInt(list.item);
            } catch (DigitOutOfRangeException e) {
                System.out.println(e);
                return 0;
            }
        }        
        @Override
        public String toString () {
            return this.lon+"";
        }
    }*/
    
    //from one of our labs
    class Node {
        String item;
        Node next;
        
        public Node (String item, Node next) {
            this.item = item;
            this.next = next;
        }
    }
    
    //exception thrown when string number has non-numeric characters (excl. "-")
    public class BadNumberFormatException extends RuntimeException {
        public BadNumberFormatException(){
            super("Please format your number string to be devoid of letters.");
        }
        public BadNumberFormatException(String m){
            super(m);
        }
    }
    
    //exception thrown when getDigit() index is out of bounds
    public class DigitOutOfRangeException extends NullPointerException {
        public DigitOutOfRangeException(){
            super("You cannot index beyond the constraints of the number.");
        }
        public DigitOutOfRangeException(String m){
            super(m);
        }
    }
    
    public static void main(String[] args) {
        Assign3 a = new Assign3();
    }        
}
