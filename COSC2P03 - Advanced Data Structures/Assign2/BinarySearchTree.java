import java.util.*;
public class BinarySearchTree<E extends Comparable<E>> implements Iterable<E> {
	BinaryNode<E> myTree;
	public BinarySearchTree() {
		
	}
	
	public void add(E toInsert) {
		BinaryNode<E> ptr;
		if (myTree==null) {
			myTree=new BinaryNode<E>(toInsert);
		}
		else {
			ptr=myTree;
			while (true) {
				if (toInsert.compareTo(ptr.info)>0) { //Right branch
					if (ptr.right==null) { //Stick it here
						ptr.right=new BinaryNode<E>(toInsert);
						break;
					}
					else ptr=ptr.right;
				}
				else { //Left branch
					if (ptr.left==null) { //Stick it here
						ptr.left=new BinaryNode<E>(toInsert);
						break;
					}
					else ptr=ptr.left;
				}
			}
		}
	}
	
	//Recursive method (from the book)
	private BinaryNode<E> findMin(BinaryNode<E> node) {
		if (node==null) return null;
		else if (node.left==null) return node;
		return findMin(node.left);
	}
	
	//Iterative method (from the book)
	private BinaryNode<E> findMax(BinaryNode<E> node) {
		if (node!=null) while (node.right!=null) node=node.right;
		return node;
	}
	
	//From the book
	private BinaryNode<E> remove(E val, BinaryNode<E> tree) {
		int direction; //Used for telling left/right (less-than/greater-than)
		if (tree==null) { //Value to be removed is not present
			return null;
		}
		direction=val.compareTo(tree.info);
		if (direction<0) { //Left branch
			tree.left=remove(val,tree.left);
		}
		else if (direction>0) { //Right branch
			tree.right=remove(val,tree.right);
		}
		else if (tree.left!=null && tree.right!=null) { //Matches current node; two children
			tree.info=findMin(tree.right).info;
			tree.right=remove(tree.info,tree.right);
		}
		else
			tree=(tree.left!=null)?tree.left:tree.right;
		return tree;
	}
	
	public void remove(E val) {
		BinaryNode<E> discardable=remove(val,myTree);
		System.out.println(":"+discardable.info+":");
	}
	
	public BinaryNode<E> getRoot() {
		return myTree;
	}
	
	public Iterator<E> iterator() {
		return new BinaryTreeIteratorBFS<E>(myTree);
	}

}
