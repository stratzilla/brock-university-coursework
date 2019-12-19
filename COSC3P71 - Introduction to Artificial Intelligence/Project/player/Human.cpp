#include "Human.hpp"

/**
 * Human class definition
 */

/**
 * Human class constructor
 * @param c - the color of the player
 */
Human::Human(bool c) {
	setColor(c);
}

/**
 * method to prompt user for their move
 * @param c - the user's color
 * @return - the move they've chosen
 */
Move Human::promptMove(bool c) {
	std::string s;
	std::cout << "\n";
	while(true) { // continue prompting until valid
		std::cout << "Enter a move in the form of 'a0b1' (from a0 to b1).\n";
		std::cout << "Or \"save\" to save game, \"quit\" to quit game.\n";
		std::cout << "Move: "; std::cin >> s;
		// if showing moves
		if (s.size() == 2) {
			if (!verifyShow(s)) {
				errorMessage(7);
				std::cout << "\n";
			} else {
				getBoard()->showMoves(s[0]-97, s[1]-49);
			}
			continue;
		}
		// if saving board state
		if (s == "save") {
			saveBoard();
			continue; // do not quit on save
		}
		// if quitting game
		if (s == "quit") {
			quitGame();
		}
		// if legal string
		if (!verifyInput(s)) {
			errorMessage(1);
		// if piece exists
		} else if (!checkPiece(s[0]-97, s[1]-49)) {
			errorMessage(2);
		// if user owns the piece
		} else if (!checkOwner(c, s[0]-97, s[1]-49)) {
			errorMessage(3);
		// if the move is valid
		} else if (!checkValid(Move(s[0]-97, s[1]-49, s[2]-97, s[3]-49))) {
			errorMessage(4);
		// if move would not bring out of check
		} else if (stillInCheck(Move(s[0]-97, s[1]-49, s[2]-97, s[3]-49))) {
			errorMessage(6);
		} else { break; }
		std::cout << "\n";
	}
	return Move(s[0]-97, s[1]-49, s[2]-97, s[3]-49);
}

/**
 * method to verify if a piece exists
 * @param c, r - the coordinate to check
 * @return - whether that piece exists
 */
bool Human::checkPiece(unsigned int c, unsigned int r) {
	return (*getBoard())(c, r);
}

/**
 * method to verify if moves can be shown
 * @param s - the coordinate to check
 * @return - whether moves can be shown
 */
bool Human::verifyShow(std::string s) {
	// if destination column is valid
	if (s[0] < 97 || s[0] > 104) { return false; }
	// if destination row is valid
	if (s[1] < 49 || s[1] > 56) { return false; }
	// if a piece exists on that tile
	if (!(*getBoard())(s[0]-97, s[1]-49)) { return false; }
	return true;
}

/**
 * method to verify the input is legal
 * @param s - the string input
 * @return - whether it's legal or not
 */
bool Human::verifyInput(std::string s) {
	// valid strings are exactly 4 characters
	if (s.length() != 4) { return false; }
	// if origin column is valid
	if (s[0] < 97 || s[0] > 104) { return false; }
	// if origin row is valid
	if (s[1] < 49 || s[1] > 56) { return false; }
	// if destination column is valid
	if (s[2] < 97 || s[2] > 104) { return false; }
	// if destination row is valid
	if (s[3] < 49 || s[3] > 56) { return false; }
	return true;
}

/**
 * method to verify ownership of piece
 * @param c - the color of the user
 * @param c, r - the coordinate to check
 * @return - whether piece at coords belongs to user
 */
bool Human::checkOwner(bool col, unsigned int c, unsigned int r) {
	return col == (*getBoard())(c, r).getPiece().getColor();
}

/**
 * method to verify if move is legal
 * @param m - the proposed move
 * @return - whether move is valid
 */
bool Human::checkValid(Move m) {
	Tile t = (*getBoard())(m.getOrigC(), m.getOrigR());
	std::vector<Move> moveList = t.getPiece().getMoves(getBoard(), m.getOrigC(), m.getOrigR());
	// check if m is within the move list
	for (unsigned int i = 0; i < moveList.size(); i++) {
		if (m == moveList[i]) { return true; }
	}
	return false;
}

/**
 * method to verify if a move would leave or remain in check
 * @param m - the proposed move
 * @return - whether move leaves check
 */
bool Human::stillInCheck(Move m) {
	// if in check
	if (getBoard()->determineCheck(getColor())) {
		// make a copy of the board
		Board copy((*getBoard()));
		// and make the move on the copy
		copy.movePiece(m);
		// if the copy shows in check still, bad move
		if (copy.determineCheck(getColor())) { return true; }
	}
	// if wasn't in check or able to leave check
	return false;
}

/**
 * method to handle errors from invalid inputs
 * @param i - the type of error
 */
void Human::errorMessage(unsigned int i) {
	std::cout << "\n";
	switch(i) {
		case 1: std::cout << "This is not a valid input.\n"; break;
		case 2: std::cout << "There is no piece to move.\n"; break;
		case 3: std::cout << "This piece is not your color.\n"; break;
		case 4: std::cout << "This move is an illegal move.\n"; break;
		case 5: std::cout << "There was no filename specified.\n"; break;
		case 6: std::cout << "You must move out of check.\n"; break;
		case 7: std::cout << "This is not a valid position.\n"; break;
	}
}

// method to handle game saving
void Human::saveBoard() {
	std::string s;
	std::cout << "\n";
	while(true) { // continue prompting until valid
		std::cout << "Enter name for save file.\n";
		std::cout << "Filename: "; std::cin >> s;
		// if no filename given
		if (s.length() < 1) {
			errorMessage(5);
		// if filename given
		} else { break; }
		std::cout << "\n";
	}
	getBoard()->saveToFile(s, getColor());
	std::cout << "\n";
}

// method to handle game quitting
void Human::quitGame() {
	exit(0); // impossible to unwrap into main() unfortunately
}