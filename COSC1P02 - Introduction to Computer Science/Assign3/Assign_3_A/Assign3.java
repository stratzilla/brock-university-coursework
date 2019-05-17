package Assign_3_A;

import Media.*;
import Media.Sound;
import java.awt.*;
import static java.lang.Math.*;
import static java.awt.Color.*;

//There was a little bit of weirdness with this assignment, as Parts B and C
//assume everything is perfect with the user defined file. Part B in particular
//assumes the image has dimensions of even pixel lengths, but when an odd
//dimension is put in, it results in a build error. To circumvent this,
//if the inputted image has odd dimensions, crop it by 1 pixel to make it even

//Secondly, for Part C I can't really tell the difference between old and new
//sound files. They sound more or less the same, although the second is more
//muted. I have no evidence to suggest it even worked other than my analysis
//of the waveform of both files. The second file has a more smooth wave, so
//I'm confident it has worked as intended.

public class Assign3 {
    
    public Assign3 () {    
        PictureDisplayer display;
        Picture pic;
        pic = new Picture();
        makeNegative(pic);
        display = new PictureDisplayer(pic);
        display.close();
        pic.save();
    }
    
    private void makeNegative (Picture aPic) {
        int width, height; //image dimensions
        int [] cArr = new int[3]; //array for RGB-vals
        Color col; //color object
        width = aPic.getWidth();
        height = aPic.getHeight();
        for (int i = 0; i < height; i++){ //y-val loop
            for (int j = 0; j < width; j++){ //x-val loop
                col = aPic.getPixel(j,i).getColor(); //get pixel RGB val
                //I could have just used three variables, but
                //I think it's a little cleaner with an array
                cArr[0] = 255-col.getRed();
                cArr[1] = 255-col.getGreen();
                cArr[2] = 255-col.getBlue();
                //set colors to array elements
                aPic.getPixel(j,i).setColor(cArr[0],cArr[1],cArr[2]);
            }
        }
    } 

    public static void main(String[] args) {Assign3 a = new Assign3(); }    
    
}
