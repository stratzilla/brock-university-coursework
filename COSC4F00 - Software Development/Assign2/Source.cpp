#include "stdafx.h"
#include <stdio.h>
#include <iostream>
#include <string>
#include <algorithm>
#include <thread>

using namespace std;

//method declarations
int tally_score(char, int[][3]);
void finish_game();
bool check_done(int[][3]);
void main_menu();
bool check_valid_move(char, int[][3]);
int minimax(int[][3], int, char);
void remove_peg(char, int[][3]);
void announce_state(int);
void xThread();
void oThread(int);

//global variables
string spacing = "       "; //for cleaner formatting
string half_spacing = "    "; //likewise
char current_player; //the current player
int board[8][3]; //game board
char possibleMoves[8]; //used in later method, indexes next moves
char userMove; //x input
int ai_state; //ai state
bool gotMove = false; //did x queue move?
bool doneMove = false; //did x's move complete?

/* initializes initial game state: player = X, board = empty*/
void initial_board() {
	current_player = (char)88;
	for (int i = 0; i < 8; i++) {
		for (int j = 0; j < 3; j++) {
			board[i][j] = 0;
		}
	}
}

/* prints the current board state
* argument b = board to print */
void print_board(int b[8][3]) {
	int jump[2] = { 0,3 }; //because its 3,2,3 board, not 3,3,3
	int pillar_char = 97; //A, increment so less code later
	cout << "\n\n";
	for (int k = 0; k < 3; k++) {
		for (int j = 0; j < 4; j++) {
			cout << spacing;
			for (int i = jump[0]; i < jump[1]; i++) {
				if (j < 3) {
					if (b[i][2 - j] == 0) { cout << (char)124; } // none
					if (b[i][2 - j] == 1) { cout << (char)88; } // X
					if (b[i][2 - j] == 2) { cout << (char)79; } // O
				}
				else {
					cout << (char)pillar_char++; //a,b,c, etc at bottom
				}
				cout << spacing;
			}
			if (k == 1) {
				cout << "\n" + half_spacing;
			}
			else {
				cout << "\n";
			}
		}
		if (k == 0) {
			cout << "\n" + half_spacing;
			jump[0] = 3;
			jump[1] = 5;
		}
		else if (k == 1) {
			cout << "\n";
			jump[0] = 5;
			jump[1] = 8;
		}
	}
	cout << "\n";
}

/* prints score of board state
* argument b = board state */
void print_score(int b[8][3]) {
	cout << spacing + half_spacing;
	cout << "X: " << tally_score('X', b) << half_spacing;
	cout << "O: " << tally_score('O', b) << "\n\n";
}

/* tallies up the score of a given player
* argument player is player to find score, b
* is board state in which to analyze score */
int tally_score(char player, int b[8][3]) {
	int p;
	int up = 0;
	//just to assign X = 1, O = 2
	if (player == 'X') {
		p = 1;
	}
	else {
		p = 2;
	}
	//horizontal checks
	if (b[0][0] == p && b[1][0] == p && b[2][0] == p) { up++; }
	if (b[0][1] == p && b[1][1] == p && b[2][1] == p) { up++; }
	if (b[0][2] == p && b[1][2] == p && b[2][2] == p) { up++; }
	if (b[5][0] == p && b[6][0] == p && b[7][0] == p) { up++; }
	if (b[5][1] == p && b[6][1] == p && b[7][1] == p) { up++; }
	if (b[5][2] == p && b[6][2] == p && b[7][2] == p) { up++; }
	if (b[0][0] == p && b[3][0] == p && b[6][0] == p) { up++; }
	if (b[0][1] == p && b[3][1] == p && b[6][1] == p) { up++; }
	if (b[0][2] == p && b[3][2] == p && b[6][2] == p) { up++; }
	if (b[1][0] == p && b[4][0] == p && b[7][0] == p) { up++; }
	if (b[1][1] == p && b[4][1] == p && b[7][1] == p) { up++; }
	if (b[1][2] == p && b[4][2] == p && b[7][2] == p) { up++; }
	if (b[5][0] == p && b[3][0] == p && b[1][0] == p) { up++; }
	if (b[5][1] == p && b[3][1] == p && b[1][1] == p) { up++; }
	if (b[5][2] == p && b[3][2] == p && b[1][2] == p) { up++; }
	if (b[6][0] == p && b[4][0] == p && b[2][0] == p) { up++; }
	if (b[6][1] == p && b[4][1] == p && b[2][1] == p) { up++; }
	if (b[6][2] == p && b[4][2] == p && b[2][2] == p) { up++; }
	//vertical checks
	if (b[0][0] == p && b[0][1] == p && b[0][2] == p) { up++; }
	if (b[1][0] == p && b[1][1] == p && b[1][2] == p) { up++; }
	if (b[2][0] == p && b[2][1] == p && b[2][2] == p) { up++; }
	if (b[3][0] == p && b[3][1] == p && b[3][2] == p) { up++; }
	if (b[4][0] == p && b[4][1] == p && b[4][2] == p) { up++; }
	if (b[5][0] == p && b[5][1] == p && b[5][2] == p) { up++; }
	if (b[6][0] == p && b[6][1] == p && b[6][2] == p) { up++; }
	if (b[7][0] == p && b[7][1] == p && b[7][2] == p) { up++; }
	//diagonal checks
	if (b[0][0] == p && b[1][1] == p && b[2][2] == p) { up++; }
	if (b[0][2] == p && b[1][1] == p && b[2][0] == p) { up++; }
	if (b[5][0] == p && b[6][1] == p && b[7][2] == p) { up++; }
	if (b[5][2] == p && b[6][1] == p && b[7][0] == p) { up++; }
	if (b[0][0] == p && b[3][1] == p && b[6][2] == p) { up++; }
	if (b[0][2] == p && b[3][1] == p && b[6][0] == p) { up++; }
	if (b[1][0] == p && b[4][1] == p && b[7][2] == p) { up++; }
	if (b[1][2] == p && b[4][1] == p && b[7][0] == p) { up++; }
	if (b[1][0] == p && b[4][1] == p && b[7][2] == p) { up++; }
	if (b[1][2] == p && b[4][1] == p && b[7][0] == p) { up++; }
	if (b[1][0] == p && b[4][1] == p && b[7][2] == p) { up++; }
	if (b[1][2] == p && b[4][1] == p && b[7][0] == p) { up++; }
	return up; //return score of given player
}

