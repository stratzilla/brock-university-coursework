#include <iostream>
#include <unistd.h>
#include <cstdlib>
#include <signal.h>
#include <pthread.h>
#include <sstream>

volatile int target;
volatile bool continuing;
volatile int occupied;
static const unsigned int NUM_THREADS = 4;
pthread_mutex_t occupiedLock;
pthread_mutex_t outputLock;

void genULong(unsigned long&);
std::string printThirtyTwo(unsigned int);
std::string printSixtyFour(unsigned long);
int leadingZeroes(unsigned int);
unsigned int calcHash(unsigned long);

void* hashWork(void* unnecessary) {
	unsigned long nonce;
	unsigned int hash;
	while (continuing) {
		genULong(nonce);
		hash = calcHash(nonce);
		if (leadingZeroes(hash) >= target) {
			sleep(1);
			pthread_mutex_lock(&outputLock);
			std::cout << "Mining: " << printSixtyFour(nonce) << std::endl;
			std::cout << "Hashed: " << printThirtyTwo(hash) << "\n" << std::endl;
			pthread_mutex_unlock(&outputLock);
		}
	}
	pthread_mutex_lock(&occupiedLock);
	occupied--;
	std::cout << "Thread is done." << std::endl;
	pthread_mutex_unlock(&occupiedLock);
}

std::string printThirtyTwo(unsigned int word) {
	std::stringstream output;
	for (int i = 0; i < 32; i++) {
		output << (((0x80000000 >> i) & word) ? 1 : 0);
	}
	return output.str().c_str();
}

std::string printSixtyFour(unsigned long word) {
	std::stringstream output;
	for (int i = 0 ; i < 64; i++) {
		output << (((0x8000000000000000 >> i) & word) ? 1 : 0);
	}
	return output.str().c_str();
}

unsigned int calcHash(unsigned long nonce) {
	unsigned int hash = 0;
	for (int i = 15 ; i >= 0; i--) {
		hash = 17 * hash + ((nonce >> (4 * i)) & 0x0F);
	}
	return hash;
}

void genULong(unsigned long &nonce) {
	nonce = 0;
	for (int i = 63; i >= 0; i--) {
		nonce <<= 1;
		nonce |= random() % 2;
	}
}

int leadingZeroes(unsigned int value) {
	for (int i = 0; i < 32; i++) {
		if ((value >> (31-i)) & 1) { return i; }
	}
	return 32;
}

void interrupted(int sig) {
	continuing = false;
	std::cout << "\nComputations complete. \nHalting now..." << std::endl;
}

int main(void) {
	srand(time(NULL));
	pthread_t ct[NUM_THREADS];
	if (signal(SIGINT, interrupted) == SIG_ERR) {
		std::cout << "Unable to change signal handler." << std::endl;
		return 1;
	}
	while (true) {
		int c;
		std::cout << "How many leading zeroes? (0 to Quit): "; std::cin >> c;
		if (c <= 0) { break; }
		target = c;
		continuing = true;
		for (unsigned int i = 0; i < NUM_THREADS; i++) {
			pthread_mutex_lock(&occupiedLock);
			pthread_create(&ct[i], NULL, &hashWork, NULL);
			occupied++;
			pthread_mutex_unlock(&occupiedLock);
		}
		while (occupied > 0) { sleep(1); }
	}
	return 0;
}