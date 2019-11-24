#include <iostream>
#include <sstream>
#include <stdlib.h>

using namespace std;

int fib(int i) {
	if (i < 2) { return i; }
	return fib(i-1) + fib(i-2);
}

void error() {
	cout << "The single argument must be integer in range 0..90." << endl;
}

int main(int argc, char** argv) {
	bool flag = false; // to determine if an error occurred later
	int i = -1; // initial value out of range 0..90
	if (argc == 2) { // if proper amount of arguments
		istringstream in(argv[1]); // for reading input
		if (!(in >> i)) { flag = true; } // if unable to populate stringstream with value, its not int
		if (i > 90 || i < 0) { flag = true; } // if out of range
	} else {
		flag = true;
	}
	if (flag == true) {
		error();
		return EXIT_FAILURE;
	} else {
		cout << fib(i) << endl;
	}
	return EXIT_SUCCESS;
}
