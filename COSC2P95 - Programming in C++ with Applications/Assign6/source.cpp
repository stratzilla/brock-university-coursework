#include <stdlib.h>
#include <stdio.h>
#include <time.h>
#include "Set.h"

/**
 * test harness for Set class
 */

int main(void) {
	srand(time(NULL)); int r;
	std::cout << "Initializing Sets A and B:" << std::endl;
	Set A, B;
	std::cout << "A: " << A << ", B: " << B << std::endl << std::endl;
	std::cout << "Insert some values for each set (in the form of \"{x,y,z}\"):" << std::endl;
	std::cout << "A: "; std::cin >> A;
	std::cout << "B: "; std::cin >> B;
	std::cout << "\nA: " << A << ", B: " << B << std::endl << std::endl;
	std::cout << "A U B: " << (A+B) << std::endl;
	r = rand()%256;
	std::cout << "A U {" << r << "}: " << (A+r) << std::endl;
	std::cout << "A \\ B: " << (A-B) << std::endl;
	r = rand()%256;
	std::cout << "A \\ {" << r << "}: " << (A-r) << std::endl;
	std::cout << "A n B: " << (A^B) << std::endl;
	r = rand()%256;
	std::cout << "A n {" << r << "}: " << (A^r) << std::endl << std::endl;
	std::cout << "U \\ A: " << (~A) << std::endl << std::endl;
	std::cout << "U: " << (+A) << std::endl << std::endl;
	std::cout << "Empty set: " << (-A) << std::endl;
	std::cout << "A <= B: " << (A<=B) << std::endl;
	std::cout << "A < B: " << (A<B) << std::endl;
	std::cout << "A >= B: " << (A>=B) << std::endl;
	std::cout << "A > B: " << (A>B) << std::endl;
	std::cout << "A == B: " << (A==B) << std::endl;
	std::cout << "A != B: " << (A!=B) << std::endl;
	std::cout << "!A: " << (!A) << std::endl;
	std::cout << "!!A: " << (!!A) << std::endl;
	std::cout << "Size of A: " << (A()) << std::endl;
	r = rand()%256;
	std::cout << "A[" << r << "]: " << (A[r]) << std::endl;
	return 0;
}
