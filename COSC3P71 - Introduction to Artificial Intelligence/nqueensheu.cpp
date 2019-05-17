#include <stdio.h>
#include <stdlib.h>
#include <iostream>
#include <vector>
#include <cstdlib>
#include <limits.h>

using namespace std;

vector<vector<int> > constructBoard(int);
void printBoard(vector<vector<int> >);
bool solve(vector<vector<int> >&, unsigned int);
void randomPlacement(vector<vector<int> >&);
int checkThreats(vector<vector<int> >, int, int);
int checkHorVer(vector<vector<int> >, int, int);
int checkDiag(vector<vector<int> >, int, int);
int getRand(int);
bool newSolve(vector<vector<int> >&);
bool boardThreats(vector<vector<int> >);

int attempts = 0; // for counting the number of attempts at soln

/**
 * main driver
 * 
 * @param argv[1] - n as stated in the assignment guidelines
 * @param argv[2] - the seed value used
 **/
int main(int argc, char *argv[]) {
    if (argc < 3) { // if insufficient arguments
        cout << "Missing or invalid arguments.\n";
        cout << "Try './execute N M', where N is an integer and M is seed.\n";
        return 1;
    }
    srand(strtol(argv[2], NULL, 10)); // set up rand seed
    int n = (int)strtol(argv[1], NULL, 10);
    vector<vector<int> > board = constructBoard(n); // create board
    randomPlacement(board); // place queens at random acc. to seed
    if (!newSolve(board)) { // if cannot solve
        cout << "There are no solutions for placing ";
        cout << n << " queens on an " << n << "x" << n << " board ";
        cout << "using seed " << argv[2] << ".\n\n";
        cout << attempts << " piece placements were tried.\n"; 
    } else { // if can solve!
        cout << "There exists a solution for placing ";
        cout << n << " queens on an " << n << "x" << n << " board ";
        cout << "using seed " << argv[2] << ".\n\n";
        cout << attempts << " piece placements were tried.\n\n";
        cout << "Here is an example solution: \n\n";
        printBoard(board);
    }
    return 0;
}

/**
 * function to place the queens at random spots
 * 
 * this puts one queen on each row at random
 * 
 * @param board - the board to place on
 **/
void randomPlacement(vector<vector<int> > &board) {
    for (int i = 0; i < board.size(); i++) {
        int j = getRand(board.size());
        board[i][j] = 1;
    }
}

/**
 * function to check how many threats a given position has
 * 
 * see comments for part a, but basically it checks
 * if there exists queens up/down/left/right/diagonally from
 * a given position, and numerates this
 * 
 * @param board - the board to check threat levels on
 * @param row - the row of the position to check
 * @param col - the col of the position to check
 * @return - the number of threats of a given row,col co-ord
 **/
