package Assign_3_C;

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
        SoundPlayer player;
        Sound noisy;
        Sound lessNoisy;
        noisy = new Sound();
        lessNoisy = clean(noisy, 2);
        player = new SoundPlayer(lessNoisy);
        lessNoisy.save();
    }
    
    private Sound clean (Sound original, int factor){
        Sound cleanedSound;
        int sample = 0;
        int soundLength = original.getNumSamples();
        cleanedSound = new Sound(soundLength);
        //the constraints of the for loop don't begin at zero
        //because samples 0, 1, 2, ... , factor don't have enough neighbours
        //same for when the loop ends
        for (int v = 0+factor; v < soundLength-factor; v++){
            //loop to work with samples plusminus factor away
            for (int h = -factor; h <= factor; h++){
                //same as Part B, just add up to be averaged later
                sample = sample + original.getSample(v+h).getAmp();
            }
            //find the mean
            sample = sample/(factor+factor+1);
            //set sample to the mean
            cleanedSound.getSample(v).setAmp(sample);
            //reset sample
            sample = 0;
        }        
        return cleanedSound;
    }

    public static void main(String[] args) {Assign3 a = new Assign3(); }    
    
}
