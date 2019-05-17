#include <iostream>
#include <stdio.h>
#include <limits.h>
#include <vector>
#include <string>
#include <sstream>

using namespace std;

void optimalMult(int, vector<int>);
void findExpression(int, int, int*, int);

// main driver
int main(int argc, char *argv[]) {
    if (argc == 2) {
	istringstream iss(argv[1]);
	int n;
        iss >> n;
        vector<int> matrixR(n+1, 0);
        for (int i = 0; i <= n; i++) {
            cout << "What is r" << i << "? ";
            cin >> matrixR[i];
        }
        optimalMult(n, matrixR);
    } else {
        cout << "Too few or too many arguments found.\n";
        cout << "To execute program, type \"./execute1 <number>\"\n";
        cout << "Where <number> is the number of matrices (n).\"\n";
    }
    return 1;
}

void optimalMult(int n, vector<int> r) {
    int m[n+1][n+1];
    string expM[n+1][n+1];
    for (int i = 1; i<= n; i++) {
        m[i][i] = 0;
    }
    for (int w = 2; w <= n; w++) {
        for (int i = 1; i <= (n-w+1); i++) {
            int j = (i+w-1);
            m[i][j] = m[i+1][j] + r[i-1]*r[i]*r[j];
            for (int k = (i+1); k < j; k++) {
                int t = m[i][k] + m[k+1][j] + r[i-1]*r[k]*r[j];
                if (t < m[i][j]) {
                    m[i][j] = t;
                }
            }
        }
    }
    cout << "Optimal number of operations: " << m[1][n] << endl;
    cout << "Expression: ";
    cout << "\n";
}
