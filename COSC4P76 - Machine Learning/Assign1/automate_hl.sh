#!/bin/sh

echo "\nCollecting data to determine size of hidden layer (LOG).";

./backprop_nn.py log 5 0.01 0.00 100 1.00 > ./results/log/hl/1.csv &
./backprop_nn.py log 5 0.05 0.00 100 1.00 > ./results/log/hl/2.csv &
./backprop_nn.py log 5 0.10 0.00 100 1.00 > ./results/log/hl/3.csv &
./backprop_nn.py log 5 0.50 0.00 100 1.00 > ./results/log/hl/4.csv &
./backprop_nn.py log 5 1.00 0.00 100 1.00 > ./results/log/hl/5.csv &

wait

./backprop_nn.py log 10 0.01 0.00 100 1.00 > ./results/log/hl/6.csv &
./backprop_nn.py log 10 0.05 0.00 100 1.00 > ./results/log/hl/7.csv &
./backprop_nn.py log 10 0.10 0.00 100 1.00 > ./results/log/hl/8.csv &
./backprop_nn.py log 10 0.50 0.00 100 1.00 > ./results/log/hl/9.csv &
./backprop_nn.py log 10 1.00 0.00 100 1.00 > ./results/log/hl/10.csv &

wait

./backprop_nn.py log 15 0.01 0.00 100 1.00 > ./results/log/hl/11.csv &
./backprop_nn.py log 15 0.05 0.00 100 1.00 > ./results/log/hl/12.csv &
./backprop_nn.py log 15 0.10 0.00 100 1.00 > ./results/log/hl/13.csv &
./backprop_nn.py log 15 0.50 0.00 100 1.00 > ./results/log/hl/14.csv &
./backprop_nn.py log 15 1.00 0.00 100 1.00 > ./results/log/hl/15.csv &

wait

./backprop_nn.py log 20 0.01 0.00 100 1.00 > ./results/log/hl/16.csv &
./backprop_nn.py log 20 0.05 0.00 100 1.00 > ./results/log/hl/17.csv &
./backprop_nn.py log 20 0.10 0.00 100 1.00 > ./results/log/hl/18.csv &
./backprop_nn.py log 20 0.50 0.00 100 1.00 > ./results/log/hl/19.csv &
./backprop_nn.py log 20 1.00 0.00 100 1.00 > ./results/log/hl/20.csv &

wait

./backprop_nn.py log 25 0.01 0.00 100 1.00 > ./results/log/hl/21.csv &
./backprop_nn.py log 25 0.05 0.00 100 1.00 > ./results/log/hl/22.csv &
./backprop_nn.py log 25 0.10 0.00 100 1.00 > ./results/log/hl/23.csv &
./backprop_nn.py log 25 0.50 0.00 100 1.00 > ./results/log/hl/24.csv &
./backprop_nn.py log 25 1.00 0.00 100 1.00 > ./results/log/hl/25.csv &

wait

./backprop_nn.py log 30 0.01 0.00 100 1.00 > ./results/log/hl/26.csv &
./backprop_nn.py log 30 0.05 0.00 100 1.00 > ./results/log/hl/27.csv &
./backprop_nn.py log 30 0.10 0.00 100 1.00 > ./results/log/hl/28.csv &
./backprop_nn.py log 30 0.50 0.00 100 1.00 > ./results/log/hl/29.csv &
./backprop_nn.py log 30 1.00 0.00 100 1.00 > ./results/log/hl/30.csv &

wait

./backprop_nn.py log 35 0.01 0.00 100 1.00 > ./results/log/hl/31.csv &
./backprop_nn.py log 35 0.05 0.00 100 1.00 > ./results/log/hl/32.csv &
./backprop_nn.py log 35 0.10 0.00 100 1.00 > ./results/log/hl/33.csv &
./backprop_nn.py log 35 0.50 0.00 100 1.00 > ./results/log/hl/34.csv &
./backprop_nn.py log 35 1.00 0.00 100 1.00 > ./results/log/hl/35.csv &

wait

echo "\nCollecting data to determine size of hidden layer (TANH).";

