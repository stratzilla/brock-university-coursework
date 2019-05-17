#include <stdlib.h>
#include <GL/freeglut.h>
#include <stdio.h>
#include <time.h>
#include <math.h>
#include <iostream>
#include <list>

using namespace std;

/**
 * Point class
 * represents a pixel on the display
 */
class Point {
private:
	int x, y; // position
	bool inLine; // if it's been hulled already
public:
	// constructor
	Point(int a, int b) {
		x = a; y = b;
		inLine = false;
	}
	// getter methods for position
	int getX() {
		return x;
	}
	int getY() {
		return y;
	}
	// getter for whether point is part of line or not
	bool getLine() {
		return inLine;
	}
	// toggles flag for line status
	void setLine() {
		inLine = true;
	}
};

/**
 * Line class
 * represents a connection between two points
 */
class Line {
private:
	int x1, y1, x2, y2; // position
public:
	// constructor
	Line(int a, int b, int c, int d) {
		x1 = a; y1 = b;
		x2 = c; y2 = d;
	}
	// getter methods for start points
	int getX1() {
		return x1;
	}
	int getY1() {
		return y1;
	}
	// getter methods for end points
	int getX2() {
		return x2;
	}
	int getY2() {
		return y2;
	}
};

/**
 * Triangle class
 * represents a connection between three points
 */
class Triangle {
private:
	int x1, y1, x2, y2, x3, y3; // positions
public:
	// constructor
	Triangle(int a, int b, int c, int d, int e, int f) {
		x1 = a; y1 = b;
		x2 = c; y2 = d;
		x3 = e; y3 = f;
	}
	// getter method for each position
	int getX1() {
		return x1;
	}
	int getY1() {
		return y1;
	}
	int getX2() {
		return x2;
	}
	int getY2() {
		return y2;
	}
	int getX3() {
		return x3;
	}
	int getY3() {
		return y3;
	}
};

/**
 * I use non-fixed structures as per assignment guidelines
 * this way you can have any number of points, lines, triangles
 */
list<Line> listLines; // list of lines used for hull/peel
list<Point> listPoints; // list of points likewise
list<Triangle> listTriangles; // list of triangles likewise
bool toggleMouse = false; // used for mouse control
int height, width; // dimensions of screen

/**
 * Function to check if a point is unique
 * @param i - the point to compare to
 * @return - whether that point is unique
 */
bool checkUnique(int x, int y) {
	/**
	 * it is exceptionally frustrating the lab
	 * computers don't support modern foreach loops :(
	 */
	for (list<Point>::iterator p = listPoints.begin(); p != listPoints.end(); ++p) { // for each point
		// check if position is identical to any existing points
		if (x == p->getX() && y == p->getY()) {
			return false; // if same, non-unique
		}
	}
	return true; // if unique
}

/**
 * Function to generate a random position
 * @return - a random position
 */
Point getRandomPosition() {
	while (true) {
		/**
		 * I've made a 5% deadzone where points cannot appear
		 * because I found points at the extremity of the window are difficult
		 * to see. It can be removed without any detriment
		 * to operation, it's just an aesthetic choice. Mouse input
		 * does not have this deadzone
		 */
		int x = (int)(rand() % (int)((0.95*width) - (0.05*width)) + 0.05*width);
		int y = (int)(rand() % (int)((0.95*height) - (0.05*height)) + 0.05*height);
		/**
		 * as per assignment guidelines, we don't want point
		 * overlap, so ensure it is unique beforehand
		 */
		if (checkUnique(x, y)) {
			return Point(x, y);
		}
	}
}

/**
 * Function which adds points to the screen
 * appends points to a list which is then displayed
 * @param i - how many points to add
 */
void putPoints(int i) {
	for (int j = 0; j < i; j++) {
		listPoints.push_back(getRandomPosition()); // append to list
	}
}

/**
 * Function to remove points on lines
 * I've combined these two for readability
 * just removes hulled points for further peel operation
 */
bool linePredicate(Point &p) { return p.getLine(); }
void removePoints() {
	/**
	 * removes elements based on a conditional predicate.
	 * easier than iterating through manually, etc
	 */
	listPoints.remove_if(linePredicate);
}

/**
 * Function to flip truth value of points
 * once a point is part of a line, it needs to be flagged
 * as such so it can be removed with successive peels.
 * @params x, y - the position of the point to trip
 */
