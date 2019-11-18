#include <iostream>
#include <cmath>
#include <signal.h>
#include <unistd.h>
#include <pthread.h>
#include <array>
#include <float.h>
#include <sstream>

// globals
const static unsigned int DIMENSIONS = 2; // how many dimensions
std::array<double, DIMENSIONS> bestPosition; // best position so far
double bestResult = DBL_MAX; // arbitrarily large
volatile unsigned int occupied; // for thread count
volatile bool continuing; // for thread while loop
pthread_mutex_t updateLock; // lock

// function declarations
bool checkInBounds(std::array<double, DIMENSIONS>, int, int);
std::array<double, DIMENSIONS> getRandPosition(int, int);
std::string printBest();

/**
 * Egg Holder function evaluation function
 * @param p - the position to evaluate
 * @return - the fitness of that position (lower is better)
 */
double eggHolderEvaluation(std::array<double, DIMENSIONS> p) {
	double sum; // to aggregate dimensions
	for (unsigned int i = 0; i < DIMENSIONS-1; i++) {
		// split for readability, this is the EH summation
		double subSum = (p[i+1] + 47) * (std::sin(std::sqrt(std::abs((p[i]/2) + p[i+1] + 47))));
		subSum -= (p[i] * std::sin(std::sqrt(std::abs(p[i] - p[i+1] - 47))));
		subSum *= (-1);
		sum += subSum; // append it to aggregate sum
	}
	return sum;
}

/**
 * Hill Climbing worker function, one per thread
 */
void* hillClimb(void* ignore) {
	std::array<double, DIMENSIONS> position; // position for evaluation later
	std::array<double, DIMENSIONS> stoch; // stochastic element
	double best, tempBest; // best of thread, temporary best used later
	while (continuing) { // while thread alive
		position = getRandPosition(-512, 512); // find a random position -512..512 for each dimension
		best = eggHolderEvaluation(position); // find fitness of that position
		while (checkInBounds(position, -512, 512)) { // while the position is within bounds
			for (unsigned int i = 0; i < 4; i++) { // four possible moves
				stoch = getRandPosition(-5, 5); // stochastic summand position -5..5
				std::array<double, DIMENSIONS> tempPos; // for adding
				// add the stochastic element to the position
				for (unsigned int j = 0; j < DIMENSIONS; j++) { tempPos[j] = stoch[j] + position[j]; }
				tempBest = eggHolderEvaluation(tempPos); // evaluate that new position
				if (tempBest < best) { // if better than loop
					position = tempPos; // overwrite
					best = tempBest;
				}
			}
			// only one lock is needed as output is performed as updates are made
			pthread_mutex_lock(&updateLock);
			if (best < bestResult) { // if better than global
				bestPosition = position; // overwrite
				bestResult = best;
				std::cout << "New minimum: " << printBest();
			}
			pthread_mutex_unlock(&updateLock); // unlock
		}
	}
	occupied--; // note number of active threads
	return 0;
}

/**
 * function to find a random position
 * @param l - lower bound
 * @param h - higher bound
 * @return - a random position in all dimensions l..h
 */
std::array<double, DIMENSIONS> getRandPosition(int l, int h) {
	std::array<double, DIMENSIONS> p;
	for (unsigned int i = 0; i < DIMENSIONS; i++) { // for each dimension
		// generate random position
		p[i] = l + (double)(rand() / (double)RAND_MAX) * (h - l);
	}
	return p;
}

/**
 * function to check if a position is within bounds
 * @param p - the position to check
 * @param l - lower bound
 * @param h - higher bound
 * @return - whether p is within l..h
 */
bool checkInBounds(std::array<double, DIMENSIONS> p, int l, int h) {
	for (unsigned int i = 0; i < DIMENSIONS; i++) { // for each dimension
		// first dimension proc chance may come earlier; return F on per dimension basis
		if (p[i] > h || p[i] < l) { return false; }
	}
	// if it gets here, it's all within bounds
	return true;
}

/**
 * function to print the best to user
 * @return - the function with params and its evaluation
 */
std::string printBest() {
	std::stringstream toReturn;
	toReturn << "f(";
	for (unsigned int i = 0; i < DIMENSIONS; i++) {
		toReturn << bestPosition[i];
		if (i < DIMENSIONS - 1) { toReturn << ", "; }
	}
	toReturn << ") = " << bestResult << "\n";
	return toReturn.str().c_str();
}

// interrupt handler
void interrupted(int signal) {
    continuing = false;
}

// check best handler
void checkBest(int signal) {
	std::cout << "Best so far: " << printBest();
}

int main (int argc, char* argv[]) {
	srand(time(NULL));
	signal(SIGINT, interrupted);
	signal(SIGUSR1, checkBest);
	int choice = atoi(argv[1]);
	pthread_t threads[8];
	if (argc != 2) { std::cout << "Too few parameters." << std::endl; return 1; }
	if (choice > 8) { std::cout << "Too many climbers." << std::endl; return 1; }
	if (choice < 1) { std::cout << "Too few climbers." << std:: endl; return 1; }
	continuing = true;
	for (int i = 0; i < choice; i++) {
		pthread_create(&threads[i], NULL, &hillClimb, NULL);
		occupied++;
	}
	while (continuing && occupied > 0) { sleep(1); }
	std::cout << "\nBest in run: " << printBest();
    return 0;
}
