
package MULTISET;

import java.util.Iterator;

public class Bag<E extends Keyed> implements MultiSet<E> {
    
    int count; //# of elements in bag
    E[] array; //the array of elements

    //default constructor
    public Bag() {
        this.count = 0; //initialize initial count as zero
        this.array = (E[]) new Keyed[100]; //a size of 100 seems sufficient
    }

    //added method to print the set
    @Override
    public String printSet() {
        String printer = "{";
        for (int i = 0; i < this.cardinality(); i++) {
            printer = printer + this.getArray()[i].getKey();
            if (i != this.cardinality()-1) {
                printer = printer + ",";
            }
        } 
        printer = printer + "}";
        return printer;
    }

    //adding an item
    @Override
    public void add(E item) {
        //if no room, throw
        if (this.count >= this.array.length) {
            throw new NoSpaceException();
        //if first element to be added, initialize as first element
        } else if (this.count == 0) {
            this.array[0] = item;
        } else {
        //my attempted sorted insertion
            /*int n = 0;
            while (item.getKey().compareTo(this.array[n].getKey()) <= 0 && n < this.count) {
                n++;
            }
            for (int j = this.count; j > n+1; j--) {
                this.array[j] = this.array[j-1];
            }
            this.array[n] = item;*/
            //append to end of bag
            this.array[this.count] = item;
            //sort them after each insertion
            this.eBubble();
        }
        //raise the count
        this.count++;
    }

    //standard bubblesort
    public E[] eBubble() {
        for (int i = 0; i < this.count; i++) {
            for (int j = 0; j < this.count - i; j++) {
                if (this.array[j].getKey().compareTo(this.array[j+1].getKey()) > 0) {
                    E temp = this.array[j];
                    this.array[j] = this.array[j+1];
                    this.array[j+1] = temp;
                }
            }                
        }     
        return this.array;
    }

    //returns the count of a Bag
    @Override
    public int cardinality() {
        return this.count;
    }

    //returns the instances of item in Bag
    @Override
    public int multiplicity(E item) {
        int n = 0;
        int counter = 0;
        while (n < this.count) {
            //if found a match, increment counter
            if (this.array[n].getKey().compareTo(item.getKey()) == 0) {
                counter++;
            }
            n++;
        }
        return counter;
    }

    //returns true if set is empty
    @Override
    public Boolean isEmpty() {
        return this.count == 0;
    }

    //returns the array for use in some methods
    @Override
    public E[] getArray() {
        return this.array;
    }

    @Override
    public Node<E> getNode() {
        return null;
    }

    /* because of my bubblesort on insertion, all I need to do is sequent-
       -ially get each element from both bags, and insert them into
       a new Bag.
    */
    @Override
    public MultiSet<E> union(MultiSet<E> aSet) {
        MultiSet newBag = new Bag();
        for (int i = 0; i < aSet.cardinality(); i++) {
            newBag.add(aSet.getArray()[i]);
        }
        for (int i = 0; i < this.count; i++) {
            newBag.add(this.getArray()[i]);
        }
        return newBag;
    }

    //similar to the above, but only adds if an element is in both A & B
    @Override
    public MultiSet<E> intersection(MultiSet<E> aSet) {
        MultiSet newBag = new Bag();
        for (int i = 0; i < aSet.cardinality(); i++) {
            //if multiplicity of element C from A in B, add to new Bag
            if (this.multiplicity(aSet.getArray()[i]) != 0) {
                newBag.add(aSet.getArray()[i]);
            }
        }
        return newBag;
    }

    //checks equivalency between two Bags
    @Override
    public Boolean equal(MultiSet<E> aSet) {
        //if the counts aren't identical, can't be equal
        if (this.count != aSet.cardinality()){
            return false;
        }
        //iterate over each element, if unequal, can't be equal
        for (int i = 0; i < this.count; i++) {
            if (!(this.array[i].getKey().equals(aSet.getArray()[i].getKey()))) {
                return false;
            }
        }
        //otherwise true
        return true;
    }

    //I can't get the below to work, so I iterated with typical for loops
    @Override
    public Iterator<E> iterator() {
        return new ArrIterator(this);
    }

    public class ArrIterator implements Iterator<E> {

        Bag<E> arr;
        int coun;

        public ArrIterator(Bag<E> arr) {
            this.arr = arr;
            this.coun = 0;
        }

        @Override
        public boolean hasNext() {
            return this.coun < arr.cardinality();
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoItemException();
            }
            return arr.getArray()[coun+1];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }    

    public static class NoSpaceException extends RuntimeException {
        public NoSpaceException(){
            super("There is not enough room in the set for another element.");
        }
    }

    public static class NotValidTypeException extends NullPointerException {
        public NotValidTypeException(){
            super("You can only add strings to a set.");
        }
    }

    public static class NoItemException extends NullPointerException {
        public NoItemException(){
            super("There is no next element.");
        }
    }
}