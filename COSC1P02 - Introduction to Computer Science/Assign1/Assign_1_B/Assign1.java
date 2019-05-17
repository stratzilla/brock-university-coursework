package Assign_1_B;

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
        //perform function DoubleRow()
        DoubleRow();
        display.close();
}
    
    //Assign1 Part B
    private void DoubleRow () {
        //loop for # of rows
        for (int j = 0; j < 2; j++){
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
            //if row 2
            //it's j == 0 because the transition to row 2 happens in row 1
            if (j == 0){
                turt.penUp();
                turt.right(PI/2);
                turt.forward(30);
                turt.right(PI/2);
                turt.forward(10);
                turt.penDown();
            }
        }       
    }
        
    public static void main(String[] args) { Assign1 a = new Assign1(); }
}
