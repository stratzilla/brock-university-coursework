#include <stdio.h>
#include <fstream>
#include <iostream>
#include <string>

using namespace std;

int main(int argc, char** argv) {
	string fn = "";
	char c;
	if (argc == 2) { fn = argv[1]; }
	ifstream is(fn.c_str(), ios::binary);
	ofstream os("temp.txt", ios::binary);
	while (is.get(c)) {
		switch(c) {
			case 0x0d: break;
			case 0x0a: os.put((char)0x0d); os.put((char)0x0a); break;
			default: os.put(c); break;
		}
	}
	is.close(); os.close();
	string command = "mv temp.txt " + fn;
	system(command.c_str());
	return EXIT_SUCCESS;
}
