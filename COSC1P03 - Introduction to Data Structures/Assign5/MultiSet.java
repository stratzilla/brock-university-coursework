package MULTISET;

public interface MultiSet<E extends Keyed> extends Iterable<E> {
    public int cardinality(); //returns the size of a set
    public int multiplicity(E item); //returns the # of instances of char in set
    public void add(E item); //adds an item to the set
    public Boolean isEmpty(); //checks if set is empty
    public MultiSet<E> union(MultiSet<E> aSet); //performns AuB
    public Boolean equal(MultiSet<E> aSet); //checks equivalency between A and B
    public MultiSet<E> intersection(MultiSet<E> aSet); //performs AnB
    public String printSet(); //prints the set in curly-braces
    public E[] getArray(); //returns this.array for bag
    public Node<E> getNode(); //returns this.node for set
    //public Iterator<E> iterator(); not working
}

//taken from lab/lecture notes, create an LL class
class Node<E extends Keyed> {
    /*I could have done this with only element and next, but sorted insertion
      demands both.
    */
    Node<E> next; //next node
    Node<E> prev; //previous node
    E element; //the element within

    public Node (Node<E> prev, E e, Node<E> next) {
        this.prev = prev;
        this.element = e;
        this.next = next;
    }
}