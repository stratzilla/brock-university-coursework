#!/usr/bin/gnuplot

# create PNG output
set terminal png
set output "../results/plots/bp-hl-plot.png"

# set axis titles
set xlabel "Epoch"
set ylabel "MSE" offset 2

# separate CSV by commas
set datafile separator comma

# load CSV as lines
hl05 = "../results/bp/HL05.csv" 
hl10 = "../results/bp/HL10.csv"
hl15 = "../results/bp/HL15.csv"
hl20 = "../results/bp/HL20.csv"

# get column count
cols_05 = int(system('head -1 '.hl05.' | wc -w'))+1
cols_10 = int(system('head -1 '.hl10.' | wc -w'))+1
cols_15 = int(system('head -1 '.hl15.' | wc -w'))+1
cols_20 = int(system('head -1 '.hl20.' | wc -w'))+1

# plot each line
plot hl05 using cols_05 with lines linecolor 1 linewidth 3 title "HL=05", \
	 hl10 using cols_10 with lines linecolor 2 linewidth 3 title "HL=10", \
	 hl15 using cols_15 with lines linecolor 3 linewidth 3 title "HL=15", \
	 hl20 using cols_20 with lines linecolor 4 linewidth 3 title "HL=20"