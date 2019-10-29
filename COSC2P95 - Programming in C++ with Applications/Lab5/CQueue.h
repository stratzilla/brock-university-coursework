#pragma once

class CQueue {
	private:
		struct Node { int item; struct Node *next; };
		Node *front;
		Node *back;
		int size;
	public:
		CQueue();
		~CQueue();
		void enqueue(int);
		void dequeue();
		bool isEmpty();
		int getSize();
		void printQueue();
};
