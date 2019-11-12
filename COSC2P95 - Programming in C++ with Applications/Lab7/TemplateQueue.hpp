#include <iostream>

template <typename T, int capacity>
class TemplateQueue {
	private:
		T arr[capacity];
		int size;
	public:
		TemplateQueue() { size = 0; }
		void enqueue(T in) {
			arr[getSize()] = in;
			size++;
		}
		T dequeue() {
			T temp = arr[0];
			for (int i = 0; i < getSize()-1; i++) {
				arr[i] = arr[i+1];
			}
			size--;
			return temp;
		}
		bool isEmpty() { return size == 0; }
		int getSize() { return size; }
		void printQueue() {
			std::cout << "Queue contains: " << std::endl;
			for (int i = 0; i < getSize(); i++) {
				std::cout << arr[i] << " --> ";
			}
			std::cout << "NULL (size = " << getSize() << ")" << std::endl;
		}
};
