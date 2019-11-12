#include "Graph.h"

int main(int argc, char* argv[]) {
	if (argc == 2) {
		Graph g(argv[1]);
		g.printInfo();
		int in = -2;
		while (in != -1) {
			if (in != -2) { g.dfs(in); }
			std::cout << "Starting vertex number for DFT: "; std::cin >> in;
			if (in < -1 || in >= g.getNumVertices()) {
				std::cout << "\nThat is an invalid vertex.\n" << std::endl;
				in = -2;
			}
		}
	}
	return 0;
}
