#include <stdio.h>
#include <iostream>
#include <cmath>
#include <array>

// clamped values
int MIN_X = -4, MAX_X = 6;
int MIN_Y = -12, MAX_Y = 5;

// function declaration
double calculate(double, double, int);

using namespace std;

/**
 * function to determine user choices in how
 * the program will operate
 * @return - the choices the user has made
 */
array<int, 3> promptChoices() {
	array<int, 3> choices;
	cerr << "Select your function:\n";
	cerr << "1. sin(x)cos(y)\n";
	cerr << "2. sin(x)+cos^2(y/2)-x/y\n";
	cerr << "3. 1/2 sin(x) + 1/2 cos(y)\n";
	cerr << "4. 1/2 sin(x) + xcos(3y)\n";
	cerr << "0. Quit\n";
	cin >> choices[0]; // what type of function
	if (choices[0] == 0) { return choices; }
	cerr << "Number of graduations per axis: ";
	cin >> choices[1]; // number of graduations
	cerr << "(0) Bitmap or (1) Values? ";
	cin >> choices[2]; // bitmap or not
	return choices;
}

/**
 * function to populate the array and then display it
 * @param in - the user choices
 */
void populateArray(array<int, 3> in) {
	/**
	 * VLA is considered poor form but I don't know how
	 * else to do the assignment. In truth, arrays aren't
	 * even necessary to get the right output, but I'm
	 * limited by assignment guidelines. VLAs work here
	 * because the scope is limited to just this function
	 * I would have made it more declarative and have a
	 * separate function to print the array, but VLAs here
	 * means init here, display here unfortunately.
	 */
	double array[in[1]][in[1]];
	// find how many "steps" each iteration should take
	double gradX = (double)(MAX_X - MIN_X)/(double)in[1];
	double gradY = (double)(MAX_Y - MIN_Y)/(double)in[1];
	// for keeping track
	double currX = MIN_X; double currY = MIN_Y;
	for (int x = 0; x < in[1]; x++, currX += gradX) {
		for (int y = 0; y < in[1]; y++, currY += gradY) {
			array[y][x] = calculate(currX, currY, in[0]); // find value
		}
		currY = MIN_Y; // reset since the inner loop is done >n times
	}
	// then print the array
	for (int x = 0; x < in[1]; x++) {
		for (int y = 0; y < in[1]; y++) {
			if (in[2] == 1) {
				cout << array[y][x];
			} else if (in[2] == 0) {
				cout << ((array[y][x] > 0) ? 'X' : 'O');
			}
		}
		cout << "\n";
	}
	/** the array is flipped along both axes but the assigment sheet
	 * said this is fine.
	 */
	cout << "\n";
}

/**
 * function to calculate the value to be inserted into
 * the array for display.
 * @param x - the x-coord of the input
 * @param y - the y-coord of the input
 * @param type - the function to use
 * @return - the result of the function taking coords as params
 */
double calculate(double x, double y, int type) {
	switch (type) {
		case 1: return sin(x) * cos(y);
		case 2: return sin(x) + pow(cos(y/2),2) - (x/y);
		case 3: return ((1/2) * sin(x)) + ((1/2) * cos(y));
		case 4: return ((1/2) * sin(x)) + (x * cos(3*y));
		default: return 0.00;
	}
}

int main(void) {
	while(true) {
		array<int, 3> choices = promptChoices();
		if (choices[0] == 0) { break; }
		populateArray(choices);
	}
	return EXIT_SUCCESS;
}
