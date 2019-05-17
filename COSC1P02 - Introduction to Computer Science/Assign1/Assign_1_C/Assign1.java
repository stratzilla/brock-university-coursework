package Assign_1_C;

//I did this in NP++ and Netbeans at home
//When transferring to DrJava, it screws up some of the formatting I had
//Sorry if some parentheses are off by a couple of spaces
//but DrJava is unwieldy to use and I'm not sure how to fix them

import static java.lang.Math.*;
import Media.*;
import static Media.Turtle.*;

public class Assign1 {

    private TurtleDisplayer display;
    private Turtle          turt;
 //q is a variable that alternates between 1 and 0
 //this determines whether it is an even-numbered row, or an odd-numbered row
    int q = 0;
    
 //constructor (aren't these also called test drivers?)
    public Assign1 () {
        display = new TurtleDisplayer();
        turt = new Turtle(FAST);
        display.placeTurtle(turt);
        turt.moveTo(-80,0);
        turt.setPenWidth(10);
        turt.penDown();
        //peform function Board()
        Board();
        display.close();
}
        
    //Assign1 Part C
    private void Board () {
        //need to move so board is centered
        turt.penUp();
        turt.moveTo(-80,80);
        turt.penDown();
        //loop for # of rows
        for (int j = 0; j < 8; j++){
            //loop for # of squares
            for (int k = 0; k < 4; k++){
                //loop for # of 10x10 "pixels" per square
                for (int i = 0; i <= 4; i++){
                    turt.forward(10);
                    //if turn 1-4
                    if (i < 4){
                        turt.right(PI/2);   
                    //if turn 5, make space between squares
                    } else {
                        turt.penUp();
                        turt.forward(30);
                        turt.penDown();
                    }
                }
            } 
            //if rows 2, 4, 6, etc
            if (q == 0){
                turt.penUp();
                turt.right(PI/2);
                turt.forward(30);
                turt.right(PI/2);
                turt.forward(10);
                turt.penDown();
                //increment q because next row we want q == 1
                q++;
            //if rows 3, 5, 7, etc
            } else if (q == 1){
                turt.penUp();
                turt.right(PI/2);
                turt.backward(10);
                turt.right(PI/2);
                turt.forward(10);
                turt.penDown();
                //decrement q because next row we need q == 0
                q--;
            }
        }       
    }

    public static void main(String[] args) { Assign1 a = new Assign1(); }
}
