#pragma once

/**
 * definition of PQueue class
 */

#include <string>

class PQueue {
	private: // private attribute inaccessible from other files
		struct PRecord {
			std::string item;
			int priority;
			struct PRecord *next;
		};
		PRecord *front;
		PRecord *back;
		int size;
		void resetQueue(); // member method
	public:
		// constructor and destructor
		PQueue();
		~PQueue();
		// member methods
		void enqueue(std::string, int);
		std::string dequeue();
		bool isEmpty();
		int getSize(); // getter
		void printQueue();
};
