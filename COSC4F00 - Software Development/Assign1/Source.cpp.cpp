#include "stdafx.h"
#include "circ_ll.h"

using namespace cLL;
using namespace std;

int main() {
	circ_ll testCLL;
	char c;
	int v, j;
	while (true) {
		cout << "\nSelect an option by keying the appropriate character." << endl;
		cout << "1. Create initial element\t2. Insert Next\t3.Insert Prev" << endl;
		cout << "4. Insert Start\t5. Insert End\t6.Delete Current" << endl;
		cout << "7. Display CLL\t8. Find Next Instance of Value" << endl;
		cout << "9. Inspect Current Element\tt. Test Harness" << endl;
		cout << "\n< = Prev, > = Next" << endl;
		cout << "\nOption: ";
		cin >> c;
		switch (c) {
		case '1':
			cout << "\nInput a value for initial element: ";
			cin >> v;
			testCLL.create(v);
			break;
		case '2':
			cout << "\nInput a value to insert next: ";
			cin >> v;
			testCLL.insert_next(v);
			break;
		case '3':
			cout << "\nInput a value to insert prev: ";
			cin >> v;
			testCLL.insert_prev(v);
			break;
		case '4':
			cout << "\nInput a value to insert at start: ";
			cin >> v;
			testCLL.insert_start(v);
			break;
		case '5':
			cout << "\nInput a value to insert at end: ";
			cin >> v;
			testCLL.insert_end(v);
			break;
		case '6':
			cout << "\nDeleting current element...";
			testCLL.delete_current();
			break;
		case '7':
			cout << "\n";
			testCLL.print();
			break;
		case '8':
			cout << "\nInput a value to search for and point current at: ";
			cin >> v;
			testCLL.goTo(v);
			break;
		case '9':
			testCLL.inspect();
			break;
		case ',': case '<':
			testCLL.prev();
			break;
		case '.': case '>':
			testCLL.next();
			break;
		case 't': case'T':
			cout << "\nTesting printing empty CLL..." << endl;
			testCLL.print();
			cout << "\nTesting moving clockwise on empty CLL..." << endl;
			testCLL.next();
			cout << "\nTesting moving anticlockwise on empty CLL..." << endl;
			testCLL.prev();
			cout << "\nTesting deletion on empty CLL..." << endl;
			testCLL.delete_current();
			cout << "\nInspecting element on empty CLL..." << endl;
			testCLL.inspect();
			cout << "\nPopulating CLL with 5 values..." << endl;
			testCLL.create(1);
			for (j = 2; j <= 5; j++) {
				testCLL.insert_end(j);
			}
			testCLL.print();
			cout << "\nMoving clockwise 4 positions..." << endl;
			for (j = 0; j < 4; j++) {
				testCLL.next();
			}
			testCLL.print();
			cout << "\nMoving anticlockwise 2 positions..." << endl;
			for (j = 0; j < 2; j++) {
				testCLL.prev();
			}
			testCLL.print();
			cout << "\nInspecting valid element..." << endl;
			testCLL.inspect();
			cout << "\nInserting '6' at start..." << endl;
			testCLL.insert_start(6);
			testCLL.print();
			cout << "\nInserting '7' at end..." << endl;
			testCLL.insert_end(7);
			testCLL.print();
			cout << "\nInserting '8' after current..." << endl;
			testCLL.insert_next(8);
			testCLL.print();
			cout << "\nInserting '9' before current..." << endl;
			testCLL.print();
			cout << "\nTesting deleting element..." << endl;
			testCLL.delete_current();
			testCLL.print();
			cout << "\nTesting deleting head element..." << endl;
			for (j = 0; j < 2; j++) {
				testCLL.prev();
			}
			testCLL.delete_current();
			testCLL.print();
			cout << "\nTesting deleting tail element..." << endl;
			testCLL.prev();
			testCLL.delete_current();
			testCLL.print();
			break;
		}
	}
	return 0;
}