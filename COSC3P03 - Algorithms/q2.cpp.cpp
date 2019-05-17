#include <iostream>
#include <stdio.h>
#include <limits.h>
#include <vector>
#include <string>

using namespace std;

int findLCS(string, string);

// main driver
int main(int argc, char *argv[]) {
    if (argc == 3) {
        string a = argv[1];
        string b = argv[2];
        cout << "The longest common substring between " << a << " and " << b << " is: ";
        cout << findLCS(a,b) << endl;
    } else {
        cout << "Too few or too many arguments found.\n";
        cout << "To execute program, type \"./execute1 <stringA> <stringB>\"\n";
        cout << "Where <stringA> and <stringB> to find LCS.\"\n";
    }
    return 1;
}

int findLCS(string a, string b) {
    int m = a.size(), n = b.size();
    int suffix[m+1][n+1];
    int maxLength = 0;
    for (int i = 0; i <= m; i++) {
        for (int j = 0; j <= n; j++) {
            if (i == 0 || j == 0) {
                suffix[i][j] = 0;
            } else if (a[i-1] == b[j-1]) {
                suffix[i][j] = suffix[i-1][j-1] + 1;
            } else {
                suffix[i][j] = 0;
            }
        }
    }
    for (int i = 0; i <= m; i++) {
        for (int j = 0; j <= n; j++) {
            maxLength = max(maxLength, suffix[i][j]);
        }
    }
    return maxLength;
}