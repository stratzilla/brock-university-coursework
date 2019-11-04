#include <stdlib.h>
#include <freeglut.h>
#include <FreeImage.h>
#include <stdio.h>
#include <malloc.h>
#include <time.h>
#include <math.h>

//surprised these aren't in math.h prebaked
#define max(x,y) (((x) > (y)) ? (x) : (y))
#define min(x,y) (((x) < (y)) ? (x) : (y))

//function declarations
void reset();
void walls();
void rainbow();
void velUp();
void velDown();
void refreshUp();
void refreshDown();
void sizeUp();
void sizeDown();
void makeFullscreen();

float pos[2]; //position of ball
float speed[2] = { 0.02, 0.01 }; //initial speed
int height = 500; //window size
int width = 500; //window size
float circleRadius = 0.05; //radius of ball
int refreshRate = 25; //not really frames per second, but frequency of window update
bool modWalls = true; //wrap around enabled/disabled
bool modRainbow = false; //colored ball enabled/disabled
bool fullscreen = false; //initially windowed
int it = 0; //for selecting colors
//a set of 12 colors and default white
int rgbSpec[13][3] = { { 255, 255, 255 },
{ 255, 0, 0 }, { 255, 128, 0 }, { 255, 255, 0 },
{ 128, 255, 0 }, { 0, 255, 0 }, { 0, 255, 128 },
{ 0, 255, 255 }, { 0, 128, 255 }, { 0, 0, 255 },
{ 128, 0, 255 }, { 255, 0, 255 }, { 255, 0, 128 }
};

/* menu with keyboard controls */
void menu(unsigned char key, int x, int y) {
	switch (key) {
	case '1': { //increase ball speed
				  velUp();
				  break;
	}
	case '2': { //decrease ball speed
				  velDown();
				  break;
	}
	case '3': { //increase refresh rate
				  refreshUp();
				  break;
	}
	case '4': { //decrease refresh rate
				  refreshDown();
				  break;
	}
	case '5': { //increase ball size
				  sizeUp();
				  break;
	}
	case '6': { //decrease ball size
				  sizeDown();
				  break;
	}
	case '7': { //toggle wrap-around
				  walls();
				  break;
	}
	case '8': { //shuffle between colors
				  rainbow();
				  break;
	}
	case 'r': { //resets position and parameters
				  reset();
				  break;
	}
	case 'f': { //make fullscreen
				  makeFullscreen();
				  break;
	}
	case 'q': { //quit program
				  exit(0);
				  break;
	}
	}
}

//puts the ball on a canvas and displays every refreshrate (ms)
void display_image(void) {
	glClear(GL_COLOR_BUFFER_BIT); //clear color buffer
	glBegin(GL_TRIANGLE_FAN); //I made a circle using triangles
	glVertex2f(pos[0], pos[1]); //put it down at x,y position
	if (modRainbow == true) { //if user toggled rainbows on
		it = (it == 12) ? 1 : it + 1; //excludes white (index 0), cycles through them
	}
	glColor3f(rgbSpec[it][0], rgbSpec[it][1], rgbSpec[it][2]); //set ball color
	//create i many triangles to form a circle shape
	for (float i = 0; i <= 2 * 3.14592 + 0.1; i = i + 0.1) {
		glVertex2f(pos[0] + sin(i) * circleRadius, pos[1] + cos(i) * circleRadius);
	}
	glEnd();
	/* swaps between front and back buffers
	* I'm actually unsure the specifics of what this does,
	* but I tried both single and double buffering and
	* colors, etc look better on double buffer */
	glutSwapBuffers();
	//increase the position by the speed, so new pos is pos+speed
	pos[0] += speed[0];
	pos[1] += speed[1];
	if (modWalls == true) { //if nowrap disabled
		if (pos[0] + circleRadius > 1.0) { //edge detection
			pos[0] = 1.0 - circleRadius; //stop it at edge to revert speed
			speed[0] = -speed[0]; //neg(speed) to bounce it back
		}
		else if (pos[0] - circleRadius < -1.0) {
			pos[0] = -1.0 + circleRadius;
			speed[0] = -speed[0];
		}
		if (pos[1] + circleRadius > 1.0) {
			pos[1] = 1.0 - circleRadius;
			speed[1] = -speed[1];
		}
		else if (pos[1] - circleRadius < -1.0) {
			pos[1] = -1.0 + circleRadius;
			speed[1] = -speed[1];
		}
	}
	else if (modWalls == false) { //if nowrap enabled
		if (pos[0] > 1.0) { //edge detection
			pos[0] = -pos[0]; //bring it to the other side
		}
		else if (pos[0] < -1.0) {
			pos[0] = -pos[0];
		}
		if (pos[1] > 1.0) {
			pos[1] = -pos[1];
		}
		else if (pos[1] < -1.0) {
			pos[1] = -pos[1];
		}
	}
}

