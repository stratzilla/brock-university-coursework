#include <iostream>

class Record {
	private:
		long rid;
		char *content;
		static long ridCount;
		static long nextRIDCount();
		Record& operator=(const Record &other) {}
	public:
		Record(char[]);
		Record(const Record &original);
		~Record();
		long getRID();
		char *getContent();
		friend std::ostream& operator<<(std::ostream &out, Record &rec);
};
