#!/usr/bin/gnuplot

# create PNG output
set terminal png
set output "../results/plots/bp-lr-plot.png"

# set axis titles
set xlabel "Epoch"
set ylabel "MSE" offset 2

# separate CSV by commas
set datafile separator comma

# load CSV as lines
lr0001 = "../results/bp/LR0001.csv" 
lr0010 = "../results/bp/LR0010.csv"
lr0100 = "../results/bp/LR0100.csv"
lr1000 = "../results/bp/LR1000.csv"

# get column count
cols_0001 = int(system('head -1 '.lr0001.' | wc -w'))+1
cols_0010 = int(system('head -1 '.lr0010.' | wc -w'))+1
cols_0100 = int(system('head -1 '.lr0100.' | wc -w'))+1
cols_1000 = int(system('head -1 '.lr1000.' | wc -w'))+1

# plot each line
plot lr0001 using cols_0001 with lines linecolor 1 linewidth 3 title \
	 "LR=0.001", \
	 lr0010 using cols_0010 with lines linecolor 2 linewidth 3 title \
	 "LR=0.010", \
	 lr0100 using cols_0100 with lines linecolor 3 linewidth 3 title \
	 "LR=0.100", \
	 lr1000 using cols_1000 with lines linecolor 4 linewidth 3 title \
	 "LR=1.000"