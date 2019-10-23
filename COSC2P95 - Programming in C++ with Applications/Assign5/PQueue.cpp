/**
 * implementation of PQueue class
 */

#include "PQueue.h"
#include <iostream>
#include <string>
#include <limits.h>

// constructor
PQueue::PQueue() {
	size = 0;
	front = nullptr;
}

// destructor
PQueue::~PQueue() {
	PRecord *tempRecord;
	while (true) {
		if (front == nullptr) { break; }
		tempRecord = front;
		front = front->next;
		delete tempRecord;
	}
}

/**
 * method to reset queue to initial conditions
 */
void PQueue::resetQueue() {
	delete front;
	front = nullptr;
	size = 0;
}

/**
 * method to add element to queue
 * always adds in rear
 * @param r - the record to add
 * @param p - the priority of the record
 */
void PQueue::enqueue(std::string r, int p) {
	// the new record
	PRecord *newRecord = new PRecord;
	PRecord *tempRecord;
	newRecord->item = r;
	newRecord->priority = p;
	newRecord->next = nullptr;
	if (front == nullptr) { // if only element
		front = newRecord;
	} else { // if not only element
		tempRecord = front;
		while(true) { // find next open spot
			if (tempRecord->next == nullptr) { break; }
			tempRecord = tempRecord->next; // squeeze it in
		}
		tempRecord->next = newRecord;
	}
	size++;
}

/**
 * method to dequeue from priority queue
 * dequeue is different from queue as you remove
 * highest priority item before others, otherwise
 * removes from front
 * @return - the record being removed
 */
std::string PQueue::dequeue() {
	if (size == 0) { return "Nothing"; } // if empty already
	PRecord *tempRecord = front;
	int bestPriority = INT_MAX; // any priority will be lower valued (ie higher)
	std::string toReturn = ""; // for returning later
	while(tempRecord != nullptr) { // iterate over the queue
		if (tempRecord->priority < bestPriority) { // if better priority
			bestPriority = tempRecord->priority; // overwrite
			toReturn = tempRecord->item;
		}
		tempRecord = tempRecord->next;
	}
	/**
	 * we've found the best priority, now remove that record
	 * this is not the cleanest way, but if you were to iterate
	 * from front to back looking for a given priority,
	 * since records are inserted in rear, the older
	 * record in case of a tie is removed first
	 * otherwise it will seek out the same record as above and remove
	 */
	tempRecord = front;
	while(tempRecord != nullptr) {
		// this only really applies if first record is it
		if (tempRecord->priority == bestPriority) {
			if (tempRecord->next == nullptr) { // if only element
				front = nullptr; // erase
			} else { // otherwise, set new front
				front = tempRecord->next;
			}
			delete tempRecord; // free it up
			break;
		}
		// if not first element
		if (tempRecord->next->priority == bestPriority) {
			if (tempRecord->next->next == nullptr) {
				tempRecord->next = nullptr;
			} else {
				tempRecord->next = tempRecord->next->next;
			}
			// I'm not sure what to delete here :(
			break;
		}
		tempRecord = tempRecord->next;
	}
	size--;
	if (size == 0) { resetQueue(); } // reset
	return toReturn;
}

/**
 * method to determine if the queue is empty or not
 * @return - truth value on whether queue is empty
 */
bool PQueue::isEmpty() {
	// surely a queue of size 0 is empty
	return getSize() == 0;
}

/**
 * method to find the size of the queue
 * @return - the size of the queue
 */
int PQueue::getSize() {
	return size; // grab otherwise private attribute
}

/**
 * method to print the queue
 * shows each record and its corresponding priority
 * prints in order as inserted based on priority
 */
void PQueue::printQueue() {
	std::cout << std::endl << "Priority Queue contains (record, priority):" << std::endl;
	PRecord *tempRecord = front;
	while(true) {
		if (tempRecord == nullptr) { break; }
		std::cout << "{" << tempRecord->item << ", " << tempRecord->priority << "} --> ";
		tempRecord = tempRecord->next;
	}
	std::cout << "NULL" << " (size = " << getSize() << ")" << std::endl;
	std::cout << std::endl;
}
