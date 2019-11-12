/****************************************
* member methods for circ_ll class. While it's
* not an exhaustive list of possible methods
* for an ADT like this one, it's enough to
* be of some use at least, plus demonstrates
* some of the more important functions of 
* a list-like ADT.
*****************************************/

#include "stdafx.h"
#include "circ_ll.hpp"

using namespace std;

namespace cLL {
	/*
	* prints the CLL from head->tail as well as
	* currently pointed at element, size of the CLL,
	* which values are the head/tail
	*/
	void circ_ll::print() {
		if (isEmpty()) { //if list is empty...
			cout << "CLL is empty." << endl; //catch condition
		}
		else { //if list is not empty
			struct node *temp = new(struct node); //temp node
			temp = this->head; //printing from h->t, so start at h
			while (true) { //do until conditions stop it
				if (temp == this->curr) { //makes current element stand out
					cout << ">" << temp->val << "<";
				}
				else { //non-current element
					cout << temp->val;
				}
				if (temp->next == this->head) { //if it has done a "lap"
					break; //stop loop
				}
				temp = temp->next; //go to next node
				cout << " -> ";
			}
			cout << " -> ..." << endl;
			//display metrics based on CLL
			cout << "\nCurr: " << this->curr->val << ", Head: " << this->head->val << ", Tail: " << this->tail->val << ", Size: " << size() << endl;
		}
	}

	/* 
	* finds the size of the CLL for display with print()
	* or for use with other member functions
	*
	* returns how many elements are within the CLL
	*/
	int circ_ll::size() {
		if (this->head == NULL) { //if empty CLL
			return 0; //doesn't matter as other method will cout empty
		}
		struct node *temp = new(struct node); //temp
		temp = this->head;
		int i = 1;
		while (temp->next != this->head) { //iterate over all CLL elements
			i++;
			temp = temp->next;
		}
		return i; //return how many elements
	}

	/*
	* init initial element in the CLL; some other
	* methods will fall back onto this one if an
	* insertion is attempted without an initial element
	*/
	void circ_ll::create(int val) {
		if (!isEmpty()) { //if the CLL has an element already
			cout << "The circular linked list has already been made." << endl;
		}
		else {
			struct node *newNode = new(struct node); //new node
			newNode->val = val; 
			newNode->prev = newNode;
			newNode->next = newNode;
			this->head = newNode; //set head to new node
			this->tail = this->head; //as well as tail
			this->curr = this->head; //current can exist now, point to head
		}
	}

	/* checks if the CLL is empty
	*
	* returns T/F
	*/
	bool circ_ll::isEmpty() {
		if (this->size() <= 0) {
			return true;
		}
		return false;
	}

	/* 
	* inserts element at the start of the CLL
	*/
	void circ_ll::insert_start(int val) {
		if (isEmpty()) { //if empty
			create(val); //fall back to create()
		}
		else {
			struct node *newNode = new(struct node);
			newNode->val = val;
			newNode->next = this->head; //make way for new head
			this->head->prev = newNode; //move new node before head
			this->head = newNode; //new node is new head
			this->head->prev = this->tail; //repoint tail to new head's prev
			this->tail->next = this->head; //repoint head to new tail's next
		}
	}

	/*
	* inserts element at end of CLL
	*/
	void circ_ll::insert_end(int val) {
		if (isEmpty()) {
			create(val);
		}
		else {
			struct node *newNode = new(struct node);
			newNode->val = val;
			newNode->prev = this->tail; //make way for new tail
			this->tail->next = newNode; //old tail's next is new tail
			this->tail = newNode; //new tail is newnode
			this->head->prev = this->tail; //repointing
			this->tail->next = this->head;
		}
	}

	/*
	* inserts new element after currently pointed to element
	*/
	void circ_ll::insert_next(int val) {
		if (isEmpty()) {
			create(val);
		}
		else {
			struct node *newNode = new(struct node);
			newNode->val = val;
			newNode->next = this->curr->next; //new node's next is curr's next
			newNode->prev = this->curr; //new node's prev is curr node
			this->curr->next = newNode; //curr's next is new node
			if (this->curr == this->tail) { //if tail, refactor
				this->tail = this->curr->next;
			}
		}
	}

	/*
	* inserts new element before currently pointed to element
	*/
	void circ_ll::insert_prev(int val) {
		if (isEmpty()) {
			create(val);
		}
		/* 
		* might not be the most elegant way
		*/
		else {
			prev(); //go back
			insert_next(val); //new node after previous
			next(); //repoint to old curr
			next();
		}
	}

	/*
	* deletes current element and refactors list
	*/
	void circ_ll::delete_current() {
		if (isEmpty()) {
			cout << "The CLL is currently empty and thus cannot have an elemented delete." << endl;
		} 
		else {
			struct node *tempNode = new(struct node);
			struct node *t = new(struct node);
			tempNode = this->curr;
			/*
			* make a gap between curr->prev and curr->next
			* so when curr is removed, can "reconnect"
			* the two adjacent nodes
			*/
			t = this->curr->next;
			t->prev = this->curr->prev;
			this->curr = t->prev; //make curr => curr's prev
			this->curr->next = t; //make curr's next = (old) curr's next
			if (this->head == tempNode) { //if head
				this->head = t; //reinit head
				/*
				* since head was removed, need to point current
				* to the new head
				*/
				this->curr = this->head;
				this->tail->next = this->head; //reinit tail->next
			}
			else if (this->tail == tempNode) { //if tail
				this->tail = t->prev; //reinit tail
				this->curr = this->tail;
				this->head->prev = this->tail;
			}
			free(tempNode); //adios
		}
	}

	/*
	* goes "clockwise", points to next element
	*/
	void circ_ll::next() {
		if (isEmpty()) {
			cout << "The CLL is currently empty." << endl;
		}
		else {
			this->curr = this->curr->next;
		}
	}

	/*
	* goes "anticlockwise", points to previous element
	*/
	void circ_ll::prev() {
		if (isEmpty()) {
			cout << "The CLL is currently empty." << endl;
		}
		else {
			this->curr = this->curr->prev;
		}
	}

	/*
	* finds out if an element exists in the CLL
	*
	* returns T if found
	*/
	bool circ_ll::find(int val) {
		struct node *tempNode = new(struct node);
		tempNode = this->curr;
		while (true) {
			if (tempNode->val == val) {
				return true; //found it!
			}
			tempNode = tempNode->next;
			if (tempNode == this->curr) {
				break; //if did a complete lap
			}
		}
		return false;
	}

	/*
	* points current to next element with value equal
	* to user inputted integer
	*/
	void circ_ll::goTo(int val) {
		if (!find(val)) { //if val is not in the CLL
			cout << "\nUnable to find element." << endl;
		}
		else {
			while (true) {
				if (this->curr->val == val) {
					break; //found it!
				}
				next(); //go to next
			}
		}
	}

	/*
	* inspects current element returning its value
	* says the value and type of value
	*
	* returns val
	*/
	int circ_ll::inspect() {
		if (isEmpty()) {
			cout << "The CLL is currently empty." << endl;
			return NULL;
		}
		cout << "\nThe current element is " << this->curr->val << " of type " << typeid(this->curr->val).name() << "." << endl;
		return this->curr->val;
	}
}