#include <iostream>
#include <fstream>
#include <list>

class Graph {
	private:
		int numEdges;
		int numVertices;
		std::string options;
		std::list<int> *adjacency;
		void addEdge(int, int);
	public:
		Graph(std::string);
		void dfs(int);
		void dfsSearch(int, bool[]);
		void printInfo();
		int getNumVertices();
};
