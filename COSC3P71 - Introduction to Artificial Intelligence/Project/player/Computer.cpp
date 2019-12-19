#include "Computer.hpp"

/**
 * Computer class definition
 */

/**
 * Computer class constructor
 * @param c - color of this player
 * @param d - the depth for AI tree search
 */
Computer::Computer(bool c, unsigned int d) {
	setColor(c);
	setDepth(d);
	evalCount = 0;
}

/**
 * method to prompt user for their move
 * @param c - the user's color
 * @return - the move they've chosen
 */
Move Computer::promptMove(bool c) {
	// perform AI routine
	return negamaxHandler(); // as found by negamax
}

/**
 * negamax handler
 * since negamax deals with integers, we need
 * a way to devise a move with the highest value
 * as provided by negamax. This method effectively
 * takes care of the first sequential board state
 * and the inner negamax method deals with beyond
 * @return - AI determined best move
 */
Move Computer::negamaxHandler() {
	// get all moves you can make
	std::vector<Move> moveList = getBoard()->getAllMoves(!getColor());
	/**
	 * the AI is smart enough to look moves ahead
	 * but not smart enough to realize check is occurring
	 * immediately after a move. It will look at moves ahead
	 * and may see a move which benefits it more (eg. checkmate)
	 * but this move presupposes check doesn't exist. For this
	 * reason, remove moves that would put the king into check
	 */
	preventSelfCheck(moveList);
	int bestMoveValue = INT_MIN; // initially -inf
	std::vector<Move> bestMoves; // collection of best moves
	// for each possible move
	for (unsigned int i = 0; i < moveList.size(); i++) {
		/**
		 * to avoid threefold repetition, we need to handle cases
		 * where a move is repeated. Mostly evident in AI vs AI games
		 * where it will go on forever as they move pieces back
		 * and forth ad infinitum. This prevents reuse of the previous moves
		 * the amount of which is determined by a buffer. Added benefits
		 * of less negamax calls meaning faster speed
		 */
		if (std::find(moveQueue.begin(), moveQueue.end(), moveList[i].stateMove()) != moveQueue.end()) {
			continue;
		}
		Board* copy = new Board(*getBoard()); // make a new board copy
		copy->movePiece(moveList[i]); // make move on copy
		// find value of that move
		int value = negamax(copy, getDepth(), INT_MIN, INT_MAX, !getColor());
		// if move has same worth
		if (value == bestMoveValue ) {
			// add it to the collection
			bestMoves.push_back(moveList[i]);
		} else if (value > bestMoveValue) {
			// add it to the list of possible moves and update best score
			bestMoveValue = value;
			bestMoves.clear(); // don't need worse moves
			bestMoves.push_back(moveList[i]);
		}
	}
	printData(bestMoveValue); // show some data
	/**
	 * because some moves will be worth the same as others, we'll have
	 * a collection of the best moves here. We'll choose one stochastically
	 */
	Move theBestMove = bestMoves[rand() % bestMoves.size()];
	moveQueue.push_back(theBestMove.stateMove()); // add best move to buffer
	// keep buffer size consistent
	if (moveQueue.size() > BUFFER) { moveQueue.erase(moveQueue.begin()); }
	return theBestMove;
}

/**
 * negamax implementation using alpha-beta pruning
 * will search tree space recursively but without
 * the typical two function calls that minimax uses
 * negamax relies on the property of:
 * max(alf, bet) == -min(-alf, -bet)
 * which is equivalent or better than vanilla minimax
 * @param b - the board to checkmate
 * @param d - the depth to use to break out of algorithm
 * @param alf - alpha
 * @param bet - beta
 * @param p - the calling player
 * @return - best evaluation for AI
 */
