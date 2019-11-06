#include "PartA.h" // include function declarations, includes, etc

vector<int> disk;

/* main driver */
int main(int argc, char *argv[]) {
    if (argc == 2) {
        // init disk to vector returned from reading file
        disk = readFile(argv[1]);
        // print result from fcfs scheduling
        cout << fcfs(disk) << endl;
        // print result from ssf scheduling
        cout << sstf(disk) << endl;
    } else {
        cout << "Too few or too many arguments found.\n\n";
        cout << "To execute program, type \"./execute <filename>\"\n";
    }
    return 1;
}

/* first-come, first-serve scheduling algorithm
 * 
 * @param in - vector from file to perform algorithm on
 * @return string containing distance */
string fcfs(vector<int> &in) {
    stringstream outString; // for returning later
    int totalSeek = 0; // total distance
    int j = 11; // initial head position
    outString << "Running FCFS Scheduling\n";
    for (unsigned int i = 0; i < disk.size(); i++) { // for all disk elements
        totalSeek += abs(j - in[i]); // find distance
        j = in[i]; // new head position
        outString << "Seeking cylinder " << j << "\n";
    }
    outString << "FCFS Distance: " << totalSeek << "\n";
    return outString.str(); // return
}

/* shortest-seek first scheduling algorithm
 * 
 * @param in - vector from file to perform algorithm on
 * @return string containing distance */
string sstf(vector<int> &in) {
    stringstream outString;
    int totalSeek = 0; // total head distance
    int j = 11, k = 0; // initial head position, head pos. of shortest
    int shortest; // shortest distance
    outString << "Running SSTF Scheduling\n";
    while (containsPos(in)) {
        /* this is a dirty way of doing it, but I need a number
         * arbitrarily big and assured to be higher than any
         * possible distance. I summed the head positions as
         * this is guaranteed to be bigger */
        shortest = sumVector(in);
        for (unsigned int i = 0; i < disk.size(); i++) {
            // if unread head and distance is smaller than shortest
            if (in[i] != -1 && abs(in[i]-j) < shortest) {
                shortest = abs(in[i]-j); // basically find min distance
                k = i; // save smallest dist head pos
            }
        }
        totalSeek += abs(j - in[k]); // sum up distances
        j = in[k]; // set new head position
        /* I needed a way to set read cylinders. It's against convention
         * to make a class extending std::vector so I improvised. I figure
         * you cannot have a negative head position, and it's assumed
         * a head is only read once. The wording of the assignment __COULD__
         * have made this harder but I'm confident my work is congruent
         * with how the assignment is worded. 
         *
         * eg. if vector length is equal to number of unique cylinder 
         * heads and not requests, hoo boy my method would not work */
        in[k] = -1; // read head becomes -1
        outString << "Seeking cylinder " << j << "\n";
    }
    outString << "SSTF Distance: " << totalSeek << "\n";
    return outString.str(); // return it
}

/* finds sum of vector elements
 *
 * @param in - the vector to find sum of
 * @return the sum of vector elements */
int sumVector(vector<int> &in) {
    int sum = 0; // counter
    for(unsigned int i = 0; i < in.size(); i++) { // foreach loop
        sum += abs(in[i]); // add 'em up'
    }
    return sum; // return the sum
}

/* a check to see if unread cylinder heads exist
 *
 * @param in - the vector of cylinder heads
 * @return truth value on if unread heads exist */
bool containsPos(vector<int> &in) {
    for (unsigned int i = 0; i < in.size(); i++) {
        if (in[i] > 0) { // if not -1
            return true; // there's at least one`'
        }
    }
    return false; // otherwise none
}

/* function to read contents of .txt file
 * 
 * @param inFile - filename of input
 * @return a vector representing the disk */
vector<int> readFile(char *inFile) {
    ifstream diskFile(inFile); // open file
    int numElements; // size of vector to be
    string values, token; // for splitting
    diskFile >> numElements; // first number is size
    diskFile >> values; // csv for head positions
    stringstream iss(values); // for splitting
    vector<int> vect(numElements); // init vector
    for(int i = 0; getline(iss, token, ','); i++) {
        // dirty way of doing it, but populate vector with head pos.
        vect[i] = (atoi(token.c_str()));
    }
    diskFile.close(); // close the file
    return vect; // return the vector
}