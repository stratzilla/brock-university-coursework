package MULTISET;

import java.util.Iterator;

public class Set<E extends Keyed> implements MultiSet<E> {
    
    int count; //# of elements in set
    Node theNode; //node

    //Default constructor
    public Set() {
        this.count = 0; //set size = 0
        this.theNode = null; //empty
    }

    //prints the set between curly braces
    @Override
    public String printSet() {
        String printer = "{";
        int i = 0;
        Node<E> p = this.theNode;
        while (p != null && i < this.count) {
            printer = printer + p.element.getKey();
            if (i != this.cardinality()-1) {
                printer = printer + ",";
            }
            p = p.next;
            i++;
        } 
        printer = printer + "}";
        return printer;
    }

    //adding an item
    @Override
    public void add(E item) {
        int cntr = 0;
        //if empty, add first element
        if (this.count == 0) {
            this.theNode = new Node(null,item,null);
        } else {
            int j = 0;
            Node<E> m = this.theNode;
            //checks if element exists already, see: set can't have duplic.
            while (m != null && j < this.count && cntr == 0) {
                if (m.element.getKey().compareTo(item.getKey()) == 0) {
                    cntr++; //if duplicate, increment
                }
                j++;
                m = m.next;
            }
            //if cntr was 0, add as usual (means no dupe)
            if (cntr == 0) {
                int i = 0;
                Node<E> q = null;
                Node<E> p = this.theNode;
                //the below dozen lines are for sorted insertion
                while (p != null && i < this.count && //sorry for line split
                        item.getKey().compareTo(p.element.getKey()) >= 0) {
                    q = p;
                    p = p.next;
                }
                //insertion behind
                if (q == null) {
                    this.theNode = new Node(null,item,p);
                    if (p != null) {
                        p.prev = this.theNode;
                    }
                //insertion in front
                } else {
                    q.next = new Node(q,item,p);
                    if (p != null) {
                        p.prev = q.next;
                    }
                }
            }
        }
        //only increment count if cntr was 0
        this.count = (cntr == 0) ? this.count+1 : this.count;
    }

    //returns size of set
    @Override
    public int cardinality() {
        return this.count;
    }

    //returns # of instances of Val in set
    @Override
    public int multiplicity(E item) {
        int n = 0;
        Node<E> p = this.theNode;
        int counter = 0;
        while (p != null && n < this.count) {
            //if count a match, increment counter
            if (p.element.getKey().compareTo(item.getKey()) == 0) {
                counter++;
            }
            p = p.next;
            n++;
        }
        return counter;
    }

    //checks emptiness of set
    @Override
    public Boolean isEmpty() {
        return this.count == 0;
    }

    //not used, forced to include
    @Override
    public E[] getArray() {
        return null;
    }

    /*this just adds all elements from A and B to form C, the .add() method
      ensures no duplicates anyways.
    */
    @Override
    public MultiSet<E> union(MultiSet<E> aSet) {
        MultiSet newSet = new Set();
        MultiSet abSet = aSet;
        Node<E> p = this.theNode;
        int i = 0;
        while (p != null && i < this.count) {
            newSet.add(p.element);
            p = p.next;
            i++;
        }
        p = aSet.getNode();
        i = 0;
        while (p != null && i < aSet.cardinality()) {
            newSet.add(p.element);
            p = p.next;
            i++;
        }
        return newSet;
    }

    //returns node
    @Override
    public Node getNode() {
        return this.theNode;
    }

    //makes set C of elements A in B, again, add() takes care of duplicates
    @Override
    public MultiSet<E> intersection(MultiSet<E> aSet) {
        MultiSet newSet = new Bag();
        Node<E> p = this.theNode;
        int i = 0;
        while (p != null && i < this.count) {
            if (aSet.multiplicity(p.element) == 1) {
                newSet.add(p.element);
            }
            i++;
            p = p.next;
        }
        return newSet;
    }

    //checks equality between A & B
    @Override
    public Boolean equal(MultiSet<E> aSet) {
        if (this.count != aSet.cardinality()){
            return false;
        }
        int i = 0;
        Node<E> p = this.theNode;
        Node<E> q = aSet.getNode();
        while (p != null && q != null && i < this.count) {
            if (p.element.getKey().compareTo(q.element.getKey()) != 0) {
                return false;
            }
            //iterate over both
            p = p.next;
            q = q.next;
            i++;
        }
        return true;
    }

    //again, iterator is not used, see Bag
    @Override
    public Iterator<E> iterator() {
        return new NodeIterator(this);
    }

    public class NodeIterator implements Iterator<E> {

        Set<E> arr;
        int coun;

        public ArrIterator(Set<E> arr) {
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