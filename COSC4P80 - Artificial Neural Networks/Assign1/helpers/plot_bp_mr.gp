#!/usr/bin/gnuplot

# create PNG output
set terminal png
set output "../results/plots/bp-mr-plot.png"

# set axis titles
set xlabel "Epoch"
set ylabel "MSE" offset 2

# separate CSV by commas
set datafile separator comma

# load CSV as lines
mr0001 = "../results/bp/mr0001.csv" 
mr0010 = "../results/bp/mr0010.csv"
mr0100 = "../results/bp/mr0100.csv"
mr1000 = "../results/bp/mr1000.csv"

# get column count
cols_0001 = int(system('head -1 '.mr0001.' | wc -w'))+1
cols_0010 = int(system('head -1 '.mr0010.' | wc -w'))+1
cols_0100 = int(system('head -1 '.mr0100.' | wc -w'))+1
cols_1000 = int(system('head -1 '.mr1000.' | wc -w'))+1

# plot each line
plot mr0001 using cols_0001 with lines linecolor 1 linewidth 3 title \
	 "MR=0.001", \
	 mr0010 using cols_0010 with lines linecolor 2 linewidth 3 title \
	 "MR=0.010", \
	 mr0100 using cols_0100 with lines linecolor 3 linewidth 3 title \
	 "MR=0.100", \
	 mr1000 using cols_1000 with lines linecolor 4 linewidth 3 title \
	 "MR=1.000"