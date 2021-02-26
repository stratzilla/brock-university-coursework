#!/bin/bash

# how many networks trained per test, how many concurrently
max_runs=30
max_concurrent_runs=10

# tests as array to make this file prettier
bp_hl_tests=("HL05" "HL10" "HL15" "HL20")
bp_lr_tests=("LR0001" "LR0010" "LR0100" "LR1000")
bp_mr_tests=("MR0001" "MR0010" "MR0100" "MR1000")
pso_hl_tests=("HL05" "HL10" "HL15" "HL20")
pso_ss_tests=("SS010" "SS025" "SS050" "SS075" "SS100")
pso_par_tests=("PAR1" "PAR2" "PAR3" "PAR4" "PAR5")

# remove old results and set up for new ones
for i in "${bp_hl_tests[@]}"; do
	rm -rf ../results/bp/hl/${i}
	mkdir -p ../results/bp/hl/${i}
done
for i in "${bp_lr_tests[@]}"; do
	rm -rf ../results/bp/lr/${i}
	mkdir -p ../results/bp/lr/${i}
done
for i in "${bp_mr_tests[@]}"; do
	rm -rf ../results/bp/mr/${i}
	mkdir -p ../results/bp/mr/${i}
done
for i in "${pso_hl_tests[@]}"; do
	rm -rf ../results/pso/hl/${i}
	mkdir -p ../results/pso/hl/${i}
done
for i in "${pso_ss_tests[@]}"; do
	rm -rf ../results/pso/ss/${i}
	mkdir -p ../results/pso/ss/${i}
done
for i in "${pso_par_tests[@]}"; do
	rm -rf ../results/pso/par/${i}
	mkdir -p ../results/pso/par/${i}
done

sleep 0.5

printf "\nGetting results for BP-NN for hidden layer size...";

