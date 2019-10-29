#include "CQueue.cpp"
#include <stdio.h>

using namespace std;

int main(void) {
	CQueue cq = CQueue();
	cq.printQueue();
	for (int i = 0; i < 5; i++) {
		cout << "Adding " << i << " to queue..." << endl;
		cq.enqueue(i);
		cq.printQueue();
	}
	int loopEnd = cq.getSize();
	for (int i = 0; i < loopEnd; i++) {
		cout << "Dequeuing from queue..." << endl;
		cq.dequeue();
		cq.printQueue();
	}
	return 0;
}
