#include <iostream>
#include <stdio.h>
#include <limits.h>
#include <vector>
#include <sstream>

using namespace std;

// price array (12 prices for 12 stops)
double prices[] =   {0.00, 1.25, 3.01,
                     5.75, 6.15, 4.25,
                     7.24, 10.85, 9.21,
                     3.25, 6.59, 4.99};
int m = (sizeof(prices)/sizeof(prices[0])); // length of road
double travel(int);
void printPrices();

// main driver
int main(int argc, char *argv[]) {
    if (argc == 2) {
	istringstream iss(argv[1]);
        int input;
	iss >> input;
        printPrices();
        cout << "Traveling " << input << " miles..." << endl;
        cout << travel(input) << endl;
    } else {
        cout << "Too few or too many arguments found.\n";
        cout << "To execute program, type \"./execute1 <number>\"\n";
        cout << "Where <number> is an integer value.\"\n";
    }
    return 1;
}

void printPrices() {
    cout << "Miles:\t";
    for (int i = 0; i < m; i++) {
        cout << i << "\t";
    }
    cout << "\nPrices:\t";
    for (int i = 0; i < m; i++) {
        cout << prices[i] << "\t";
    }
    cout << "\n";
}

double travel(int n) {
    vector<double> lengths(n+1, INT_MAX);
    for (int i = 0; i <= n; i++) {
        if (i <= m) {
            if (i == 0 || i == 1) {
                lengths[i] = prices[i];
            } else {
                for (int j = 1; j < i; j++) {
                    lengths[i] = min(prices[i], lengths[j]+prices[i-j]);
                }
            }
        } else {
            for (int j = i-m; j < i; j++) {
                int minimum = INT_MAX;
                if (minimum > lengths[j]+prices[i-j]) {
                    minimum = lengths[j]+prices[i-j];
                }
                lengths[i] = minimum;
            }
        }
    }
    return lengths[n];
}