int checkThreats(vector<vector<int> > board, int row, int col) {
    int threats = 0;
    if (board[row][col] == 1) {
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
 * function to check how threatening the board is
 * 
 * checks each queen position to see if it has any threats
 * a solution is found when each queen has 0 threat level
 * 
 * @param board - the board to check
 * @return - T/F whether threats exist to any queen
 **/
bool boardThreats(vector<vector<int> > board) {
    int threats = 0;
    for (int i = 0; i < board.size(); i++) {
        for (int j = 0; j < board.size(); j++) {
            // short circuited to save time
            if (board[i][j] == 1 // if queen
            && checkThreats(board, i, j)) { // if threat exists at Q pos
                return true;
            }
        }
    }
    return false;
}

/**
 * function to find the least threatened position on board
 * 
 * a queen should be moved to a position with the
 * lowest threat level, so we need to find this.
 * if there exist ties, they're all brought over
 * 
 * @param board - the board to check upon
 * @return - a sequence of safest spots to move to
 **/
vector<int> findLeastConflict(vector<vector<int> > board) {
    /**
     * initially assume the least threatening position
     * has inf threat level. I guess technically an INT_MAX
     * by INT_MAX board could have more threat level but let's be
     * reasonable
     **/
    int minThreat = INT_MAX;
    int k = 0; // the position
    vector<int> potentialMoves; // a list of potential moves
    for (int i = 0; i < board.size(); i++) {
        for (int j = 0; j < board[0].size(); j++) {
            int temp = checkThreats(board, i, j); // find threat of a pos
            k = j + (i*board.size());
            if (board[i][j] == 0 // check if nonqueen first
            && minThreat > temp) { // and if threat is below prev. min
                minThreat = temp; // set new minimum
                potentialMoves.clear(); // erase the list
                potentialMoves.push_back(k); // add position to list
            } else if (board[i][j] == 0 && minThreat == temp) { // if eq
                potentialMoves.push_back(k); // then we have many, add it
            }
        }
    }
    return potentialMoves;
}

/**
 * function to find the naughtiest queens
 * 
 * just like the previous function, we want to find the queen or
 * queens who are the most threatening. Best way to solve the problem
 * is to move the most hostile queens to the least hostile areas
 * 
 * if there exist ties for the most hostile queen, keep them all
 * just like we did previously
 * 
 * @param board - the board to get queens from
 * @return - a set of queens that are most threatening
 **/
vector<int> findMostConflict(vector<vector<int> > board) {
    int maxThreat = 0; // initially assume no threat
    int k = 0; // the position
    vector<int> potentialQueens; // list of bad queens
    for (int i = 0; i < board.size(); i++) {
        for (int j = 0; j < board[0].size(); j++) {
            int temp = checkThreats(board, i, j);
            k = j + (i*board.size());
            if (board[i][j] == 1  // check if queen first
            && maxThreat < temp) { // and if more threatening
                maxThreat = temp; // set new maximum
                potentialQueens.clear(); // erase past queens
                potentialQueens.push_back(k); // add this one
            } else if (board[i][j] == 1 && maxThreat == temp) {
                potentialQueens.push_back(k); // add to existing list
            }
        }
    }
    return potentialQueens;
}

/**
 * the solution algorithm
 * 
 * we take a random queen from the previous list of bad queens
 * and a random safe spot to put them. Sometimes it would hang if it wasn't
 * random, but most of the time it runs faster doing it random. No clue why
 * 
 * basically this swaps queens around the board, looking for
 * a solution to the problem
 * 
 * @param board - the board to swap queens from
 * @return - T/F whether soln was found
 **/
bool newSolve(vector<vector<int> > &board) {
    // solutions exist where n = 1 and n > 3
    if (board.size() == 2 || board.size() == 3) {
        return false;
    }
    // this makes attempts until a soln is found
    while (boardThreats(board)) {
        attempts++; // each step inc. attempts
        // get list of worst queen(s) on the board
        vector<int> potentialQueens = findMostConflict(board);
        // take one at random
        int worstQueen = potentialQueens[getRand(potentialQueens.size())];
        // get list of best spot(s) to move said queen
        vector<int> potentialMoves = findLeastConflict(board);
        // pick one at random
        int goodMove = potentialMoves[getRand(potentialMoves.size())];
        // get to the position of the chosen queen
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board[0].size(); j++) {
                if (j + (i*board.size()) == worstQueen) { // if found
                    board[i][j] = 0; // remove her temporarily
                    break; // stop
                }
            }
        }
        // get to the position of the best spot to put her
        for (int k = 0; k < board.size(); k++) {
            for (int l = 0; l < board[0].size(); l++) {
                if (l + (k*board.size()) == goodMove) { // if found
                    board[k][l] = 1; // add her back here
                    break; // step
                }
            }
        }
    }
    // if broken out of the loop, ie. a soln found, return so
    return true;
}

/**
 * function to initially setup the board
 * 
 * @param n - the dimensions of the board
 * @return - a vector array of size nxn
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
 * function to return a random number
 * 
 * @param n - generate rand from 0..n
 * @return - the random number
 **/
int getRand(int n) {
    return (rand() % n);
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
