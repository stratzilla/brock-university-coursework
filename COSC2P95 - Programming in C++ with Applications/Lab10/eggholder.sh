#!/usr/bin/gnuplot -persist

set terminal x11
set border 3
set xlabel "x" textcolor lt 3
set ylabel "y" textcolor lt 3
set isosamples 100
set view 35,37
unset key
set xrange [-512:512]
set yrange [-512:512]
splot -(y+47)*sin(sqrt(abs((x/2)+y+47)))-x*sin(sqrt(abs(x-y-47)))
