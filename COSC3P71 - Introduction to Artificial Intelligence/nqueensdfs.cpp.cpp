#include <stdio.h>
#include <stdlib.h>
#include <iostream>
#include <vector>

using namespace std;

vector<vector<int> > constructBoard(int);
void printBoard(vector<vector<int> >);
bool solve(vector<vector<int> >&, unsigned int);
int checkThreats(vector<vector<int> >, int, int);

int attempts = 0; // for counting piece movements

/**
 * main driver
 * 
 * @param argv[1] - the size of the NxN board and N queens
 **/
int main(int argc, char *argv[]) {
    if (argc < 2) { // if insufficient arguments
        cout << "Missing or invalid argument.\n";
        cout << "Try './execute N', where N is an integer.\n";
        return 1;
    }
    // this is the N used for declaring size of board and number of queens
    int n = (int)strtol(argv[1], NULL, 10);
    // create the n x n board
    vector<vector<int> > board = constructBoard(n);
    if (!solve(board, 0)) { // if unsolvable
        cout << "There are no solutions for placing ";
        cout << n << " queens on an " << n << "x" << n << " board.\n\n";
        cout << attempts << " piece placements were tried.\n"; 
    } else { // if solvable
        cout << "There exists a solution for placing ";
        cout << n << " queens on an " << n << "x" << n << " board.\n\n";
        cout << attempts << " piece placements were tried.\n\n";
        cout << "Here is an example solution: \n\n";
        printBoard(board); // print final result
    }
    return 0;
}

/**
 * function to give a solution for placing n queens on an nxn board
 * 
 * @param board - the board to solve
 * @param start - the column in which to start the algorithm on
 * @return - a T/F value on whether it is solvable
 **/
bool solve(vector<vector<int> > &board, unsigned int start) {
    attempts++; // at every step, increment the number of attempts
    if (start >= board[0].size()) { // if we've reached the end
        return true; // stop it, declare it solvable
    }
    /**
     * the way this works is it goes left to right, placing queens
     * in a column and then making sure the next placement on the
     * next column is safe. If it isn't, it backtracks to the previous column
     * and tries another position. The depth-first search is exhaustive
     * and assuredly finds a solution but not with any speed.
     **/
    for (unsigned int i = 0; i < board[0].size(); i++) {
        // if the potential queen position is unthreatened
        if (checkThreats(board, i, start) == 0) {
            board[i][start] = 1; // place a queen
            // recurse into deeper columns
            if (solve(board, start+1)) {
                /**
                 * eventually, it will hit the base case a few lines
                 * above and return true, meaning it is solvable.
                 **/
                return true;
            }
            /**
             * but if it ever gets to this point, it must backtrack
             * by removing the queen and trying a new position.
             **/
            board[i][start] = 0;
        }
    }
    // if for some reason a solution can't be found return F
    return false;
}

/**
 * threat checking algorithm
 * 
 * this is mondo ugly but the best way I could deal with it. I'm
 * reminded of a time in 2P03 I think where we had to make a maze
 * solver and it took me hours to figure out diagonal
 * array traversal because it's a major headache. I was dreading
 * dealing with it here. I couldn't find a more elegant method so you'll have
 * to endure this monstrosity for the time being.
 * 
 * @param board - the board to check threats upon
 * @param row - the row to check
 * @param col - the column to check
 * @return - the number of threats a given space has
 **/
int checkThreats(vector<vector<int> > board, int row, int col) {
    int threats = 0; // assume no threats initially
    if (board[row][col] == 1) {
        /**
         * it will return nonzero threats if the checked
         * position contains a queen as it checks if it threatens
         * itself. Maybe I can avoid this in a v2, I'm sure it's trivial
         **/
        threats-= 2;
    }
    for (int i = 0; i < board.size(); i++) { // check up/down
        if (board[row][i] == 1) {
            threats++;
        }
    }
    for (int i = 0; i < board.size(); i++) { // check up/down
        if (board[i][col] == 1) {
           threats++;
        }
    }
    // check lower-right diagonal
    for (int i = row+1, j = col+1; i < board.size() && j < board.size(); i++, j++) {
        if (board[i][j] == 1) {
           threats++;
        }
    }
    // check upper-left diagonal
    for (int i = row-1, j = col-1; i >= 0 && j >= 0; i--, j--) {
        if (board[i][j] == 1) {
           threats++;
        }
    }
    // check lower-left diagonal
    for (int i = row-1, j = col+1; i >= 0 && j < board.size(); i--, j++) {
        if (board[i][j] == 1) {
           threats++;
        }
    }
    // check upper-right diagonal
    for (int i = row+1, j = col-1; i < board.size() && j >= 0; i++, j--) {
        if (board[i][j] == 1) {
           threats++;
        }
    }
    return threats;
}

/**
 * function to construct the board
 * 
 * basically just creates the vector array outside of the main
 * driver. Makes it look nicer, plus I reused it in debug
 * 
 * @param n - the size of the square board
 * @return - an empty NxN vector array
 **/
vector<vector<int> > constructBoard(int n) {
    vector<vector<int> > board(n, vector<int>(n));
    for (unsigned int i = 0; i < board.size(); i++) {
        for (unsigned int j = 0; j < board[0].size(); j++) {
            board[i][j] = 0;
        }
    }
    return board;
}

/**
 * function to print the board
 * 
 * @param board - the board to print
 **/
void printBoard(vector<vector<int> > board) {
    for (unsigned int i = 0; i < board.size(); i++) {
        for (unsigned int j = 0; j < board[0].size(); j++) {
            if (board[i][j] == 1) {
                cout << "Q ";
            } else {
                cout << "_ ";
            }
        }
        cout << "\n";
    }
}