/* checks if a move is valid or not
* args position = peg to move onto, b = board state
* returns true if move is possible */
bool check_valid_move(char position, int b[8][3]) {
	for (int i = 0; i < 3; i++) {
		if (b[(int)position - 97][i] == 0) { //if space for move
			return true; //success!
		}
	}
	return false;
}

/* checks if a user move is within valid range
* args d = user inputted move
* returns true if in range */
bool valid_range(char d) {
	if ((int)d >= 65 && (int)d <= 72) { //if lowercase
		d += 32; //make uppercase
	}
	if ((int)d < 97 || (int)d > 104) { //if out of bound from A..H
		cout << "\nThis is an invalid move as that column doesn't exist.\n";
	}
	else if (!check_valid_move((int)d, board)) { //check if the move is valid
		cout << "\nThis is an invalid move as that column is full.\n";
	}
	else {
		return true; //its in range, ret T
	}
	return false;
}

/* makes move on certain position
* args player = moving player, position = peg to move onto
* b = board state
* returns true if successful */
bool place_peg(char player, char position, int b[8][3]) {
	if (check_valid_move(position, b)) { //if move is valid
		int thePlayer;
		if (player == (char)88) {
			thePlayer = 1; //because board is 0s, 1s, and 2s
		}
		else if (player == (char)79) {
			thePlayer = 2;
		}
		for (int i = 0; i < 3; i++) {
			//find lowest open position on peg
			if (b[(int)position - 97][i] == 0) {
				b[(int)position - 97][i] = thePlayer;
				return true; //success!
			}
		}
	}
	//if it isn't valid, ret. F
	return false;
}

/* removes a move, used for undoing
* args peg = peg to remove from, b = board state */
void remove_peg(char peg, int b[8][3]) {
	for (int i = 2; i >= 0; i--) { //start from top
		if (b[peg][i] != 0) { //if not empty
			b[peg][i] = 0; //empty it
			break;
		}
	}
}

/* checks if a board is full, thus game is over
* args b = board state
* returns true if board full */
bool check_done(int b[8][3]) {
	for (int i = 0; i < 8; i++) { //checks all pegs...
		if (b[i][2] == 0) { //if top one is empty
			return false; //can't be full
		}
	}
	return true;
}

/* summarizes the game state at end */
void finish_game() {
	char d;
	//if X points > O points
	if (tally_score('X', board) > tally_score('O', board)) {
		cout << "The winner is X!";
	}
	//if the opposite
	else if (tally_score('X', board) < tally_score('O', board)) {
		cout << "The winner is O!";
	}
	//if equal
	else {
		cout << "It's a tie!";
	}
	//lets you go back to main menu
	cout << "\n\nPress Q to return to main menu. ";
	while (true) {
		cin >> d;
		switch (d) {
		case 'q': case 'Q':
			main_menu();
			break;
		}
	}
}

