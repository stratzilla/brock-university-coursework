#include <stdio.h>
#include <cmath>
#include <vector>
#include <iostream>
#include <iomanip>
#include <sstream>
#include <array>
#include <fstream>

// clamped values
double MIN_X = -4, MAX_X = 6;
double MIN_Y = -12, MAX_Y = 5;
double MIN_Z = 0, MAX_Z = 6.28318530718; // coincidence this is truncated 2*PI?

// function declaration
double calculate(double, double, double);
int convert(double);

using namespace std;

/**
 * function to determine user choice in graduations and frames
 * @return - the choice the user has made
 */
array<int, 2> promptChoices() {
	array<int, 2> choices;
	cerr << "Number of graduations per axis: ";
	cin >> choices[0]; // number of graduations
	cerr << "Number of frames: ";
	cin >> choices[1];
	return choices;
}

/**
 * function to save a PGM file
 * @param arr - the array to save
 * @param c - the frame
 */
void makePGM(vector<vector<double> > arr, int c) {
	stringstream filename;
	filename << "anim" << setw(4) << setfill('0') << c << ".pgm";
	ofstream output(filename.str().c_str());
	output << "P2" << "\n"; // type
	output << arr.size() << " " << arr.size() << "\n"; // dimensions
	output << "255" << "\n"; // depth (0..255)
	for (int x = 0; x < arr.size(); x++) {
		for (int y = 0; y < arr.size(); y++) {
			output << setw(3) << setfill('0') << arr[y][x] << " ";
		}
		output << "\n";
	}
	output.close();
}

/**
 * function to populate the array and then display it
 * @param in - the user choices for grads and frames
 */
void populateArray(array<int, 2> in) {
	vector<vector<double> > array(in[0], vector<double>(in[0]));
	// find how many "steps" each iteration should take
	double gradX = (double)(MAX_X - MIN_X)/(double)in[0];
	double gradY = (double)(MAX_Y - MIN_Y)/(double)in[0];
	// for frames
	double gradZ = (double)(MAX_Z - MIN_Z)/(double)in[1];
	// for keeping track
	double currX = MIN_X, currY = MIN_Y, currZ = MIN_Z;
	int count = 0;
	for (int z = 0; z < in[1]; z++, currZ += gradZ, currX = MIN_X, count++) {
		for (int x = 0; x < in[0]; x++, currX += gradX, currY = MIN_Y) {
			for (int y = 0; y < in[0]; y++, currY += gradY) {
				array[y][x] = convert(calculate(currX, currY, currZ)); // find value then convert
			}
		}
		makePGM(array, count); // save a pgm
	}
}

/**
 * function to scale calculated value to 0..255
 * @param in - the value to convert
 * @return - the converted value
 */
int convert(double in) {
	// found this online; scales from range -1..1 to 0..255
	return (255 - 0)*((in - (-1))/(1 - (-1)))+0;
	// returning as int will round
}

/**
 * function to calculate the value to be inserted into
 * the array for display.
 * @param x - the x-coord of the input
 * @param y - the y-coord of the input
 * @param z - the z-coord of the input
 * @return - the result of the function taking coords as params
 */
double calculate(double x, double y, double z) {
	return (cos(z) * ((0.5)*sin(x) + (0.5)*cos(y)));
}

int main(void) {
	populateArray(promptChoices());
	return EXIT_SUCCESS;
}
