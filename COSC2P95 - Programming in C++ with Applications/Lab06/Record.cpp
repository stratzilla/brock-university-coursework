#include "Record.hpp"

Record::Record(char content[]) {
	int clength = 0;
	this->rid = nextRIDCount();
	while (content[clength] != '\0') { clength++; }
	clength++;
	this->content = new char[clength];
	for (int i = 0; i < clength; i++) { this->content[i] = content[i]; }
}

Record::Record(const Record &original):rid(original.rid) {
	int clength = 0;
	while (original.content[clength] != '\0') { clength++; }
	clength++;
	this->content = new char[clength];
	for (int i = 0; i < clength; i++) { this->content[i] = original.content[i]; }
}

Record::~Record() { delete[] content; }

long Record::nextRIDCount() { return ridCount++; }
long Record::ridCount = 0;

long Record::getRID() { return rid; }
char* Record::getContent() { return content; }

std::ostream& operator<<(std::ostream &out, Record &rec) {
	std::cout << '[' << rec.rid << ':' << rec.content << ']';
}
