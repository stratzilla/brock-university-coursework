#!/usr/bin/gnuplot -persist

set terminal postscript eps # set output type
set output "plotted.eps" # set output name
set border 3 # set border size
set xlabel "x" textcolor lt 3 # set axes labels
set ylabel "y" textcolor lt 3
set isosamples 100 # how many samples
set view 35,37 # default view
unset key # remove legend
set xrange [-512:512] # set bounds for function
set yrange [-512:512]
splot -(y+47)*sin(sqrt(abs((x/2)+y+47)))-x*sin(sqrt(abs(x-y-47))) title "2D Eggholder Function" # plot the function