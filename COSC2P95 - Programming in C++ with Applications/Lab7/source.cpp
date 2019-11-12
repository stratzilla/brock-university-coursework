#include "TemplateQueue.hpp"
#include <stdio.h>
#include <array>

int main(void) {
	std::cout << "Integer TemplateQueue: " << std::endl << std::endl;
	TemplateQueue<int, 5> intQueue;
	for (int i = 0; i < 5; i++) {
		intQueue.enqueue(i);
		intQueue.printQueue();
	}
	unsigned int loopEnd = intQueue.getSize();
	for (int i = 0; i < loopEnd; i++) {
		intQueue.dequeue();
		intQueue.printQueue();
	}
	std::cout << "\nString TemplateQueue: " << std::endl << std::endl;
	std::array<std::string, 3> strArr = {"hello","world","2p95"};
	TemplateQueue<std::string, 3> stringQueue;
	for (int i = 0; i < 3; i++) {
		stringQueue.enqueue(strArr[i]);
		stringQueue.printQueue();
	}
	loopEnd = stringQueue.getSize();
	for (int i = 0; i < loopEnd; i++) {
		stringQueue.dequeue();
		stringQueue.printQueue();
	}
	return EXIT_SUCCESS;
}