int Computer::negamax(Board* b, unsigned int d, int alf, int bet, bool p) {
	evalCount++; // increment count to display positions evaluated
	// terminal case would be stalemate or checkmate
	if (d == 0) { // base recursive case
		return (p ? 1 : -1) * evalBoard(b); // return that board's evaluation
	}
	/**
	 * consider a checkmate as best possible configuration
	 * this will become -INT_MAX were it the minimizing
	 * player, so it handles both cases
	 */
	if (b->determineCheckmate(p)) { return INT_MAX; }
	// consider a stalemate is better than losing
	if (b->determineStalemate(p)) { return 0; }
	int value = INT_MIN; // initially minimum (will overwrite)
	std::vector<Move> moveList = b->getAllMoves(!p); // get moves
	for (unsigned int i = 0; i < moveList.size(); i++) {
		Board* copy = new Board(*b); // make a new board copy
		copy->movePiece(moveList[i]); // make move on copy
		// then recurse
		int v = -negamax(copy, d-1, -bet, -alf, !p);
		value = std::max(value, v);
		alf = std::max(alf, value);
		if (alf >= bet) { break; } // cutoff for branches
	}
	return value;
}

/**
 * method to evaluate a board's worth
 * @param b - the board to evaluate
 * @return - total mobility according to AI
 */
int Computer::evalBoard(Board* b) {
	/**
	 * board evaluation is done in three steps:
	 *  1. find the values of all pieces
	 *  2. find the mobility of all pieces
	 *  3. find the pawn control
	 * then evaluation considers the total board
	 * value plus the board control plus pawn control
	 */
	int value = getValues(b); // material worth of board
	int mobility = getMobility(b); // board control
	int pawns = getPawns(b); // pawn control
	/**
	 * some coefficients to balance the weights
	 * of the various metrics about the board
	 */
	int c1 = 15, c2 = 3, c3 = 6;
	// evaluation is a function of material, board control, pawn control
	return c1*value + c2*mobility + c3*pawns;
}

/**
 * method to determine the value worth of a board
 * @param b - the board to evaluate
 * @return - total value according to AI
 */
int Computer::getValues(Board *b) {
	// tabulate total piece value of AI pieces
	unsigned int ownValues = b->getAllPieceValues(getColor());
	// tabulate total piece value of Human pieces
	unsigned int oppValues = b->getAllPieceValues(!getColor());
	// the values of a board according to AI is (AI-Human)
	return (ownValues - oppValues);
}

/**
 * method to determine the mobility worth of a board
 * @param b - the board to evaluate
 * @return - total mobility according to AI
 */
int Computer::getMobility(Board *b) {
	// tabulate total number of moves AI can make
	unsigned int ownMoves = b->getAllMoves(!getColor()).size();
	// tabulate total number of moves Human can make
	unsigned int oppMoves = b->getAllMoves(getColor()).size();
	// the mobility of a board according to AI is (AI-Human)
	return (ownMoves - oppMoves);
}

/**
 * method to determine pawn worth of a board
 * @param b - the board to evaluate
 * @return - total pawn worth
 */
int Computer::getPawns(Board *b) {
	return b->getPawnControl(getColor());
}

/**
 * method to prevent AI from entering check
 * @param m - the movelist to mutate
 */
void Computer::preventSelfCheck(std::vector<Move>& m) {
	std::vector<int> indexes; // so the indices don't get clobbered
	for (unsigned int i = 0; i < m.size(); i++) {
		// create a copy and make move on it
		Board* copy = new Board(*getBoard());
		copy->movePiece(m[i]);
		// if move would put into check
		if (copy->determineCheck(getColor())) {
			indexes.insert(indexes.begin(), i);
		}
	}
	// remove the moves that would put king in check
	for (unsigned int i = 0; i < indexes.size(); i++) {
		m.erase(m.begin() + indexes[i]);
	}
}

/**
 * method to let console know number of
 * game states evaluated as well as the score
 * the AI gave the board
 * @param s - the score
 */
void Computer::printData(int s) {
	std::cout << "\n" << evalCount << " game states evaluated.\n";
	std::cout << (getColor() ? "White" : "Black");
	std::cout << " chose a move with score " << s << ".\n";
	evalCount = 0;
}

// accessor methods
unsigned int Computer::getDepth() { return depth; }

// mutator methods
void Computer::setDepth(unsigned int d) { depth = d; }