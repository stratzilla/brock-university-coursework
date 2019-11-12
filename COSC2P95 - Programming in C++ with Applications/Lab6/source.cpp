#include "Record.hpp"
#include <stdio.h>

using namespace std;

int main(void) {
	Record a = Record((char*)"hello");
	Record b = Record((char*)"world");
	Record c = Record((char*)"2p95");
	Record d = Record((char*)"c++");
	Record e = Record((char*)"brock university");
	cout << a << ", " << b << ", " << c << ", " << d << ", " << e << endl;
	return 0;
}