/* two player mode */
void two_player() {
	initial_board(); //init empty board
	while (true) {
		char d;
		system("CLS"); //clear screen
		print_board(board); //print board
		print_score(board); //print score
		if (check_done(board)) { //if game over, break
			break;
		}
		//prompt current player for move
		cout << "Player " << current_player << ", choose a column: ";
		while (true) {
			cin >> d;
			if (valid_range(d)) { //if valid move, break
				place_peg(current_player, d, board); //place peg before break
				break;
			}
			//otherwise assume invalid move
			cout << "Player " << current_player << ", pick another column: ";
		}
		//swap players
		if (current_player == (char)88) {
			current_player = (char)79; //if X, make O
		}
		else {
			current_player = (char)88; //if O, make X
		}
	}
	finish_game(); //game over
}

/* vs. AI mode
* argument difficulty is how many future moves
* the AI should look ahead. It adjusts difficulty more or less */
void vs_computer(int difficulty) {
	initial_board(); //init blank board
	while (true) {
		int bestMove = -1000; //normally moves will give range -38...38
		system("CLS"); //clear screen
		print_board(board); //print current board state
		print_score(board); //print current score
		if (check_done(board)) { //if board full...
			break; //stop loop
		}
		ai_state = 0; //thinking...
		announce_state(0); //announce state
		thread first(oThread, difficulty); //AI thread
		thread second(xThread); //user thread
								//both functions will concurrently run
		first.join();
		second.join();
		/* wait for user to make his move, don't let
		* AI move until user has moved */
		if (doneMove) {
			//AI makes his move
			place_peg('O', (int)possibleMoves[userMove - 97], board);
		}
	}
	finish_game(); //announce winner, end game
}

/* prints the current state of the AI from thinking to waiting */
void announce_state(int state) {
	if (state == 0) {
		/* if not all future states analyzed,
		* print this message */
		cout << "Thinking about potential moves...\n";
	}
	else if (state == 1) {
		/* if all future states analyzed and just
		* waiting on user input, print this */
		string pl = ""; //just for formatting
		if (gotMove) { //if human move accepted
			pl = "\n\n";
		}
		cout << pl << "Got a move in mind, waiting for player input...\n\n";
	}
}

/* thread for human input */
void xThread() {
	cout << "\nPlayer X, choose a column: ";
	while (true) { //while move hasn't been played
		cin >> userMove;
		if (valid_range(userMove)) { //if user move is valid
			gotMove = true; //let game know a user has queued move
			if (ai_state == 1) {
				/* only do the below if AI has finished
				* thinking. I guess interrupting AI's thought process
				* when a move is made would be a neat stylistic touch
				* but I think this takes away from the assignment */
				place_peg(current_player, userMove, board); //initiate user move
				doneMove = true; //let game know user has made mode
				break; //stop
			}
		}
		else { //if invalid move, prompt for another choice
			cout << "Player X, pick another column: ";
		}
	}
}

/* thread for AI input
* argument difficulty is the difficulty of the AI */
void oThread(int difficulty) {
	/* the below is kind of wonky but it's the best
	* I could come up with. It analyzes all possible X moves
	* and comes up with the best response to that move by
	* using the minimax function. It finds the move that eventually
	* nets it the most points. The way it works is it examines two
	* moves: the next X move, then comes up with a response seeking
	* the best possible score. */
	for (int k = 0; k < 8; k++) {
		int bestMove = -1000;
		int tempBest = -1000;
		if (check_valid_move(k + 97, board)) { //examine valid x moves
			place_peg('X', k + 97, board); //place temp x
			for (int i = 0; i < 8; i++) {
				if (check_valid_move(i + 97, board)) { //examine valid o moves
					place_peg('O', i + 97, board); //place temp o
												   /* find the consequences of temp o, looks
												   * for score impact of that move */
					tempBest = minimax(board, difficulty, 'X');
					if (bestMove <= tempBest) {
						bestMove = tempBest;
						possibleMoves[k] = (int)i + 97;
					}
					remove_peg(i, board); //remove temp o
				}
			}
			remove_peg(k, board); //remove temp x
		}
	}
	announce_state(1); //announce AI is done thinking
	ai_state = 1; //let game know AI is done thinking
}

