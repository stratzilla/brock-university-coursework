#include "Player.hpp"
#include <math.h>
#include <limits.h>
#include <algorithm>

/**
 * Computer class declaration
 * derived class from base Player class
 */
class Computer : public Player {
	private:
		unsigned int depth;
		unsigned int evalCount;
		const static unsigned int BUFFER = 3;
		Move bestMove;
		std::vector<std::string> moveQueue;
		
		// private member methods
		Move negamaxHandler();
		int negamax(Board*, unsigned int, int, int, bool);
		int evalBoard(Board*);
		int getValues(Board*);
		int getMobility(Board*);
		int getPawns(Board*);
		void preventSelfCheck(std::vector<Move>&);
		void printData(int);
		
		// accessor methods
		unsigned int getDepth();
		
		// mutator methods
		void setDepth(unsigned int);
	public:
		Computer(bool, unsigned int);
		
		// member methods
		Move promptMove(bool);
};