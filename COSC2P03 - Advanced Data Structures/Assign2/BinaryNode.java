public class BinaryNode<E> {
	public E info;
	public BinaryNode<E> left;
	public BinaryNode<E> right;
	
	public int height; //Used for AVL
	public boolean flag; //Only used for PostOrder iterator
	
	public BinaryNode(E dealie) {
		this.info=dealie;
	}
	
}
