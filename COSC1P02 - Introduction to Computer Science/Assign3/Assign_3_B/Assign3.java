package Assign_3_B;

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
        Picture shrinkPic;
        pic = new Picture();
        shrinkPic = reduce(pic);
        display = new PictureDisplayer(shrinkPic);
        display.close();
        shrinkPic.save();
    }
       
    private Picture reduce(Picture aPic) {
        int width, height; //image dimensions
        int pr = 0, pg = 0, pb = 0; //placeholders
        int y = 0, x = 0; //used later for pixel of new picture
        Picture newPic;
        Color col; //color object
        //I could have done the below two lines as one line, but it would
        //go beyond the width of a page, so I split it into two
        width = aPic.getWidth();
        //if width is odd, make it even
        //this removes build errors because the loops can only assume even
        width = (width % 2 == 0) ? width:width-1;
        //same for height
        height = aPic.getHeight();
        height = (height % 2 == 0) ? height:height-1;
        //make new image of half-size original
        newPic = new Picture(width/2,height/2);
        for (int p = 0; p < height; p = p+2){ //y-val loop
            for (int q = 0; q < width; q = q+2){ //x-val loop
                //loops for (k,l) pixels in 2x2 group
                for (int k = 0; k < 2; k++){ //y-val of inner
                    for (int l = 0; l < 2; l++){ //x-val of inner
                        col = aPic.getPixel(q+l,p+k).getColor();
                        //add to placeholders to be averaged later
                        pr = pr + col.getRed();
                        pg = pg + col.getGreen();
                        pb = pb + col.getBlue();
                    }
                }
                //take mean of RGB values
                //I've casted them as int in case p/4 results in a
                //floating point value, but in testing no errors ever came out
                //so it's just there in case, although it's probably superfluous
                pr = (int)(pr/4);
                pg = (int)(pg/4);
                pb = (int)(pb/4);
                //place values into one pixel in new image
                newPic.getPixel(x,y).setColor(pr,pg,pb);
                //increment x for next pixel
                x++;
                //reset placeholders to prevent integer overflow
                //and for more accurate colors
                pr = 0;
                pg = 0;
                pb = 0;
            }
            //reset x because new line
            x = 0;
            //increment y for subsequent line
            y++;
        }
        return newPic;
    }

    public static void main(String[] args) {Assign3 a = new Assign3(); }    
    
}
