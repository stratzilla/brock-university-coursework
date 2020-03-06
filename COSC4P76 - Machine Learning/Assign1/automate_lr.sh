#!/bin/sh

echo "\nCollecting data to determine optimal LR (LOG).";

./backprop_nn.py log 15 0.010 0.00 100 1.00 > ./results/log/lr/1.csv &
./backprop_nn.py log 15 0.010 0.00 100 1.00 > ./results/log/lr/2.csv &
./backprop_nn.py log 15 0.010 0.00 100 1.00 > ./results/log/lr/3.csv &
./backprop_nn.py log 15 0.010 0.00 100 1.00 > ./results/log/lr/4.csv &
./backprop_nn.py log 15 0.010 0.00 100 1.00 > ./results/log/lr/5.csv &

wait

./backprop_nn.py log 15 0.025 0.00 100 1.00 > ./results/log/lr/6.csv &
./backprop_nn.py log 15 0.025 0.00 100 1.00 > ./results/log/lr/7.csv &
./backprop_nn.py log 15 0.025 0.00 100 1.00 > ./results/log/lr/8.csv &
./backprop_nn.py log 15 0.025 0.00 100 1.00 > ./results/log/lr/9.csv &
./backprop_nn.py log 15 0.025 0.00 100 1.00 > ./results/log/lr/10.csv &

wait

./backprop_nn.py log 15 0.050 0.00 100 1.00 > ./results/log/lr/11.csv &
./backprop_nn.py log 15 0.050 0.00 100 1.00 > ./results/log/lr/12.csv &
./backprop_nn.py log 15 0.050 0.00 100 1.00 > ./results/log/lr/13.csv &
./backprop_nn.py log 15 0.050 0.00 100 1.00 > ./results/log/lr/14.csv &
./backprop_nn.py log 15 0.050 0.00 100 1.00 > ./results/log/lr/15.csv &

wait

./backprop_nn.py log 15 0.100 0.00 100 1.00 > ./results/log/lr/16.csv &
./backprop_nn.py log 15 0.100 0.00 100 1.00 > ./results/log/lr/17.csv &
./backprop_nn.py log 15 0.100 0.00 100 1.00 > ./results/log/lr/18.csv &
./backprop_nn.py log 15 0.100 0.00 100 1.00 > ./results/log/lr/19.csv &
./backprop_nn.py log 15 0.100 0.00 100 1.00 > ./results/log/lr/20.csv &

wait

./backprop_nn.py log 15 0.500 0.00 100 1.00 > ./results/log/lr/21.csv &
./backprop_nn.py log 15 0.500 0.00 100 1.00 > ./results/log/lr/22.csv &
./backprop_nn.py log 15 0.500 0.00 100 1.00 > ./results/log/lr/23.csv &
./backprop_nn.py log 15 0.500 0.00 100 1.00 > ./results/log/lr/24.csv &
./backprop_nn.py log 15 0.500 0.00 100 1.00 > ./results/log/lr/25.csv &

wait

./backprop_nn.py log 15 1.000 0.00 100 1.00 > ./results/log/lr/26.csv &
./backprop_nn.py log 15 1.000 0.00 100 1.00 > ./results/log/lr/27.csv &
./backprop_nn.py log 15 1.000 0.00 100 1.00 > ./results/log/lr/28.csv &
./backprop_nn.py log 15 1.000 0.00 100 1.00 > ./results/log/lr/29.csv &
./backprop_nn.py log 15 1.000 0.00 100 1.00 > ./results/log/lr/30.csv &

wait

echo "\nCollecting data to determine optimal LR (TANH).";

./backprop_nn.py tanh 30 0.010 0.00 100 1.00 > ./results/tanh/lr/1.csv &
./backprop_nn.py tanh 30 0.010 0.00 100 1.00 > ./results/tanh/lr/2.csv &
./backprop_nn.py tanh 30 0.010 0.00 100 1.00 > ./results/tanh/lr/3.csv &
./backprop_nn.py tanh 30 0.010 0.00 100 1.00 > ./results/tanh/lr/4.csv &
./backprop_nn.py tanh 30 0.010 0.00 100 1.00 > ./results/tanh/lr/5.csv &

wait

./backprop_nn.py tanh 30 0.025 0.00 100 1.00 > ./results/tanh/lr/6.csv &
./backprop_nn.py tanh 30 0.025 0.00 100 1.00 > ./results/tanh/lr/7.csv &
./backprop_nn.py tanh 30 0.025 0.00 100 1.00 > ./results/tanh/lr/8.csv &
./backprop_nn.py tanh 30 0.025 0.00 100 1.00 > ./results/tanh/lr/9.csv &
./backprop_nn.py tanh 30 0.025 0.00 100 1.00 > ./results/tanh/lr/10.csv &

wait

./backprop_nn.py tanh 30 0.050 0.00 100 1.00 > ./results/tanh/lr/11.csv &
./backprop_nn.py tanh 30 0.050 0.00 100 1.00 > ./results/tanh/lr/12.csv &
./backprop_nn.py tanh 30 0.050 0.00 100 1.00 > ./results/tanh/lr/13.csv &
./backprop_nn.py tanh 30 0.050 0.00 100 1.00 > ./results/tanh/lr/14.csv &
./backprop_nn.py tanh 30 0.050 0.00 100 1.00 > ./results/tanh/lr/15.csv &

wait

./backprop_nn.py tanh 30 0.100 0.00 100 1.00 > ./results/tanh/lr/16.csv &
./backprop_nn.py tanh 30 0.100 0.00 100 1.00 > ./results/tanh/lr/17.csv &
./backprop_nn.py tanh 30 0.100 0.00 100 1.00 > ./results/tanh/lr/18.csv &
./backprop_nn.py tanh 30 0.100 0.00 100 1.00 > ./results/tanh/lr/19.csv &
./backprop_nn.py tanh 30 0.100 0.00 100 1.00 > ./results/tanh/lr/20.csv &

wait

./backprop_nn.py tanh 30 0.500 0.00 100 1.00 > ./results/tanh/lr/21.csv &
./backprop_nn.py tanh 30 0.500 0.00 100 1.00 > ./results/tanh/lr/22.csv &
./backprop_nn.py tanh 30 0.500 0.00 100 1.00 > ./results/tanh/lr/23.csv &
./backprop_nn.py tanh 30 0.500 0.00 100 1.00 > ./results/tanh/lr/24.csv &
./backprop_nn.py tanh 30 0.500 0.00 100 1.00 > ./results/tanh/lr/25.csv &

wait

./backprop_nn.py tanh 30 1.000 0.00 100 1.00 > ./results/tanh/lr/26.csv &
./backprop_nn.py tanh 30 1.000 0.00 100 1.00 > ./results/tanh/lr/27.csv &
./backprop_nn.py tanh 30 1.000 0.00 100 1.00 > ./results/tanh/lr/28.csv &
./backprop_nn.py tanh 30 1.000 0.00 100 1.00 > ./results/tanh/lr/29.csv &
./backprop_nn.py tanh 30 1.000 0.00 100 1.00 > ./results/tanh/lr/30.csv &

wait

echo "\nAll processes done!\n";