void pointToLine(int x, int y) {
	for (list<Point>::iterator p = listPoints.begin(); p != listPoints.end(); ++p) {
		if (p->getX() == x && p->getY() == y) {
			p->setLine(); // flip it
		}
	}
}

/**
 * Function to find the distance between a line and a point
 * Uses euclidean geometry. Points p, q make a line. Point a distance to pq found
 * @params p1, p2 - the first point of line
 * @params q1, q2 - the second point of line
 * @params a1, a2 - the point
 * @return - the distance between pq and a
 */
double distance(int x1, int y1, int x2, int y2, int a1, int a2) {
	/**
	 * this is taken from Wikipedia: finding the distance between a point
	 * and a line. I've split it up into pieces for easier readability 
	 */
	double num = (((y2 - y1)*a1) - ((x2 - x1)*a2) + (x2*y1) - (y2*x1));
	// technically rectilinear metrics could be used in lieu of euclidean
	double den = sqrt(pow((double)(y2 - y1), 2) + (pow((double)(x2 - x1), 2)));
	return num / den;
}

/**
 * Function to perform Quickhull operation
 * @params x1, y1 - first point
 * @params x2, y2 - the second point
 */
void quickHull(int x1, int y1, int x2, int y2) {
	int newX, newY; // for later
	double tempDist = 0.00; // temporary distance of 0
	for (list<Point>::iterator p = listPoints.begin(); p != listPoints.end(); ++p) {
		// find euclidean distance
		double dist = distance(x1, y1, x2, y2, p->getX(), p->getY());
		if (dist > tempDist && dist != 0.00) { // if distance is larger
			tempDist = dist;
			newX = p->getX(); newY = p->getY();
		}
	}
	if (tempDist == 0.00) { // once you can't get better
		listLines.push_back(Line(x1, y1, x2, y2)); // append to list
		pointToLine(x1, y1); // mark those points
		pointToLine(x2, y2); // as part of a line
	}
	else { // keep going with recursive calls
		quickHull(x1, y1, newX, newY);
		quickHull(newX, newY, x2, y2);
	}
}

/**
 * Function to perform convex peel on points
 */
void peel() {
	if (listPoints.size() >= 3) { // if at least 3 points left
		/**
		 * use some temp values. Any minimal and maximal points
		 * will assuredly be positioned in a way to override these values.
		 * since you couldn't have a point at (-1, height+1) for example
		 */
		int maxX = -1; int maxY = -1;
		int minX = width + 1; int minY = height + 1;
		for (list<Point>::iterator p = listPoints.begin(); p != listPoints.end(); ++p) {
			// populate values with new ones if better
			if (p->getX() < minX) {
				minX = p->getX(); minY = p->getY();
			}
			// we want to find maximal and minimal x values
			else if (p->getX() > maxX) {
				maxX = p->getX(); maxY = p->getY();
			}
		}
		// perform the hull operation
		quickHull(minX, minY, maxX, maxY);
		quickHull(maxX, maxY, minX, minY);
		removePoints(); // remove for successive peels
	}
}

/**
 * Function to triangulate points
 * performs a peel and then selects a random inner
 * point. Joins edges of hull to that point by way of triangles
 */
void triangulate() {
	peel(); // need to peel it >0 times first
	int c = rand() % listPoints.size(); // countdown for random point
	int x = 0, y = 0; // temp
	for (list<Point>::iterator p = listPoints.begin(); p != listPoints.end(); ++p) {
		if (c == 0) { // dirty way of finding random list element
			x = p->getX();
			y = p->getY();
			break; 
		}
		c--;
	}
	// for each point on each line, join to random point making a triangle
	for (list<Line>::iterator l = listLines.begin(); l != listLines.end(); ++l) {
		listTriangles.push_back(Triangle(l->getX1(), l->getY1(), l->getX2(), l->getY2(), x, y));
	}
}

/**
 * Refresher function
 * function that redraws the frame
 * with user points/lines/etc
 */
