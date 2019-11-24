#include "PQueue.hpp"
#include <stdio.h>
#include <string>
#include <limits.h>
#include <iostream>

/**
 * function to add element to queue
 * @param p - the queue to add to
 */
void addElement(PQueue &p) {
	std::string record = "";
	int priority = 0;
	std::cout << "What is the record? "; std::cin >> record;
	std::cout << "What is the priority? "; std::cin >> priority;
	p.enqueue(record, priority);
}

/**
 * function to delete from queue
 * @param p - the queue to remove from
 */
void remElement(PQueue &p) {
	std::cout << p.dequeue() << " retrieved from queue." << std::endl;
}

/**
 * main driver
 */
int main(void) {
	unsigned int in = -1;
	PQueue pq = PQueue();
	pq.printQueue();
	std::cout << "1: Add entry to priority queue." << std::endl;
	std::cout << "2: Remove entry from priority queue." << std::endl;
	std::cout << "0: Quit" << std::endl << std::endl;
	while (in != 0) {
		std::cout << "Selection an option: ";
		std::cin >> in;
		if (!std::cin.good()) { // to catch char input
			std::cin.clear();
			std::cin.ignore(INT_MAX,'\n');
			std::cout << "Invalid option." << std::endl;
		}
		switch(in) {
			case 0: break;
			case 1: addElement(pq); break;
			case 2: remElement(pq); break;
			default: std::cout << "Invalid option." << std::endl; break;
		}
		if (in == 0) { break; }
		in = -1;
		pq.printQueue();
	}
	return 0;
}