//resets to default conditions
void reset() {
	it = 0; //reset color index
	refreshRate = 25; //reset refreshrate to default
	circleRadius = 0.05; //reset radius to default
	modWalls = true; //toggle nowrap off
	modRainbow = false; //toggle color cycling off
	srand(time(NULL));
	pos[0] = ((rand() % 200) - 100)*0.01; //new x-coord
	pos[1] = ((rand() % 200) - 100)*0.01; //new y-coord
	speed[0] = 0.02; //reset hor- and vert- speeds
	speed[1] = 0.01;
	speed[0] = (rand() % 1 == 0) ? -speed[0] : speed[0]; //randomize their directions
	speed[1] = (rand() % 1 == 0) ? -speed[1] : speed[1];
}

//toggles nowrap on/off
void walls() {
	modWalls = !modWalls;
}

//increases speed
void velUp() {
	speed[0] *= 2.00f;
	speed[1] *= 2.00f;
}

//decreases speed
void velDown() {
	speed[0] /= 2;
	speed[1] /= 2;
}

//increasing refresh rate to a maximum of 5ms
void refreshUp() {
	refreshRate = max(5, refreshRate - 5);
}

//decreases refresh rate to a minimum of 100ms
void refreshDown() {
	refreshRate = min(100, refreshRate + 5);
}

//increases ball size to a maximum of 75% of screen
void sizeUp() {
	circleRadius = min(circleRadius * 2, 0.75);
}

//decreases ball size to a minimum of 1% of screen
void sizeDown() {
	circleRadius = max(circleRadius / 2, 0.01);
}

//toggles color cycling
void rainbow() {
	modRainbow = !modRainbow;
}

//make fullscreen
void makeFullscreen() {
	if (fullscreen == false) {
		fullscreen = true;
		glutFullScreen();
	}
	else {
		fullscreen = false;
		glutPositionWindow(100, 100);
		glutReshapeWindow(width, height);
	}
}

//refresh screen every refreshrate (ms)
void repeater(int val) {
	glutPostRedisplay();
	glutTimerFunc(refreshRate, repeater, 0);
}

int main(int argc, char** argv) {
	srand(time(NULL));
	pos[0] = ((rand() % 200) - 100)*0.01; //random initial position
	pos[1] = ((rand() % 200) - 100)*0.01;
	speed[0] = (rand() % 1 == 0) ? -speed[0] : speed[0]; //random direction (but fixed speed)
	speed[1] = (rand() % 1 == 0) ? -speed[1] : speed[1];
	printf("Bouncing Ball\n\n");
	printf("q: Quit\t\tr: Reset\tf: Fullscreen (toggle)\n");
	printf("1: Increase Ball Speed\t\t2: Decrease Ball Speed\n");
	printf("3: Increase Refresh Rate\t4: Decrease Refresh Rate\n");
	printf("5: Double Ball Size\t\t6: Half Ball Size\n");
	printf("7: Wrap around (toggle)\t\t8: Rainbow Ball (toggle)");
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_RGB | GLUT_DOUBLE);
	glutInitWindowSize(width, height);
	glutCreateWindow("3P98 Assign1");
	glutKeyboardFunc(menu);
	glutDisplayFunc(display_image);
	glutTimerFunc(0, repeater, 0);
	glutMainLoop();
	return 0;
}
