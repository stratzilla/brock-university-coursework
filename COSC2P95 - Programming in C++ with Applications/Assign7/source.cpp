#include "TPQueue.h"
#include <stdio.h>
#include <string>
#include <limits.h>
#include <iostream>

/**
 * main driver
 */
int main(void) {
	unsigned int in = -1;
	TPQueue<std::string, 256> tpq;
	std::cout << tpq << std::endl;
	std::cout << "1: Add entry to priority queue." << std::endl;
	std::cout << "2: Remove entry from priority queue." << std::endl;
	std::cout << "3: Peek entry from priority queue." << std::endl;
	std::cout << "0: Quit" << std::endl << std::endl;
	while (in != 0) {
		std::cout << "Select an option: ";
		std::cin >> in;
		switch(in) {
			case 0: break;
			case 1: {
				std::string record = ""; int priority = 0;
				std::cout << "What is the record? "; std::cin >> record;
				std::cout << "What is the priority? "; std::cin >> priority;
				tpq.enqueue(record, priority);
				break;
			}
			case 2: {
				if (tpq.isEmpty()) {
					std::cout << "The queue is currently empty." << std::endl;
				} else {
					std::cout << tpq.dequeue() << " retrieved from queue." << std::endl;
				}
				break;
			}
			case 3: {
				if (tpq.isEmpty()) {
					std::cout << "The queue is currently empty." << std::endl;
				} else {
					std::cout << tpq.peek() << " peeked from queue." << std::endl;
				}
				break;
			}
			default: std::cout << "Invalid option." << std::endl; break;
		}
		if (in == 0) { break; }
		in = -1;
		std::cout << tpq << std::endl;
	}
	return 0;
}
