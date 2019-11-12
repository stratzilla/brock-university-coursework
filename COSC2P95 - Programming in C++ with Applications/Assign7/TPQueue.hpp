#include <limits.h>
#include <iostream>
#include <sstream>

/**
 * TPQueue class implementation
 */

template <typename T, int capacity>
class TPQueue {
	private:
		// record in the form of a struct
		struct TPRecord {
			T item; // what is being stored
			long priority; // its priority
		};
		// array based implementation
		TPRecord arr[capacity];
		int size; // how big the queue is (max size)
		
		int count() { return size; } // getting for size attribute
		void setCount(int i) { size = i; } // setter for the same
		
		/**
		 * method to find the index of the next dequeue'd element
		 * @return - the array index
		 */
		int getMin() {
			long m = LONG_MAX; // initially min is max_value
			for (int i = 0; i < count(); i++) {
				// if less than current min, make it new min
				if ((arr[i]).priority < m) { m = (arr[i]).priority; }
			}
			for (int i = 0; i < count(); i++) {
				// then return the first instance of that priority (FIFO)
				if ((arr[i]).priority == m) { return i; }
			}
			return 0; // should never get here
		}
	public:
		TPQueue() { setCount(0); } // constructor
		
		/**
		 * method to peek next element to be dequeue'd
		 * @return - the peeked element
		 */
		T peek() {
			// it is just the element corresponding to min index
			return (arr[getMin()]).item;
		}
		
		/**
		 * method to dequeue from queue
		 * @return - the next dequeue'd element
		 */
		T dequeue() {
			T p = peek(); // same as peeked
			for (int i = getMin(); i < count(); i++) {
				// shift everything up
				arr[i] = arr[i+1];
			}
			setCount(count()-1); // decrement count
			return p;
		}
		
		/**
		 * method to enqeueu new element into queue
		 * @param in - the item to insert
		 * @param p - the priority of that item
		 */
		void enqueue(T in, long p) {
			TPRecord tr; // make new record and set attributes
			tr.item = in; tr.priority = p;
			arr[count()] = tr; // add it to end of queue
			setCount(count()+1); // increment size
		}
		
		/**
		 * method to determine if queue is empty or not
		 * @return - whether queue is empty
		 */
		bool isEmpty() { return count() == 0; }
		
		// for outputting to cout
		friend std::ostream& operator<<(std::ostream &out, TPQueue &p) {
			out << "\nQueue contains: ";
			for (int i = 0; i < p.count(); i++) {
				out << "{" << p.arr[i].item << ", " << p.arr[i].priority << "} --> ";
			}
			out << "NULL\n";
		}
};