package Assign_2_A;

import Media.*;
import static Media.Turtle.*;
import java.awt.*;
import static java.lang.Math.*;
import static java.awt.Color.*;

//The assignment sheet didn't say to use relative positioning, so I didn't.
//There was also no indication of the co-ordinates to put the
//clouds or flowers, so I put them to what it appears in Figure 2
//A lot of Part C relies on positioning and setting pen characteristics
//and it's something I couldn't put in a loop, so it looks really messy.

public class Assign2 {
    
    TurtleDisplayer display;
    Turtle turt;
    
    public Assign2 () {    
        display = new TurtleDisplayer();
        turt = new Turtle(FAST);
        turt.setPenWidth(1);
        display.placeTurtle(turt);
        drawBowtie(10, black);
    }

    private void drawBowtie(int scale, Color color) {
        //initialize a length relevant to scale
        int length = 10*scale;
        turt.penDown();
        turt.setPenColor(color);
        turt.right(PI/6);
        for(int t = 0; t < 2; t++) {
            drawTriangle(length);
            turt.left(PI);
        }
        turt.left(PI/6);
        turt.penUp();
    }
    
    private void drawTriangle(int length){
        for(int s = 0; s < 3; s++) {
            //pretty self explanatory
            turt.forward(length);
            turt.left(2*PI/3);    
        }
    }
    
    public static void main(String[] args) {Assign2 a = new Assign2(); }    
    
}
