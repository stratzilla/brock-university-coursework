

#include "PartB.h" // include function declarations, includes, etc

vector<int> eMatrix; // total resources
vector<int> aMatrix; // available resources
vector<vector<int> > cMatrix; // currently allocated
vector<vector<int> > rMatrix; // requests of memory

/* main driver */
int main(int argc, char *argv[]) {
    if (argc == 2) { // if enough arguments from terminal
        readFile(argv[1]); // read the .txt
        cout << checkSafe() << "\n"; // do the work
    } else { // otherwise
        cout << "Too few or too many arguments found.\n\n";
        cout << "To execute program, type \"./execute <filename>\"\n";
    }
    return 1;
}

/* function to find the order of processes executed according
 * to Banker's Algorithm. Also tells you if it's safe or not
 *
 * @return a string of info */
string checkSafe() {
    stringstream outString; // for returning later
    bool isSafe; // flag to see if process is safe
    unsigned int i = 0; // for iteration
    while (cMatrix.size() > 0){ // while unexecuted processes exist
        if (i == cMatrix.size()) { // if reached max num of attempts
            outString << "Unsafe state."; // unsafe, bro
            return outString.str(); // return it
        }
        isSafe = true; // assume true unless otherwise
        // for every resource
        for (unsigned int j = 1; j < aMatrix.size()+1; j++) {
            // if there is not enough resources
            if (rMatrix[i][j] > aMatrix[j-1]) {
                isSafe = false; // unsafe process
                break; // get outta here
            }
        }
        if (isSafe) { // if all checks out, do below
            outString << "Executing process " << cMatrix[i][0] << "\n";
            // increase available resources
            for (unsigned int j = 1; j < aMatrix.size()+1; j++) {
                aMatrix[j-1] += cMatrix[i][j]; // free up resources
            }
            /* for the longest time I was stuck on the below
             * I was removing process c but not its requirements
             * so process i might have requirements i-1 for example */
            rMatrix.erase(rMatrix.begin() + i); // remove process reqs
            cMatrix.erase(cMatrix.begin() + i); // remove process
            i = 0; // start from zero again
        } else { // otherwise keep going
            i++;
        }
    }
    // if you reach here, processes are safe
    outString << "Safe state.";
    return outString.str();
}

/* function to read contents of .txt file
 * 
 * @param inFile - filename of input */
void readFile(char *inFile) {
    ifstream stateFile(inFile); // open file
    int matrixHeight;
    int scratch;
    stateFile >> matrixHeight; // height of matrices
    stateFile >> scratch; // garbage, goes outta scope = adios
    // create eMatrix vector
    populate1D(eMatrix, stateFile);
    // create aMatrix vector
    populate1D(aMatrix, stateFile);
    // create cMatrix vector
    populate2D(cMatrix, matrixHeight, stateFile);
    // create rMatrix vector
    populate2D(rMatrix, matrixHeight, stateFile);
    // close the file
    stateFile.close();
}

/* function to populate the 1D matrices (E and A) with
 * values from the text file
 *
 * @param inVect - the vector to populate
 * @param inFile - the file to read from */
void populate1D(vector<int> &inVect, ifstream &inFile) {
    string values, token; // for splitting
    inFile >> values; // get csv
    stringstream iss(values); // for splitting
    for(int i = 0; getline(iss, token, ','); i++) {
        // dirty way of doing it, but populate vector with csv.
        inVect.push_back((atoi(token.c_str())));
    }
}

/* function to populate the 2D matrices (E and A) with
 * values from the text file
 *
 * @param inVect - the vector to populate
 * @param height - the height of the matrices
 * @param inFile - the file to read from */
void populate2D(vector<vector<int> > &inVect, int height, ifstream &inFile) {
    for (int j = 0; j < height; j++) {
        vector<int> innerVect; // inner portion of 2D vector
        string values, token; // splitting
        inFile >> values; // get csv
        stringstream iss(values); // splitting
        innerVect.push_back(j); // put process number in vector
        for(int i = 0; getline(iss, token, ','); i++) {
            // dirty way of doing it, but populate vector with csv.
            innerVect.push_back((atoi(token.c_str())));
        }
        inVect.push_back(innerVect); // add inner to outer
    }
}