./backprop_nn.py tanh 5 0.01 0.00 100 1.00 > ./results/tan/hl/1.csv &
./backprop_nn.py tanh 5 0.05 0.00 100 1.00 > ./results/tan/hl/2.csv &
./backprop_nn.py tanh 5 0.10 0.00 100 1.00 > ./results/tan/hl/3.csv &
./backprop_nn.py tanh 5 0.50 0.00 100 1.00 > ./results/tan/hl/4.csv &
./backprop_nn.py tanh 5 1.00 0.00 100 1.00 > ./results/tan/hl/5.csv &

wait

./backprop_nn.py tanh 10 0.01 0.00 100 1.00 > ./results/tan/hl/6.csv &
./backprop_nn.py tanh 10 0.05 0.00 100 1.00 > ./results/tan/hl/7.csv &
./backprop_nn.py tanh 10 0.10 0.00 100 1.00 > ./results/tan/hl/8.csv &
./backprop_nn.py tanh 10 0.50 0.00 100 1.00 > ./results/tan/hl/9.csv &
./backprop_nn.py tanh 10 1.00 0.00 100 1.00 > ./results/tan/hl/10.csv &

wait

./backprop_nn.py tanh 15 0.01 0.00 100 1.00 > ./results/tan/hl/11.csv &
./backprop_nn.py tanh 15 0.05 0.00 100 1.00 > ./results/tan/hl/12.csv &
./backprop_nn.py tanh 15 0.10 0.00 100 1.00 > ./results/tan/hl/13.csv &
./backprop_nn.py tanh 15 0.50 0.00 100 1.00 > ./results/tan/hl/14.csv &
./backprop_nn.py tanh 15 1.00 0.00 100 1.00 > ./results/tan/hl/15.csv &

wait

./backprop_nn.py tanh 20 0.01 0.00 100 1.00 > ./results/tan/hl/16.csv &
./backprop_nn.py tanh 20 0.05 0.00 100 1.00 > ./results/tan/hl/17.csv &
./backprop_nn.py tanh 20 0.10 0.00 100 1.00 > ./results/tan/hl/18.csv &
./backprop_nn.py tanh 20 0.50 0.00 100 1.00 > ./results/tan/hl/19.csv &
./backprop_nn.py tanh 20 1.00 0.00 100 1.00 > ./results/tan/hl/20.csv &

wait

./backprop_nn.py tanh 25 0.01 0.00 100 1.00 > ./results/tan/hl/21.csv &
./backprop_nn.py tanh 25 0.05 0.00 100 1.00 > ./results/tan/hl/22.csv &
./backprop_nn.py tanh 25 0.10 0.00 100 1.00 > ./results/tan/hl/23.csv &
./backprop_nn.py tanh 25 0.50 0.00 100 1.00 > ./results/tan/hl/24.csv &
./backprop_nn.py tanh 25 1.00 0.00 100 1.00 > ./results/tan/hl/25.csv &

wait

./backprop_nn.py tanh 30 0.01 0.00 100 1.00 > ./results/tan/hl/26.csv &
./backprop_nn.py tanh 30 0.05 0.00 100 1.00 > ./results/tan/hl/27.csv &
./backprop_nn.py tanh 30 0.10 0.00 100 1.00 > ./results/tan/hl/28.csv &
./backprop_nn.py tanh 30 0.50 0.00 100 1.00 > ./results/tan/hl/29.csv &
./backprop_nn.py tanh 30 1.00 0.00 100 1.00 > ./results/tan/hl/30.csv &

wait

./backprop_nn.py tanh 35 0.01 0.00 100 1.00 > ./results/tan/hl/31.csv &
./backprop_nn.py tanh 35 0.05 0.00 100 1.00 > ./results/tan/hl/32.csv &
./backprop_nn.py tanh 35 0.10 0.00 100 1.00 > ./results/tan/hl/33.csv &
./backprop_nn.py tanh 35 0.50 0.00 100 1.00 > ./results/tan/hl/34.csv &
./backprop_nn.py tanh 35 1.00 0.00 100 1.00 > ./results/tan/hl/35.csv &

wait

echo "\nAll processes done!\n";