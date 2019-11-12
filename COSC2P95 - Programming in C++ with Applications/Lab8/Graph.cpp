#include "Graph.hpp"

Graph::Graph(std::string fn) {
	std::cout << "Using " << fn << std::endl;
	std::ifstream file(fn);
	std::cout << "File loaded." << std::endl;
	file >> numVertices;
	adjacency = new std::list<int>[numVertices];
	for (int i = 0; i < numVertices; i++) {
		char node;
		file >> node;
		options += node;
	}
	file >> numEdges;
	for (int i = 0; i < numEdges; i++) {
		int verA, verB;
		file >> verA; file >> verB;
		addEdge(verA, verB);
	}
	file.close();
	std::cout << "Loaded graph.\n" << std::endl;
}

void Graph::addEdge(int from, int to) {
	adjacency[from].push_back(to);
}

void Graph::dfs(int v) {
	bool *visited = new bool[numVertices];
	for (int i = 0; i < numVertices; i++) {
		visited[i] = false;
	}
	dfsSearch(v, visited);
	std::cout << "\n" << std::endl;
}

void Graph::dfsSearch(int v, bool vi[]) {
	vi[v] = true;
	std::cout << options.at(v) << " ";
	for (std::list<int>::iterator i = adjacency[v].begin(); i != adjacency[v].end(); ++i) {
		if (!vi[*i]) { dfsSearch(*i, vi); }
	}
}

int Graph::getNumVertices() { return numVertices; }

void Graph::printInfo() {
	std::cout << "Vertices:" << std::endl;
	for (int i = 0; i < numVertices; i++) {
		std::cout << "[" << i << ":" << options.at(i) << "]";
		if (i < options.length()-1) { std::cout << ", "; }
	}
	std::cout << "\n\nEdges:" << std::endl;
	for (int i = 0; i < numVertices; i++) {
		std::cout << options.at(i) << " -> ";
		for (std::list<int>::iterator j = adjacency[i].begin(); j != adjacency[i].end(); ++j) {
			std::cout << options.at(*j) << " ";
		}
		std::cout << "\n";
	}
	std::cout << "\n";
}
