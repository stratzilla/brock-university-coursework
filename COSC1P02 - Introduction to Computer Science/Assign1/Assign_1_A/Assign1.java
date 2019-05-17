package Assign_1_A;

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
    
 //constructor (aren't these also called test drivers?)
    public Assign1 () {
        display = new TurtleDisplayer();
        turt = new Turtle(FAST);
        display.placeTurtle(turt);
        turt.moveTo(-80,0);
        turt.setPenWidth(10);
        turt.penDown();
        //perform Squares() function
        Squares();
        display.close();
}
    
    //Assign1 Part A
    private void Squares () {
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
    }

    public static void main(String[] args) { Assign1 a = new Assign1(); }
}
