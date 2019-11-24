#include "Graph.hpp"

/**
 * main driver/entry point
 */

int main(int argc, char* argv[]) {
	if (argc == 2) { // if enough args
		Graph g(argv[1]); // load data into graph
		g.printInfo(); // print cursory info about graph
		g.topologicalSort(); // find TS
		g.printTs(); // print TS
	}
	return 0;
}
