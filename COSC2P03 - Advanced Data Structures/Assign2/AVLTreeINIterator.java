

import java.util.*;
public class AVLTreeINIterator<E extends Comparable<E>> implements Iterator<E> {
	
    Stack<BinaryNode<E>> theStack;
	
    public AVLTreeINIterator(BinaryNode<E> tree) {
        theStack = new Stack<BinaryNode<E>>();
        if (tree != null) {
            theStack.push(tree);
        }
    }

    @Override
    public boolean hasNext() {
        return theStack.size() > 0;
    }

    @Override
    public E next() {
        E stackVal;
        BinaryNode<E> ptr = theStack.peek();     
        while (hasNext() || ptr != null) {
            if (ptr != null) {
                theStack.push(ptr);
                ptr = ptr.left;
            } else {
                stackVal = ptr.left.info;
            }
        }
        return stackVal;
    }
}