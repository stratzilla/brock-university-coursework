
import static java.lang.Math.max;
import java.util.*;

public class AVLTree<E extends Comparable<E>> extends BinarySearchTree<E> implements Iterable<E> {
    BinaryNode<E> myTree; //root contents

    /**
     * method adds data to tree and then balances
     * itself to fit AVL specification
     * @param toInsert is the value to be added
     */
    @Override
    public void add(E toInsert) {
        BinaryNode<E> ptr; //temp pointer
        if (myTree == null) { //if empty tree, make root node
            myTree = new BinaryNode<E>(toInsert);
        } else {
            ptr = myTree; //set pointer to root
            while (true) {
                if (toInsert.compareTo(ptr.info) < 0) { //check if goes left
                    if (ptr.left == null) { //if left is empty, insert
                        ptr.left = new BinaryNode<E>(toInsert);
                        break;
                    } else {
                        ptr = ptr.left; //didn't find empty, retry
                    }
                } else { //if not left then right
                    if (ptr.right == null) { //if right is empty, insert
                        ptr.right = new BinaryNode<E>(toInsert);
                        break;
                    } else {
                        ptr = ptr.right; //didn't find empty, retry
                    }
                }
            }
        }
        myTree = adjustSelf(myTree); //adjust tree root
    }
    
    /**
     * determines height of the tree from 
     * max of all child nodes
     * @return height from chained constructor
     */
    public int height() {
        return height(myTree); //chained
    }
    
    private int height(BinaryNode node) {
        return node == null ? -1 : 1 + max(height(node.left), height(node.right));
    }
    
    /**
     * adjusts tree root to fit AVL convention
     * @param node the node to adjust
     * @return node attribute to be applied on myTree
     */
    private BinaryNode<E> adjustSelf(BinaryNode<E> node) {
        if (node != null) { //don't adjust on empty child
            BinaryNode<E> tempNode; //temp
            if (height(node.left) - height(node.right) > 1) { //if left heavy
                tempNode = node.left; //temp
                //if child of child is left heavy
                if (height(tempNode.left) >= height(tempNode.right)) { 
                    node = rotate(node, "Left"); //single rotation on left child
                } else {
                    node = doubleRot(node, "Left"); //double rotation on left child
                }
            } else if (height(node.right) - height(node.left) > 1) { //if right heavy
                tempNode = node.right; //temp
                //if child of child is right heavy
                if (height(tempNode.right) >= height(tempNode.left)) {
                    node = rotate(node, "Right"); //single rotation on right child
                } else {
                    node = doubleRot(node, "Right"); //double rotation on right child
                }
            }
        }
        return node;
    }
    
    /**
     * adjusts child to fit AVL convention
     * @param parent the node to rotate
     * @param direction if left or right rotation
     * @return node attribute to be applied on myTree
     */
    private BinaryNode<E> rotate(BinaryNode<E> parent, String direction) {
        if (direction.equals("Left")) {
            /* close to book/lecture slides
             * move around elements to fit convention
             */
            BinaryNode<E> adoptiveParent = parent.left;
            parent.left = adoptiveParent.right;
            adoptiveParent.right = parent;
            return adoptiveParent;
        } else {
            //same
            BinaryNode<E> adoptiveParent = parent.right;
            parent.right = adoptiveParent.left;
            adoptiveParent.left = parent;
            return adoptiveParent;
        }
    }
    
    /**
     * adjusts child to fit AVL convention
     * @parent the node to rotate
     * @direction if left or right rotation
     * @return node attribute to be applied on myTree
     */
    private BinaryNode<E> doubleRot(BinaryNode<E> parent, String direction) {
        if (direction.equals("Left")) {
            parent.left = rotate(parent.left, "Right");
            return rotate(parent, "Left");
        } else {
            parent.right = rotate(parent.right, "Left");  
            return rotate(parent, "Right");
        }
    }
    
    /**
     * create preorder iterator
     * @return iterator
     */
    public Iterator<E> preOrder() {
        return new AVLTreePOIterator<E>(myTree);
    }
    
    /**
     * create inorder iterator
     * @return iterator
     */
    public Iterator<E> inOrder() {
        return new AVLTreeINIterator<E>(myTree);
    }

    /**
     * create BFS iterator
     * @return iterator
     */
    public Iterator<E> iterator() {
            return new BinaryTreeIteratorBFS<E>(myTree);
    }
}