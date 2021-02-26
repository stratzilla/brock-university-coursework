#!/usr/bin/gnuplot

# verify argument was passed
if (!exists("res")) { exit 1 }

# create PNG output
set terminal png
set output "../results/plots/".res.".png"

# data to plot
in = "../data/".res.".txt"

# we don't need legends or anything
unset key
unset colorbox
unset tics

# set up image
set lmargin screen 0.01
set rmargin screen 0.99
set bmargin screen 0.01
set tmargin screen 0.99
set pm3d map

# greyscale heatmap
set palette rgbformulae 7,5,15

# plot image as matrix
splot in matrix