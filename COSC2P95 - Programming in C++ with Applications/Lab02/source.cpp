#include <iostream>
#include <bitset>
#include <string>
using namespace std;
int main() {
	unsigned char c = 0x9F; // hex value
	string b = bitset<8>(c).to_string(); // convert to string
	cout << "Binary representation is: " << b << endl;
	string word = ""; // empty word container
	cout << "In English, and reversed, this is:" << endl;
	for(int i = 7; i >= 0; i--) { // for each letter
		// recall 48 === '0'
		word = (b.at(i) == 48) ? "zero" : "one";
		cout << word << endl; // print the word
	}
	return 0;
}
