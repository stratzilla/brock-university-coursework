#include <iostream>
#include <fstream>
#include <list>
#include <vector>
#include <queue>

/**
 * Graph class definition
 */

class Graph {
	private:
		unsigned int numEdges; // number of edges
		unsigned int numVertices; // number of vertices
		std::vector<std::string> nodes; // node names
		std::list<int> *adjacency; // adjacency lists
		std::vector<int> indegree; // indegree of each node
		std::queue<int> sort; // the topological sort
		unsigned int getNumVertices();
		unsigned int getNumEdges();
		void addEdge(int, int);
		bool hasEdges();
	public:
		Graph(std::string);
		void topologicalSort();
		void printTs();
		void printInfo();
};
