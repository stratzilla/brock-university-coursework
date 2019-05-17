#include <stdlib.h>
#include <GL/freeglut.h>
#include <stdio.h>
#include <malloc.h>
#include <time.h>
#include <math.h>
#include <iostream>
#include <vector>
#include <list>

using namespace std;

#define max(x,y) (((x) > (y)) ? (x) : (y))
#define min(x,y) (((x) < (y)) ? (x) : (y))

double speedMultiplier = 1;
int waitRain = 2;
int currentFrame = 0;
int rainSize = 1;
bool rainbow = false;
bool fullscreen = false;
int colorIndex = 0;

// function declaration
void makeFullscreen();

/**
 * Drop class
 * represents a rain drop
 */
class Drop {
	private:
		int speed;
		int color;
		int posY;
		int posX;
		int size;
	public:
		Drop() { // constructor
			speed = 5;
			color = colorIndex;
			posY = 15;
			posX = (rand() % 500);
			size = rainSize;
		}
		void move() { // movement logic
			speed += speedMultiplier;
			posY += speed;
			if (posY > 499) {
				speed = 0;
				posY = 500;
			}
		}
		// getter functions
		int getColor() {
			return color;
		}
		int getPosY() {
			return posY;
		}
		int getPosX() {
			return posX;
		}
};

list<Drop> listDrops; // list of Drops

// a set of different colors for the rain
int rgbColors[13][3] = { { 255, 255, 255 },
{ 255, 0, 0 }, { 255, 128, 0 }, { 255, 255, 0 },
{ 128, 255, 0 }, { 0, 255, 0 }, { 0, 255, 128 },
{ 0, 255, 255 }, { 0, 128, 255 }, { 0, 0, 255 },
{ 128, 0, 255 }, { 255, 0, 255 }, { 255, 0, 128 }
};

/**
 * Drop Generation function
 * generates a new rain drop and appends to list
 */
void placeDrop() {
	Drop d = Drop();
	listDrops.push_back(d);
}

/**
 * Refresher function
 * function that redraws the frame
 * and performs rain logic
 */
void refreshDisplay() {
	if (rainbow) { // if rainbow toggle
		colorIndex++; // next color
		colorIndex = (colorIndex > 12) ? 0 : colorIndex; // loop back
	}
	/**
	 * this determines how frequently a new drop is added
	 * it works from 0..n frames before a new drop is added
	 * n can be changed via CLI (rain density)
	 */
	if (currentFrame == 0) {
		placeDrop(); // add new drop
	}
	glClear(GL_COLOR_BUFFER_BIT);
	glPointSize(rainSize);
	glBegin(GL_POINTS);
	list<Drop>::iterator a;
	for (a = listDrops.begin(); a != listDrops.end(); ++a) {
		// get color of drop
		int red = rgbColors[a->getColor()][0];
		int green = rgbColors[a->getColor()][1];
		int blue = rgbColors[a->getColor()][2];
		// set color
		glColor3f(red, green, blue);
		// place at right spot
		glVertex2i(a->getPosX(),a->getPosY());
		a->move();
	}
	glEnd();
	glFlush();
	glutSwapBuffers();
	// increment this
	currentFrame = (currentFrame == waitRain) ? 0 : currentFrame + 1;
}

/**
 * Reset function
 * returns program to initial conditions
 */
void resetDefaults() {
	listDrops.clear(); // empty the list of raindrops
	rainbow = false; // turn rainbow coloring off
	speedMultiplier = 1; // set to original speed
	rainSize = 1; // set to original size
	waitRain = 2; // set to original rain density
	if (fullscreen) { // if fullscreen
		makeFullscreen(); // go to windowed
	}
}

/**
 * Fullscreen Toggle function
 * sets to fullscreen or windowed
 */
void makeFullscreen() {
	if (fullscreen) { // if already fullscreen
		fullscreen = false;
		glutReshapeWindow(500, 500); // reset to 500x500 window
	} else { // otherwise...
		fullscreen = true;
		glutFullScreen(); // make it fullscreen
	}
}

/**
 * Repeater function
 * sets how fast the display is repeated/redrawn
 */
void repeater(int val) {
	glutPostRedisplay();
	glutTimerFunc(5, repeater, 0);
}

/**
 * CLI Logic function
 * function which performs algorithms as outlined in CLI menu
 */
void menu(unsigned char key, int x, int y) {
	switch (key) {
		case '1': { speedMultiplier = min(10, speedMultiplier+1); break; }
		case '2': { speedMultiplier = max(1, speedMultiplier-1); break; }
		case '3': { rainSize = min(10, rainSize+1); break; }
		case '4': { rainSize = max(1, rainSize-1); break; }
		case '5': { waitRain = max(1, waitRain-1); break; }
		case '6': { waitRain = min(10, waitRain+1); break; }
		case '7': { colorIndex++; colorIndex = (colorIndex > 12) ? 0 : colorIndex; break; }
		case '8': { rainbow = !rainbow;	break; }
		case 'r': { resetDefaults(); break; }
		case 'f': { makeFullscreen(); break; }
		case 'q': { exit(0); break; }
	}
}

/**
 * CLI Menu function
 * function to display the operations to the user
 */
void displayMenu() {
	cout << "Raindrop Screensaver" << endl;
	cout << "\nq: Quit\t\tr: Reset\tf: Fullscreen (toggle)" << endl;
	cout << "1: Increase rain speed\t2: Decrease rain speed" << endl;
	cout << "3: Increase rain size\t4: Decrease rain size" << endl;
	cout << "5: Increase rain density\t6: Decrease rain density" << endl;
	cout << "7: Change Color\t8: Rainbow Colors" << endl;
}

int main(int argc, char** argv) {
	srand(time(NULL));
	displayMenu();
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_RGB | GLUT_DOUBLE);
	glutInitWindowSize(500, 500);
	glutCreateWindow("3P98 Assign1");
	glutKeyboardFunc(menu);
	gluOrtho2D(0.0, 500.0, 500.0, 0.0);
	glutDisplayFunc(refreshDisplay);
	glutTimerFunc(0, repeater, 0);
	glutMainLoop();
	return 0;
}