for j in $(seq 1 ${#bp_hl_tests[@]}); do
	for i in $(seq 1 $max_runs); do
		../code/backprop_network.py 1 ${j} 3 2 > \
			../results/bp/hl/${bp_hl_tests[${j}-1]}/${i}.csv &
		if [ $(( $i % $max_concurrent_runs )) == 0 ]; then
			wait
		fi
	done
done

wait

printf " done! \nGetting results for BP-NN for learning rate...";

for j in $(seq 1 ${#bp_lr_tests[@]}); do
	for i in $(seq 1 $max_runs); do
		../code/backprop_network.py 1 2 ${j} 2 > \
			../results/bp/lr/${bp_lr_tests[${j}-1]}/${i}.csv &
		if [ $(( $i % $max_concurrent_runs )) == 0 ]; then
			wait
		fi
	done
done

wait

printf " done! \nGetting results for BP-NN for momentum rate...";

for j in $(seq 1 ${#bp_mr_tests[@]}); do
	for i in $(seq 1 $max_runs); do
		../code/backprop_network.py 1 2 3 ${j} > \
			../results/bp/mr/${bp_mr_tests[${j}-1]}/${i}.csv &
		if [ $(( $i % $max_concurrent_runs )) == 0 ]; then
			wait
		fi
	done
done

wait

printf " done! \n\nGetting results for PSO-NN for hidden layer size...";

for j in $(seq 1 ${#pso_hl_tests[@]}); do
	for i in $(seq 1 $max_runs); do
		../code/particle_network.py 1 ${j} 3 2 > \
			../results/pso/hl/${pso_hl_tests[${j}-1]}/${i}.csv &
		if [ $(( $i % $max_concurrent_runs )) == 0 ]; then
			wait
		fi
	done
done

wait

printf " done! \nGetting results for PSO-NN for swarm size...";

for j in $(seq 1 ${#pso_ss_tests[@]}); do
	for i in $(seq 1 $max_runs); do
		../code/particle_network.py 1 2 ${j} 2 > \
			../results/pso/ss/${pso_ss_tests[${j}-1]}/${i}.csv &
		if [ $(( $i % $max_concurrent_runs )) == 0 ]; then
			wait
		fi
	done
done

wait

printf " done! \nGetting results for PSO-NN for swarm parameters...";

for j in $(seq 1 ${#pso_par_tests[@]}); do
	for i in $(seq 1 $max_runs); do
		../code/particle_network.py 1 2 3 ${j} > \
			../results/pso/par/${pso_par_tests[${j}-1]}/${i}.csv &
		if [ $(( $i % $max_concurrent_runs )) == 0 ]; then
			wait
		fi
	done
done

wait

printf " done! \n\nConcatenating results...";

# concatenate all runs into one CSV file
for i in "${bp_hl_tests[@]}"; do
	./concat_csv.py ../results/bp/hl/${i}/ ../../${i}.csv
done
for i in "${bp_lr_tests[@]}"; do
	./concat_csv.py ../results/bp/lr/${i}/ ../../${i}.csv
done
for i in "${bp_mr_tests[@]}"; do
	./concat_csv.py ../results/bp/mr/${i}/ ../../${i}.csv
done
for i in "${pso_hl_tests[@]}"; do
	./concat_csv.py ../results/pso/hl/${i}/ ../../${i}.csv
done
for i in "${pso_ss_tests[@]}"; do
	./concat_csv.py ../results/pso/ss/${i}/ ../../${i}.csv
done
for i in "${pso_par_tests[@]}"; do
	./concat_csv.py ../results/pso/par/${i}/ ../../${i}.csv
done

sleep 1

printf " done! \nCreating a master list of runs...";

# concatenate all CSV files into one master file
printf "BP-NN - Hidden Layer Data\n" > ../results/bp_hidden_layer_data.csv
for i in "${bp_hl_tests[@]}"; do
	printf "\n${i}\n" >> ../results/bp_hidden_layer_data.csv
	cat ../results/bp/${i}.csv >> ../results/bp_hidden_layer_data.csv
done
printf "BP-NN - Learning Rate Data\n" > ../results/bp_learning_rate_data.csv
for i in "${bp_lr_tests[@]}"; do
	printf "\n${i}\n" >> ../results/bp_learning_rate_data.csv
	cat ../results/bp/${i}.csv >> ../results/bp_learning_rate_data.csv
done
printf "BP-NN - Momentum Rate Data\n" > ../results/bp_momentum_rate_data.csv
for i in "${bp_mr_tests[@]}"; do
	printf "\n${i}\n" >> ../results/bp_momentum_rate_data.csv
	cat ../results/bp/${i}.csv >> ../results/bp_momentum_rate_data.csv
done
printf "PSO-NN - Hidden Layer Data\n" > ../results/pso_hidden_layer_data.csv
for i in "${pso_hl_tests[@]}"; do
	printf "\n${i}\n" >> ../results/pso_hidden_layer_data.csv
	cat ../results/pso/${i}.csv >> ../results/pso_hidden_layer_data.csv
done
printf "PSO-NN - Swarm Size Data\n" > ../results/pso_swarm_size_data.csv
for i in "${pso_ss_tests[@]}"; do
	printf "\n${i}\n" >> ../results/pso_swarm_size_data.csv
	cat ../results/pso/${i}.csv >> ../results/pso_swarm_size_data.csv
done
printf "PSO-NN - Parameter Data\n" > ../results/pso_parameter_data.csv
for i in "${pso_par_tests[@]}"; do
	printf "\n${i}\n" >> ../results/pso_parameter_data.csv
	cat ../results/pso/${i}.csv >> ../results/pso_parameter_data.csv
done

sleep 1

printf " done! \nMaking plots...";

# create plot of MSE over epochs
gnuplot plot_bp_hl.gp
gnuplot plot_bp_lr.gp
gnuplot plot_bp_mr.gp
gnuplot plot_pso_hl.gp
gnuplot plot_pso_ss.gp
gnuplot plot_pso_par.gp

sleep 0.5

printf " done! \nCleaning up...";

# clean up redundant files
rm -r ../results/bp
rm -r ../results/pso

sleep 0.5

printf " done! \n\nCompleted collecting training data and plots!\n\n";

exit 0
