
import java.util.*;
public class AVLTreePOIterator<E extends Comparable<E>> implements Iterator<E> {
	
    Stack<BinaryNode<E>> theStack;
	
    /**
     * creates a stack of the tree
     * @param tree the tree in which to traverse
     */
    public AVLTreePOIterator(BinaryNode<E> tree) {
        theStack = new Stack<BinaryNode<E>>();
        if (tree != null) { //if tree exists
            theStack.push(tree); //create stack
        }
    }

    /**
     * returns true if the stack is not empty
     */
    @Override
    public boolean hasNext() {
        return theStack.size() > 0;
    }

    /**
     * returns value for next in iterator
     */
    @Override
    public E next() {
        E stackVal; //temp
        BinaryNode<E> ptr = theStack.peek(); //temp = root
        if (this.hasNext()) {          
            stackVal = theStack.pop().info; //return root
            /**
             * since preorder is left focused
             * put right child under left child
             */
            if (ptr.right != null) {
                theStack.push(ptr.right);
            }
            if (ptr.left != null) {
                theStack.push(ptr.left);
            }
            return stackVal; //return the parent
        }
        throw new NoSuchElementException();
    }
}