#pragma once
#include "Fractal.hpp"
#include "ComplexNumber.hpp"

/**
 * Set class declaration
 */

class Set : public Fractal {
private:
	void findPixel(int, int, ComplexNumber);
	void findPoints();
	ComplexNumber iterate(ComplexNumber, ComplexNumber);
public:
	Set(int, int, char);
	void recalculateSet();
	void resetDefaults();
};