void refreshDisplay() {
	glClear(GL_COLOR_BUFFER_BIT);
	glPointSize(3.0);
	glColor3f(1.0, 0.0, 1.0); // points are purple
	glBegin(GL_POINTS); // begin drawing points
	for (list<Point>::iterator p = listPoints.begin(); p != listPoints.end(); ++p) { // for each point
		glVertex2i(p->getX(), p->getY()); // draw pixel at position
	}
	glEnd(); // finish
	glColor3f(0.5, 0.55, 0.6); // lines are silver
	glBegin(GL_LINES); // begin drawing lines
	for (list<Line>::iterator l = listLines.begin(); l != listLines.end(); ++l) { // for each line
		glVertex2i(l->getX1(), l->getY1()); // start point of line
		glVertex2i(l->getX2(), l->getY2()); // end point of line
	}
	glEnd(); // finish
	glColor3f(0.65, 0.45, 0.2); // triangles are orange
	glBegin(GL_LINES); // begin drawing triangles
	for (list<Triangle>::iterator t = listTriangles.begin(); t != listTriangles.end(); ++t) { // for each triangle
		glVertex2i(t->getX1(), t->getY1()); // vertex one
		glVertex2i(t->getX2(), t->getY2()); // joins vertex two
		glVertex2i(t->getX2(), t->getY2()); // vertex two
		glVertex2i(t->getX3(), t->getY3()); // joins vertex three
		glVertex2i(t->getX3(), t->getY3()); // vertex three
		glVertex2i(t->getX1(), t->getY1()); // joins vertex one
	}
	glEnd(); // finish
	glFlush();
}

/**
 * Reset function
 * returns program to initial conditions
 */
void resetDefaults() {
	listPoints.clear();
	listLines.clear();
	listTriangles.clear();
}

/**
 * Repeater function
 * sets how fast the display is repeated/redrawn
 * this is reused from the first assignment and is unnecessary
 * to redraw the scene as if it was constantly updating
 * but for consistency I kept it in.
 */
void repeater(int val) {
	glutPostRedisplay();
	glutTimerFunc(5, repeater, 0);
}

/**
 * Function for user defined number of points
 */
int userInput() {
	int input;
	cout << endl;
	cout << "How many points would you like?" << endl;
	cin >> input;
	return input;
}

/**
 * Function for mouse draw
 * @param button - which mouse button clicked
 * @param state - whether up/down
 * @params x, y - mouse position
 */
void mouseInput(int button, int state, int x, int y) {
	int maxY = glutGet(GLUT_WINDOW_HEIGHT);
	y = maxY - y;
	if ((button == GLUT_LEFT_BUTTON) && (state == GLUT_DOWN) && toggleMouse) {
		listPoints.push_back(Point(x,y)); // append to list
	}
	glFlush();
}

/**
 * CLI Logic function
 * function which performs algorithms as outlined in CLI menu
 */
void menu(unsigned char key, int x, int y) {
	switch (key) {
		case 'r': resetDefaults(); break;
		case '1': putPoints(10); break;
		case '2': putPoints(25); break;
		case '3': putPoints(50); break;
		case '4': putPoints(100); break;
		case 'n': putPoints(userInput()); break;
		case 'm': toggleMouse = !toggleMouse; break;
		case 'p': peel(); break;
		case 't': triangulate(); break;
	}
}

/**
 * CLI Menu function
 * function to display the operations to the user
 */
void displayMenu() {
	cout << "Convex Hull Peeling / Delauney Triangulation" << endl;
	cout << "\nAdd Points" << endl;
	cout << "1. 10 points.\t 2. 25 points." << endl;
	cout << "3. 50 points.\t 4. 100 points." << endl;
	cout << "n. User Defined" << endl;
	cout << "\nOptions" << endl;
	cout << "r. Reset.\t p. Peel" << endl;
	cout << "m. Toggle Mouse\t t. Triangulate" << endl;
}

int main(int argc, char** argv) {
	displayMenu();
	srand(time(NULL));
	height = 500;
	width = 500;
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_RGB | GLUT_SINGLE);
	glutInitWindowSize(height, width);
	glutCreateWindow("3P98 Assignment 2 - 2019");
	glutKeyboardFunc(menu);
	glMatrixMode(GL_PROJECTION);
	gluOrtho2D(0.0, height, 0.0, width);
	glutDisplayFunc(refreshDisplay);
	glutMouseFunc(mouseInput);
	glutTimerFunc(0, repeater, 0);
	glutMainLoop();
	return 0;
}
