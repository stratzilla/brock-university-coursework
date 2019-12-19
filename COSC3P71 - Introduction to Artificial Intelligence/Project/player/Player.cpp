#include "Player.hpp"

/**
 * Player class definition
 */
 
/**
 * method to prompt user for move
 * default behavior implicitly overriden by derived classes
 * in other words, never used
 */
Move Player::promptMove(bool c) {
	return Move(0, 0, 0, 0);
}

// accessor methods
bool Player::getColor() { return color; }
Board* Player::getBoard() { return gameboard; }

// mutator methods
void Player::setColor(bool c) {	color = c; }
void Player::setBoard(Board* b) { gameboard = b; }