#include "Graph.hpp"

/**
 * Graph class implementation
 */

/**
 * constructor for Graph class
 * @param fn - the filename for input .txt file
 */
Graph::Graph(std::string fn) {
	std::cout << "Using " << fn << std::endl;
	std::ifstream file(fn); // open file
	std::cout << "File loaded." << std::endl;
	file >> numVertices; // init number of vertices
	nodes.assign(numVertices, ""); // create a vector big enough to hold them
	indegree.assign(numVertices, 0); // likewise for indegrees
	adjacency = new std::list<int>[numVertices]; // create empty adjacency lists
	for (unsigned int i = 0; i < getNumVertices(); i++) { // for each vertex
		file >> nodes[i]; // insert name into vector
	}
	file >> numEdges; // init number of edges
	for (unsigned int i = 0; i < getNumEdges(); i++) { // for each edge
		int verA, verB; // from and to
		file >> verA >> verB; // init
		addEdge(verA, verB); // add the edge to adjacency lists
	}
	file.close(); // close the file
	std::cout << "Loaded graph.\n" << std::endl;
}

/**
 * method to add edges to adjacency lists
 * @param from - origin node
 * @param to - destination node
 */
void Graph::addEdge(int from, int to) {
	adjacency[from].push_back(to); // make the edge
	indegree[to]++; // add indegree to destination node
}

/**
 * method to search for topological sort
 * this uses Khan's approach
 */
void Graph::topologicalSort() {
	std::deque<int> setIndZero; // set of nodes with indegree zero
	for (unsigned int i = 0; i < indegree.size(); i++) {
		// find the set
		if (indegree[i] == 0) { setIndZero.push_front(i); }
	}
	while (!setIndZero.empty()) { // while set contains nodes
		int n = setIndZero.back(); setIndZero.pop_back(); // add n to solution
		sort.push(n);
		// for each node m connected by edge e to node n above
		for (std::list<int>::iterator j = adjacency[n].begin(); j != adjacency[n].end(); ++j) {
			// remove edge e
			indegree[*j]--;
			// if m has indegree zero, add to set
			if (indegree[*j] == 0) { setIndZero.push_back(*j); }
		}
	}
}

/**
 * method to print topological sort after it has been found
 */
void Graph::printTs() {
	if (!hasEdges()) { // acyclic will have no edges after TS operation
		std::cout << "Topological Sort found:" << std::endl;
		while (!sort.empty()) { // while queue contains elements
			std::cout << nodes.at(sort.front()) << " ";
			sort.pop();
		}
	} else { // else cyclic will still have edges remaining
		std::cout << "Cyclic dependencies; no topological sort possible.";
	}
	std::cout << "\n" << std::endl;
}

/**
 * method to print cursory information about graph
 */
void Graph::printInfo() {
	std::cout << "Vertices:" << std::endl;
	// print vertices
	for (unsigned int i = 0; i < getNumVertices(); i++) {
		std::cout << "[" << i << ":" << nodes.at(i) << "]";
		if (i < nodes.size()-1) { std::cout << ", "; }
	}
	std::cout << "\n\nEdges:" << std::endl;
	// print edges
	for (unsigned int i = 0; i < getNumVertices(); i++) {
		std::cout << nodes.at(i) << " -> ";
		for (std::list<int>::iterator j = adjacency[i].begin(); j != adjacency[i].end(); ++j) {
			std::cout << nodes.at(*j) << " ";
		}
		std::cout << "\n";
	}
	std::cout << "\n";
}

unsigned int Graph::getNumVertices() { return numVertices; } // getter for number vertices
unsigned int Graph::getNumEdges() { return numEdges; } // getter for number edges

/**
 * method to determine if edges remain in the graph
 */
bool Graph::hasEdges() {
	for (unsigned int i = 0; i < indegree.size(); i++) {
		// if any indegree is nonzero, edges remain
		if (indegree[i] != 0) { return true; }
	}
	return false;
}
