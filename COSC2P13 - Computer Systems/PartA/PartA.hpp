#include <stdio.h>
#include <iostream>
#include <vector>
#include <fstream>
#include <sstream>
#include <string>
#include <cmath>
#include <stdlib.h>

using namespace std;

vector<int> readFile(char*);
string fcfs(vector<int>&);
string sstf(vector<int>&);
bool containsPos(vector<int>&);
int sumVector(vector<int>&);