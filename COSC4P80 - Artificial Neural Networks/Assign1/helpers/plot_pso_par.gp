#!/usr/bin/gnuplot

# create PNG output
set terminal png
set output "../results/plots/pso-par-plot.png"

# set axis titles
set xlabel "Epoch"
set ylabel "MSE" offset 2

# separate CSV by commas
set datafile separator comma

# load CSV as lines
par1 = "../results/pso/PAR1.csv" 
par2 = "../results/pso/PAR2.csv"
par3 = "../results/pso/PAR3.csv"
par4 = "../results/pso/PAR4.csv"
par5 = "../results/pso/PAR5.csv"

# get column count
cols_1 = int(system('head -1 '.par1.' | wc -w'))+1
cols_2 = int(system('head -1 '.par2.' | wc -w'))+1
cols_3 = int(system('head -1 '.par3.' | wc -w'))+1
cols_4 = int(system('head -1 '.par4.' | wc -w'))+1
cols_5 = int(system('head -1 '.par5.' | wc -w'))+1

# plot each line
plot par1 using cols_1 with lines linecolor 1 linewidth 3 \
		title "w=0.25, c1=1.35, c2=1.05", \
	 par2 using cols_2 with lines linecolor 2 linewidth 3 \
		title "w=0.35, c1=1.45, c2=1.15", \
	 par3 using cols_3 with lines linecolor 3 linewidth 3 \
		title "w=0.45, c1=1.55, c2=1.25", \
	 par4 using cols_4 with lines linecolor 4 linewidth 3 \
		title "w=0.55, c1=1.65, c2=1.35", \
	 par5 using cols_5 with lines linecolor 5 linewidth 3 \
		title "w=0.65, c1=1.75, c2=1.45"