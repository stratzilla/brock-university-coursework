//Robert Scott
//4582342
//rs09co@brocku.ca
//COSC1P02
//Prof. Hughes and Prof. Foxwell
//November 6th, 2013
//Assignment 2

package Assign_2_C;

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
        drawScene();
    }
    
    private void drawScene() {
        //x and y co-ords used for cloud loop
        int x, y;
        //for random color in drawFlower()
        double randCol;
        Color col;
        //I put this in its own method because otherwise it looks tremendously
        //ugly.
        drawCanvas();
        for (int w = 0; w < 2; w++){
            //ternary operator to set x,y values depending on iteration
            x = (w == 0) ? 75:-25;
            y = (w == 0) ? 100:125;
            //move to appropriate spot
            turt.moveTo(x,y);
            drawCloud();
        }
        turt.moveTo(-85,-60);
        for (int o = 0; o < 10; o++){
            randCol = random()*16777216+1;
            col = new Color((int)randCol);
            drawFlower(col);
            turt.penUp();
            turt.forward(20);
            turt.left(PI/2);
            turt.forward(40);
            turt.right(PI/2);
            turt.penDown();
        }
    }
    
    private void drawCanvas() {
        //pen width to be changed depending on function
        int width;
        //similar to the previous y co-ord
        int m;
        //for color
        Color colour;
        for (int h = 0; h < 2; h++){
            //again use ternary operators to find which vars work
            //best for function
            width = (h == 0) ? 50:250;
            m = (h == 0) ? -125:25;
            //I initially wanted to set colour as a string and then make
            //new color depending on contents of string, but
            //it turts out I can assign a Color object this way as well
            colour = (h == 0) ? green:cyan;
            turt.moveTo(-150,m);
            turt.setPenWidth(width);
            turt.setPenColor(colour);
            turt.penDown();
            turt.forward(300);
            turt.penUp();
        }
    }
    
    private void drawCloud() {
        turt.setPenWidth(5);
        for (int q = 0; q < 3; q++) {
            drawPuff();
            turt.backward(20);
        }
    }
    
    private void drawPuff() {
        for (int i = 0; i < 3; i++){
            drawBowtie(2,white);
            turt.right(2*PI/3);
        }
    }
    
    private void drawFlower(Color color){
        //arbitrary stem length
        int stemlength = 40;
        turt.setPenWidth(1);
        turt.left(3*PI/4);
        for(int j = 0; j < 2; j++) {
            drawBowtie(1, color);
            turt.left(PI/2);
        }
        turt.right(PI/4);
        turt.setPenColor(green);
        turt.penDown();
        turt.forward(stemlength*0.5);
        turt.right(PI/2);
        drawBowtie(1, green);
        turt.left(PI/2);
        turt.penDown();
        turt.forward(stemlength*0.5);
        turt.left(PI/2);
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