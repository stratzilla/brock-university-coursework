#!/usr/bin/gnuplot

# create PNG output
set terminal png
set output "../results/plots/pso-ss-plot.png"

# set axis titles
set xlabel "Epoch"
set ylabel "MSE" offset 2

# separate CSV by commas
set datafile separator comma

# load CSV as lines
ss010 = "../results/pso/SS010.csv"
ss025 = "../results/pso/SS025.csv"
ss050 = "../results/pso/SS050.csv"
ss075 = "../results/pso/SS075.csv"
ss100 = "../results/pso/SS100.csv"

# get column count
cols_10 = int(system('head -1 '.ss010.' | wc -w'))+1
cols_25 = int(system('head -1 '.ss025.' | wc -w'))+1
cols_50 = int(system('head -1 '.ss050.' | wc -w'))+1
cols_75 = int(system('head -1 '.ss075.' | wc -w'))+1
cols_100 = int(system('head -1 '.ss100.' | wc -w'))+1

# plot each line
plot ss010 using cols_10 with lines linecolor 1 linewidth 3 title "SS=10", \
	 ss025 using cols_25 with lines linecolor 2 linewidth 3 title "SS=25", \
	 ss050 using cols_50 with lines linecolor 3 linewidth 3 title "SS=50", \
	 ss075 using cols_75 with lines linecolor 4 linewidth 3 title "SS=75", \
	 ss100 using cols_100 with lines linecolor 5 linewidth 3 title "SS=100"