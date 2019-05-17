#include <stdio.h>
#include <iostream>
#include <vector>
#include <fstream>
#include <sstream>
#include <string>
#include <cmath>
#include <stdlib.h>

using namespace std;

void readFile(char*);
void populate1D(vector<int>&, ifstream&);
void populate2D(vector<vector<int> >&, int, ifstream&);
string checkSafe();