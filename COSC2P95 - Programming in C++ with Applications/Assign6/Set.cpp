#include "Set.h"

/**
 * Set class implementation
 */

Set::Set() {
	for (int i = 0; i < 256; i++) { elements[i] = false; }
}

Set::Set(const bool elements[256]) {
	for (int i = 0; i < 256; i++) { this->elements[i] = elements[i]; }
}

/**
 * finds union of A (this) and B
 * @param other - set B
 * @return - A U B
 */
Set Set::operator+(const Set &other) const {
	Set s(elements);
	for (int i = 0; i < 256; i++) {
		s.elements[i] = (other.elements[i]) ? true : s.elements[i];
	}
	return s;
}

/**
 * finds union of A (this) and b
 * @param other - element b
 * @return - A U b
 */
Set Set::operator+(const int &other) const {
	Set s(elements);
	s.elements[other] = true;
	return s;
}

/**
 * finds difference of A (this) and B
 * @param other - set B
 * @return - A \ B
 */
Set Set::operator-(const Set &other) const {
	Set s(elements);
	for (int i = 0; i < 256; i++) {
		s.elements[i] = (other.elements[i]) ? false : s.elements[i];
	}
	return s;
}

/**
 * finds difference of A (this) and b
 * @param other - element B
 * @return - A \ b
 */
Set Set::operator-(const int &other) const {
	Set s(elements);
	s.elements[other] = false;
	return s;
}

/**
 * finds intersect of A (this) and B
 * @param other - set B
 * @return - A n B
 */
Set Set::operator^(const Set &other) const {
	Set s(elements);
	for (int i = 0; i < 256; i++) {
		s.elements[i] = (other.elements[i] && elements[i]) ? true : false;
	}
	return s;
}

/**
 * finds intersect of A (this) and b
 * @param other - element b
 * @return - A n b
 */
Set Set::operator^(const int &other) const {
	Set s;
	s.elements[other] = true;
	return (*this)^s;
}

/**
 * finds complement of A (this)
 * @return - U (universe) \ A
 */
Set Set::operator~() const {
	return +(*this)-(*this);
}

/**
 * finds universe set
 * @return - U (universe)
 */
Set Set::operator+() const {
	Set s;
	for (int i = 0; i < 256; i++) { s.elements[i] = true; }
	return s;
}

/**
 * finds empty set
 * @return - {} (empty set)
 */
Set Set::operator-() const {
	Set s;
	return s;
}

/**
 * determines if A (this) is a subset of B
 * @param other - set B
 * @return - is A subset of B
 */
bool Set::operator<=(const Set &other) const {
	for (int i = 0; i < 256; i++) {
		if (elements[i] && !other.elements[i]) { return false; }
	}
	return true;
}

/**
 * determines if A (this) is a strict subset of B
 * @param other - set B
 * @return - is A strict subset of B
 */
bool Set::operator<(const Set &other) const {
	return (*this) <= other && (*this) != other;
}

/**
 * determines if A (this) is a superset of B
 * @param other - set B
 * @return - is A superset of B
 */
bool Set::operator>=(const Set &other) const {
	for (int i = 0; i < 256; i++) {
		if (other.elements[i] && !elements[i]) { return false; }
	}
	return true;
}

/**
 * determines if A (this) is a strict superset of B
 * @param other - set B
 * @return - is A strict superset of B
 */
bool Set::operator>(const Set &other) const {
	return (*this) >= other && (*this) != other;
}

/**
 * determines if A (this) and B are equal
 * @param other - set B
 * @return - is A equal to B
 */
bool Set::operator==(const Set &other) const {
	for (int i = 0; i < 256; i++) {
		if (elements[i] != other.elements[i]) { return false; }
	}
	return true;
}

/**
 * determines if A (this) and B are inequal
 * @param other - set B
 * @return - is A not equal to B
 */
bool Set::operator!=(const Set &other) const {
	return !((*this) == other);
}

/**
 * determines if set is empty
 * @return - is (this) empty
 */
bool Set::operator!() const {
	for (int i = 0; i < 256; i++) {
		if (elements[i]) { return false; }
	}
	return true;
}

/**
 * determines size of set
 * @return - size of (this)
 */
unsigned int Set::operator()() const {
	unsigned int k = 0;
	for (int i = 0; i < 256; i++) {
		if (elements[i]) { k++; }
	}
	return k;
}

/**
 * determines if b is in A (this)
 * @param pos - element b
 * @return - is b in A
 */
bool Set::operator[](const int &pos) const {
	return elements[pos];
}

// Stream Insertion
std::ostream& operator<<(std::ostream &out, const Set &set) {
	std::stringstream toReturn; bool flag = false;
	toReturn << "{";
	for (int i = 0; i < sizeof(set.elements)/sizeof(set.elements[0]); i++) {
		if (set.elements[i]) { 
			if (flag) { 
				toReturn << ", "; 
			} else { 
				flag = true;
			}
			toReturn << i;
		}
	}
	toReturn << "}";
	out << toReturn.str().c_str();
	return out;
}

// Stream extraction
std::istream& operator>>(std::istream &in, Set &set) {
	bool arr[256];
	char open;
	in >> open;
	if (in.fail() || open != '{') {
		in.setstate(std::ios::failbit);
		return in;
	}
	for (int i = 0; i < 256; i++) {
		arr[i] = false;
	}
	std::string buff;
	std::getline(in, buff, '}');
	std::stringstream ss(buff);
	std::string field;
	while (true) {
		std::getline(ss, field, ',');
		if (ss.fail()) { break; }
		int el;
		std::stringstream se(field);
		se >> el;
		if (el >= 0 && el < 256) { arr[el] = true; }
	}
	set = Set(arr);
	return in;
}