/* minimax function
* args b = current board state, depth = how deep to check
* potential future states, player = whose turn it in
*
* returns the highest score possible from as many game
* states as depth goes down. */
int minimax(int b[8][3], int depth, char player) {
	//if reached lowest depth or no moves possible
	if (depth <= 0 || check_done(b)) {
		//a good move for X is a bad move for O
		return ((player == 'X') ? -1 : 1)*tally_score(player, b);
	}
	/* let initial score to -1000; the effective range
	* is in the neg/pos double-digits */
	int highest = -1000;
	//iterate over 8 "pegs"
	for (int i = 0; i < 8; i++) {
		/* ... but only if a move is valid
		* it is possible a peg is full, this do not
		* consider */
		if (check_valid_move(i + 97, b)) {
			//if valid move, make the move
			place_peg(player, i + 97, b);
			//if player == x, make it O and vice-versa
			char tempPlayer = player;
			if (tempPlayer == 'X') { tempPlayer = 'O'; }
			else { tempPlayer = 'X'; }
			//recurse, find new highest
			highest = max(highest, -minimax(b, depth - 1, tempPlayer));
			//move is "undone" here: revert to prior state
			remove_peg(i, b);
		}
	}
	//return the highest score
	return highest;
}

/* prints the logo for the game
* completely unnecessary but adds a little flair I guess */
void logo() {
	cout << "\n";
	cout << "  333333333333333   DDDDDDDDDDDDD                      tttt               tttt               tttt          " << endl;
	cout << " 3:::::::::::::::33 D::::::::::::DDD                ttt:::t            ttt:::t            ttt:::t          " << endl;
	cout << " 3::::::33333::::::3D:::::::::::::::DD              t:::::t            t:::::t            t:::::t          " << endl;
	cout << " 3333333     3:::::3DDD:::::DDDDD:::::D             t:::::t            t:::::t            t:::::t          " << endl;
	cout << "             3:::::3  D:::::D    D:::::D      ttttttt:::::tttttttttttttt:::::tttttttttttttt:::::ttttttt    " << endl;
	cout << "             3:::::3  D:::::D     D:::::D     t:::::::::::::::::tt:::::::::::::::::tt:::::::::::::::::t    " << endl;
	cout << "     33333333:::::3   D:::::D     D:::::D     t:::::::::::::::::tt:::::::::::::::::tt:::::::::::::::::t    " << endl;
	cout << "     3:::::::::::3    D:::::D     D:::::D     tttttt:::::::tttttttttttt:::::::tttttttttttt:::::::tttttt    " << endl;
	cout << "     33333333:::::3   D:::::D     D:::::D           t:::::t            t:::::t            t:::::t          " << endl;
	cout << "             3:::::3  D:::::D     D:::::D           t:::::t            t:::::t            t:::::t          " << endl;
	cout << "             3:::::3  D:::::D     D:::::D           t:::::t            t:::::t            t:::::t          " << endl;
	cout << "             3:::::3  D:::::D    D:::::D            t:::::t    tttttt  t:::::t    tttttt  t:::::t    tttttt" << endl;
	cout << " 3333333     3:::::3DDD:::::DDDDD:::::D             t::::::tttt:::::t  t::::::tttt:::::t  t::::::tttt:::::t" << endl;
	cout << " 3::::::33333::::::3D:::::::::::::::DD              tt::::::::::::::t  tt::::::::::::::t  tt::::::::::::::t" << endl;
	cout << " 3:::::::::::::::33 D::::::::::::DDD                  tt:::::::::::tt    tt:::::::::::tt    tt:::::::::::tt" << endl;
	cout << "  333333333333333   DDDDDDDDDDDDD                       ttttttttttt        ttttttttttt        ttttttttttt  " << endl;
	cout << "\n\n";
}

/* generates the main menu for the game */
void main_menu() {
	char d, e;
	while (true) {
		system("CLS"); //clear screen
		logo(); //show logo
		cout << "\t\t\t 1. Two-Player" << half_spacing; //2P mode
		cout << "2. VS. Computer" << half_spacing; //1P vs AI mode
		cout << "Select an option: ";
		cin >> d; //user input
		switch (d) {
		case '1': case 1: //start 2P game
			two_player();
			break;
		case '2': case 2: //open 1P submenu
			/* various difficulty settings, they vary
			* by changing the depth of the minimax function */
			cout << "\n a: Very Easy" << half_spacing;
			cout << "b: Easy" << half_spacing;
			cout << "c: Medium" << half_spacing;
			cout << "d: Hard" << half_spacing;
			cout << "q: Back to Main Menu" << "\n\n"; //return to prior menu
			cout << "Select a Difficulty Level: ";
			cin >> e;
			switch (e) {
			case 'a': case 'A':
				vs_computer(1);
				break;
			case 'b': case 'B':
				vs_computer(3);
				break;
			case 'c': case 'C':
				vs_computer(5);
				break;
			case 'd': case 'D':
				vs_computer(6);
				break;
			case 'q': case 'Q':
				break;
			}
			break;
		}
	}
}

int main() {
	main_menu();
	return 0;
}