#include <iostream>
#include <sstream>

/**
 * Set class definition
 */

class Set {
private:
	bool elements[256];
	Set(const bool elements[256]);
public:
	Set();
	Set operator+(const Set &other) const; // union of two sets
	Set operator+(const int &other) const; // add element to set
	Set operator-(const Set &other) const; // difference of two sets
	Set operator-(const int &other) const; // remove element from set
	Set operator^(const Set &other) const; // intersection of two sets
	Set operator^(const int &other) const; // intersection of set and element
	Set operator~() const; // complement of two sets
	Set operator+() const; // set of "universe"
	Set operator-() const; // empty set
	bool operator<=(const Set &other) const; // subset
	bool operator<(const Set &other) const; // strict subset
	bool operator>=(const Set &other) const; // superset
	bool operator>(const Set &other) const; // strict superset
	bool operator==(const Set &other) const; // set equality
	bool operator!=(const Set &other) const; // set inequality
	bool operator!() const; // determine if empty set
	unsigned int operator()() const; // cardinality of set
	bool operator[](const int &pos) const; // is element in set
	friend std::ostream& operator<<(std::ostream &out, const Set &set);
	friend std::istream& operator>>(std::istream &in, Set &set);
};
