

import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

public class Assign2 {
    
    Scanner in;
    BinarySearchTree tree;
    long beginTime = 0;
    long endTime = 0;
    long totTime = 0;
    int it = 0;
    Random randGen = new Random();
    int randomNum = 0;
    
    public Assign2() {
        int choice = -1;
        in = new Scanner(System.in);
        do {
            choice = menu();
            switch (choice) {
                case 1: //Create BST tree
                    if (!alreadyMade(tree)) { break; } //if tree exists, break
                    tree = new BinarySearchTree();
                    System.out.println("\nBinary Search Tree successfully created.");
                    break;
                case 2: //Create AVL tree
                    if (!alreadyMade(tree)) { break; } //if tree exists, break
                    tree = new AVLTree();
                    System.out.println("\nAVL Tree successfully created.");
                    break;
                case 3: //Delete tree
                    if (!notMade(tree)) { break; } //can't delete null tree
                    tree = null;
                    System.out.println("\nThe tree has been reset.");
                    break;
                case 4: //add integers to tree
                    if (!notMade(tree)) { break; } //can't add to null tree
                    if (!alreadyPop(tree)) { break; } //can't mix scan types
                    System.out.println("\nEnter integers to be put into your tree. Type -1 to stop.\n");
                    System.out.print("Integer: ");
                    while (in.hasNextInt()) {
                        int theInt = in.nextInt();
                        if (theInt == -1) { break; }
                        System.out.print("Integer: ");
                        tree.add(theInt);
                    }
                    break;
                case 5: //add strings to tree
                    if (!notMade(tree)) { break; } //can't add to null truee
                    if (!alreadyPop(tree)) { break; } //can't mix scan types
                    System.out.println("\nEnter strings to be put into your tree. Type -1 to stop.\n");
                    while (in.hasNextLine()) {
                        String theString = in.nextLine();
                        if (theString.equals("-1")) { break; }
                        System.out.print("String: ");
                        tree.add(theString);
                    }
                    System.out.println();
                    break;  
                case 6: //display tree with BFS
                    if (!notMade(tree)) { break; } //can't display null tree
                    if (!notPop(tree)) { break; } //can't display empty tree
                    Iterator BFSite = tree.iterator();
                    System.out.println(printTree(BFSite));
                    break;
                case 7: //display tree with preOrder
                    if (!notMade(tree)) { break; } //can't display null tree
                    if (!checkAVL(tree)) { break; } //can't preOrder on BST
                    if (!notPop(tree)) { break; } //can't display empty tree
                    Iterator POIte = ((AVLTree)tree).preOrder(); //casted otherwise doesn't work
                    System.out.println(printTree(POIte));
                    break;
                case 8: //display tree with inOrder
                    if (!notMade(tree)) { break; } //can't display null tree
                    if (!checkAVL(tree)) { break; } //can't inOrder on BST
                    if (!notPop(tree)) { break; } //can't display empty tree
                    //Iterator IOIte = tree.inOrder();
                    //System.out.println(printTree(IOIte)+"\n");
                    System.out.println("\nOperation not supported.");
                    break;
                case 9: //timed insertion of 10,000 random 3-digit ints
                    if (!notMade(tree)) { break; } //can't add to null tree
                    if (!alreadyPop(tree)) { break; } //can't mix scan types
                    System.out.println("\nInserting 10,000 random five digit integers...");
                    for (int i = 0; i < 10000; i++, it++) {
                        randomNum = randGen.nextInt(100) + 1;
                        beginTime = System.nanoTime();
                        tree.add(randomNum);
                        endTime = System.nanoTime();
                        totTime += (endTime-beginTime);
                    }
                    System.out.println("\nAverage: "+totTime/it+"ns to insert.");
                    break;
                case 0: //timed insertion of 10,000 random strings of a length 1-5
                    if (!notMade(tree)) { break; } //can't add to null tree
                    if (!alreadyPop(tree)) { break; } //can't mix scan types
                    System.out.println("\nInserting 10,000 random strings between one and five characters long...");
                    for (int i = 0; i < 10000; i++, it++) {
                        StringBuilder app = new StringBuilder();
                        for (int j = 0; j < randGen.nextInt(5)+1; j++) {
                            char c = (char)(randGen.nextInt(26)+'a');
                            app.append(c);
                        }
                        beginTime = System.nanoTime();
                        tree.add(app.toString());
                        endTime = System.nanoTime();
                        totTime += (endTime-beginTime);
                    }
                    System.out.println("\nAverage: "+totTime/it+"ns to insert.");
                    break;                    
                case -1: //Quit
                    System.out.println();
                    break;                 
            }
        } while (choice != -1);
    }
    
    private boolean checkAVL(BinarySearchTree tree) {
        if(tree instanceof AVLTree == false) {
            System.out.println("\nThis traversal method can only be done on AVL trees.");
            return false;
        }
        return true;
    }
    
    private boolean alreadyMade(BinarySearchTree tree) {
        if(tree != null) {
            System.out.println("\nA tree already exists.");
            return false;
        }
        return true;
    }
    
    private boolean notMade(BinarySearchTree tree) {
        if(tree == null) {
            System.out.println("\nThis tree has not yet been initialized.");
            return false;
        }
        return true;
    }
    
    private boolean notPop(BinarySearchTree tree) {
        BinaryNode check;
        if(tree instanceof AVLTree) {
            check = ((AVLTree)tree).myTree;
        } else {
            check = tree.myTree;
        }
        if(check == null) {
            System.out.println("\nThis tree has not yet been populated.");
            return false;
        }
        return true;
    }
    
    private boolean alreadyPop(BinarySearchTree tree) {
        BinaryNode check;
        if(tree instanceof AVLTree) {
            check = ((AVLTree)tree).myTree;
        } else {
            check = tree.myTree;
        }
        if(check != null) {
            System.out.println("\nThis tree is already populated with elements.");
            return false;
        }
        return true;
    }
    
    private String printTree(Iterator theIte) {
        String values = "\nValues: ";
        while (theIte.hasNext()) {
            values += theIte.next()+", ";
        }
        return values.substring(0,values.length()-2);
    }
    
    private int menu() {
        System.out.print("\n1. Create Binary Search Tree\t\t");
        System.out.print("2. Create AVL Tree\t\t");
        System.out.print("3. Clear tree\n");
        System.out.print("4. Add Integers to Tree\t\t\t");
        System.out.print("5. Add Strings to Tree\n");
        System.out.print("6. Display tree with BFS\t\t");
        System.out.print("7. Display tree with pre-order\t");
        System.out.print("8. Display tree with in-order\n");
        System.out.print("9. Timed Insertion of Integers\t\t");
        System.out.print("0. Timed Insertion of Strings\n");
        System.out.println("-1. Quit\n");
        System.out.print("Option: ");
        return in.nextInt();
    }
    
    public static void main(String[] args) {
        Assign2 a = new Assign2();
    }

}