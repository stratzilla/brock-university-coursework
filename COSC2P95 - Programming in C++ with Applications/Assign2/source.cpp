#include <iostream>
#include <string>

using namespace std;

void studentNumber() {
	string num = "4582342"; // input
	string toPrint = ""; // for output
	for (char c : num) {
		// subtract ASCII value of c from ASCII value of 0, converting char to int
		int n = (c - '0');
		if (n % 2 == 0) {
			n /= 2; // divide by two
		} else {
			n *= 2; // multiply by two
			n -= ((int)n/10)*10; // subtract the "tens"
		}
		// reverse the conversion
		toPrint += (n + '0');
	}
	cout << toPrint << endl; // print the new number
}

void monkeys() {
	int c;
	cout << "How many monkeys do I have? ";
	cin >> c; // num monkeys
	// what about ternary operators? I could use switch statements but it's ugly
	bool mil = (c > 1000000) ? true : false;
	cout << "I have " << ((mil) ? to_string((double)c/1000000) : to_string(c)) << ((mil) ? " million " : " ") << "monkey" << ((c != 1) ? "s" : "") << endl;
}

void wrongCode() {
	/**
	 * what was wrong with the code? Dangling else? I get the expected output
	 * but all I changed was add parens where appropriate
	 */
	int choice1, choice2;
	cout << "Do you have room for tiramisu? (1: yes, 0: no)"; cin >> choice1;
	cout << "Do you actually like tiramisu? (1: yes, 0: no)"; cin >> choice2;
	if (choice1) {
		if (choice2) {
			cout << "Ready, willing, and able to enjoy tiramisu!\n";
		}
	} else {
		cout << "It doesn't matter if I like it or not, I don't have room for dessert!\n";
	}
}

int main(void) {
	studentNumber();
	monkeys();
	wrongCode();
	return 0;
}
