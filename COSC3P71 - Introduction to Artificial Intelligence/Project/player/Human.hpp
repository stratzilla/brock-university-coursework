#include "Player.hpp"

/**
 * Human class declaration
 * derived class from base Player class
 */
class Human : public Player {
	private:
		// private member methods
		void errorMessage(unsigned int);
		bool verifyInput(std::string);
		bool verifyShow(std::string);
		bool checkValid(Move);
		bool checkOwner(bool, unsigned int, unsigned int);
		bool checkPiece(unsigned int, unsigned int);
		bool stillInCheck(Move);
		void saveBoard();
		void quitGame();
	public:
		Human(bool); // constructor
		
		// public member methods
		Move promptMove(bool);
};