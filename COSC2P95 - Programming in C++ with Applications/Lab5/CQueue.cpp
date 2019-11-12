#include "CQueue.hpp"
#include <iostream>

using namespace std;

CQueue::CQueue() {
	size = 0;
	front = back = NULL;
}

CQueue::~CQueue() {
	Node *tempNode;
	while (true) {
		if (tempNode == NULL) { break; }
		tempNode = front;
		front = front->next;
		delete tempNode;
	}
}

void CQueue::enqueue(int i) {
	Node *newNode = new Node;
	newNode->item = i;
	newNode->next = NULL;
	size++;
	if (front == NULL) {
		front = newNode;
	} else {
		back->next = newNode;
	}
	back = newNode;
}

void CQueue::dequeue() {
	if (front != NULL) {
		size--;
		Node *oldNode = front;
		front = front->next;
		delete oldNode;
		if (size == 0) {
			back = NULL;
		}
	}
}

bool CQueue::isEmpty() {
	return size == 0;
}

int CQueue::getSize() {
	return size;
}

void CQueue::printQueue() {
	cout << "Queue contains: ";
	Node *tempNode = front;
	while(true) {
		if (tempNode == NULL) { break; }
		cout << tempNode->item << " --> ";
		tempNode = tempNode->next;
	}
	cout << "NULL" << " (size = " << getSize() << ")" << endl;
}
