/**********************************************
* method declarations for the circular linked list
* ADT. The CLL has a head, tail, and current nodes
* and when indexed, loops back around at each endpoint.
* 
* The CLL is first initialized as a new variable
* and then populated with any of the insertion methods.
* While create() is meant to be insertion of first element,
* any of the other insertion methods will fall back
* onto create() if the CLL is currently empty.
*
* I initially had method chaining to prompt the user
* to input values should they desire to do it manually rather
* than hardcoded, but there is a menu in main() which
* instead does this.
*
* There should be no uncaught errors except perhaps
* for inputting an invalid value. While it's been
* designed around integers, generics/templates could
* be used. I couldn't find a way to implement templates
* cleanly. Maybe it's my lack of experience in C++ but
* it looks like the template needs to be defined before
* every method which looks downright ugly.
***********************************************/

#include "stdafx.h"
#include <iostream>

namespace cLL {
	struct node {
		int val;
		struct node *prev;
		struct node *next;
	};
	class circ_ll {
	private:
		/*
		* Even though a circular linked list has no head or tail,
		* there needs to be something to know when to loop back
		* around. I could have used NULL and loop back depending
		* on direction you encounter a null pointer at, but I
		* opted for head/tail instead to make it look cleaner.
		*/
		node *head; //"front" of the list
		node *tail; //"back" of the list
		node *curr; //currently pointed to element
	public:
		//for movement clockwise/anticlockwise in the list
		void next(); //goes to next element
		void prev(); //goes to previous element
		//for adding values to the CLL
		void create(int val); //creates a CLL with initial element
		void insert_start(int val); //inserts element at start of list
		void insert_end(int val); //inserts element at end of list
		void insert_next(int val); //inserts element between current and next element
		void insert_prev(int val); //inserts element between current and previous
		//related to checking if empty or deleting
		void delete_current(); //removes current element
		bool isEmpty(); //checks if CLL is empty
		//for searching
		int inspect(); //inspects current element
		bool find(int val); //finds specific element
		void goTo(int val); //sets current to next element of specific value
		//miscellaneous
		int size(); //finds size of CLL
		void print(); //displays the CLL contents
		circ_ll() { //constructor
			head = NULL;
			tail = NULL;
			curr = NULL;
		}
		~circ_ll() {} //deconstructor
	};
}