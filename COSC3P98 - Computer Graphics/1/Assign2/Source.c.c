#include <stdlib.h>
#include <freeglut.h>
#include <FreeImage.h>
#include <stdio.h>
#include <malloc.h>
#include <time.h>
#include <math.h>
#include <stdbool.h>
#include <Windows.h>

int refreshRate = 60; //how many times it refreshes/s
int height = 750; //static, but can be changed without affecting program
int width = 750;

int minX; //co-ords of max/min x-valued points
int maxX;
int minY;
int maxY;
int numLines = 0; //number of lines, used for while loops
int numTris = 0; //number of triangles, used for while loops
bool toggleMouse = false; //for mouse control

struct lines {
	int startX; //start position of line
	int startY;
	int endX; //end position of line
	int endY;
	struct lines *next; //following line
};

struct triangles {
	int ax;
	int ay; //pt a
	int bx;
	int by; // b
	int cx;
	int cy;// c
	struct triangles *next; //following triangle
};

struct dataPoint {
	int x; //x co-ordinate of point
	int y; //y co-ordinate of point
	struct dataPoint *next; //following point
};

struct dataPoint *list; //linked list of points
struct lines *listLines; //linked list of lines
struct triangles *tris; //linked list of triangles

void loadPoints(int);
void printList(dataPoint *);
void printLines(lines *);
void printStats();
void triangulate();
void peelIt();
void reset();
void mouseLoad(int, int);
void mausInput(int, int, int, int);
int getSize(dataPoint *);
bool checkUnique(dataPoint *, int, int);
void addLine(int, int, int, int);
void addTri(int, int, int, int, int, int);
void removePoints(lines *);
double distance(int, int, int, int, int, int);

void menu(unsigned char key, int x, int y) {
	switch (key) {
	case '1': { //5 random points
				  loadPoints(5);
				  break;
	}
	case '2': { //50 random points
				  loadPoints(50);
				  break;
	}
	case '3': { //100 random points
				  loadPoints(100);
				  break;
	}
	case 'n': { //n random points
				  int n;
				  printf("\nHow many points would you like?\n");
				  scanf_s("%d", &n);
				  loadPoints(n);
				  break;
	}
	case 'm': { //mouse control
				  toggleMouse = !toggleMouse;
				  break;
	}
	case 'c': { //Hull Peel
				  if (getSize(list) >= 3) {
					  peelIt();
					  removePoints(listLines);
				  }
				  else {
					  printf("\nThere are less than three points.");
				  }
				  break;
	}
	case 't': { //trisection triangulation
				  if (getSize(list) >= 3) {
					  peelIt();
					  triangulate();
				  }
				  else {
					  printf("\nThere are less than three points.");
				  }
				  break;
	}
	case 'd': { //diagnostic
				  printList(list);
				  printLines(listLines);
				  printStats();
				  break;
	}
	case 'r': { //reset/clear
				  reset();
				  break;
	}
	}
}

//does quickhull algorithm on one side (top/bottom)
void quickHull(struct dataPoint *points, int x1, int y1, int x2, int y2) {
	struct dataPoint *temp = points;
	double tempD = 0.00; //placeholder
	struct dataPoint *pMax = temp;
	while (temp != NULL) {
		//find distance between the point and line
		double d = distance(x1, y1, x2, y2, temp->x, temp->y);
		if (d > tempD && d != 0) {  //if the distance is higher, make it so
			tempD = d;
			pMax = temp;
		}
		temp = temp->next;
	}
	//if nothing was found
	if (tempD == 0.00) {
		numLines++;
		addLine(x1, y1, x2, y2); //make new line
	}
	else {
		//recursive calls from class
		quickHull(points, x1, y1, pMax->x, pMax->y);
		quickHull(points, pMax->x, pMax->y, x2, y2);
	}
}

//finds the size of available points for that >= 3 constraint
int getSize(struct dataPoint *points) {
	struct dataPoint *temp = points;
	int i = 0;
	while (temp != NULL) {
		i++;
		temp = temp->next;
	}
	return i;
}

//distance from a point to a line (from two points)
double distance(int p1x, int p1y, int p2x, int p2y, int ax, int ay) {
	//str8 from wikipedia but removed the fabs to find directional distance
	double numerator = ((((p2y)-(p1y))*ax) - (((p2x)-(p1x))*ay) + ((p2x)*(p1y)) - ((p2y)*(p1x)));
	double denominator = (sqrt(pow((double)((p2y)-(p1y)), 2) + (pow((double)((p2x)-(p1x)), 2))));
	return numerator / denominator;
}

//selects a random point and creates triangles connecting hull to point
void triangulate(void) {
	struct lines *ptr = listLines;
	struct dataPoint *btr = list;
	int i = rand() % getSize(btr); //rand point
	while (i > 0) {
		i--;
		btr = btr->next;
	}
	i = 0;
	while (i < numLines && ptr != NULL) {
		//make triangle
		addTri(ptr->startX, ptr->startY, ptr->endX, ptr->endY, btr->x, btr->y);
		i++;
		ptr = ptr->next;
	}
}

//adds triangle to triangle L.List
void addTri(int x1, int y1, int x2, int y2, int x3, int y3) {
	struct triangles *temp = (triangles *)malloc(sizeof(struct triangles));
	temp->ax = x1;
	temp->ay = y1;
	temp->bx = x2;
	temp->by = y2;
	temp->cx = x3;
	temp->cy = y3;
	temp->next = NULL;
	if (tris == NULL) { //if no entries exist
		tris = temp; //first entry
	}
	else {
		//append to front
		temp->next = tris;
		tris = temp;
	}
	numTris++;
}

//removes points on the hull so a new hull can be found deeper within
void removePoints(lines *lines) {
	struct lines *temp = lines;
	int i = 0;
	//check if a point exists in any lines
	while (i < numLines && lines != NULL) {
		//check if front
		if ((lines->startX == list->x && lines->startY == list->y)
			|| (lines->endX == list->x && lines->endY == list->y)) {
			list = list->next; //adios
		}
		struct dataPoint *ptr = list;
		//check if middle
		while (ptr->next != NULL) {
			if ((lines->startX == ptr->next->x && lines->startY == ptr->next->y)
				|| (lines->endX == ptr->next->x && lines->endY == ptr->next->y)) {
				//check if last
				if (ptr->next->next == NULL) {
					ptr->next = NULL; //remove from end
					break;
				}
				ptr->next = ptr->next->next; //remove between two points
			}
			ptr = ptr->next; //iterate to next point
		}
		lines = lines->next; //iterate to next line
	}
}

/* finds minimal and maximal x-coordinated point and does Quickhull on that
* when points are removed, find a new min/max point and continue */
void peelIt(void) {
	struct dataPoint *ptr = list;
	int minX = ptr->x; //temp
	int minY = ptr->y; //likewise
	int maxX = ptr->x; //temp
	int maxY = ptr->y; //likewise
	ptr = ptr->next;
	while (ptr != NULL) {
		//search for minimal x
		if (ptr->x < minX) {
			minX = ptr->x;
			minY = ptr->y;
		}
		//and maximal x
		else if (ptr->x > maxX) {
			maxX = ptr->x;
			maxY = ptr->y;
		}
		ptr = ptr->next;
	}
	quickHull(list, minX, minY, maxX, maxY); //do top QHull
	quickHull(list, maxX, maxY, minX, minY); //do bottom next
}

//for mouse input
void mausInput(int button, int state, int x, int y) {
	int maxY = glutGet(GLUT_WINDOW_HEIGHT);
	y = maxY - y; //ypos is from top of screen rather than btm
	if ((button == GLUT_LEFT_BUTTON) && (state == GLUT_DOWN) && toggleMouse) {
		mouseLoad(x, y); //load clicked coord into point list
	}
	glFlush();
}

//takes data from mouse input and loads it here instead
void mouseLoad(int x, int y) {
	struct dataPoint *temp = (dataPoint *)malloc(sizeof(struct dataPoint));
	temp->x = x;
	temp->y = y;
	temp->next = NULL;
	if (list == NULL) { //if no entries exist
		list = temp; //first entry
	}
	else {
		//points must be unique
		if (checkUnique(list, x, y) == TRUE) {
			//append to front
			temp->next = list;
			list = temp;
		}
	}
}

//adds num points to the point list
void loadPoints(int num) {
	struct dataPoint *root = NULL;
	root = (dataPoint *)malloc(sizeof(struct dataPoint));
	struct dataPoint *ptr = root;
	//there's a deadzone of 5% so points are not too close to the edge
	root->x = (int)(rand() % (int)((0.95*width) - (0.05*width)) + 0.05*width);
	root->y = (int)(rand() % (int)((0.95*height) - (0.05*height)) + 0.05*height);
	int i = 1;
	while (i < num) {
		int x = (int)(rand() % (int)((0.95*width) - (0.05*width)) + 0.05*width);
		int y = (int)(rand() % (int)((0.95*height) - (0.05*height)) + 0.05*height);
		if (checkUnique(list, x, y) == TRUE) { //if not unique, try again
			ptr->next = (dataPoint *)malloc(sizeof(struct dataPoint));
			ptr->next->x = x;
			ptr->next->y = y;
			ptr->next->next = NULL;
			ptr = ptr->next;
			i++;
		}
	}
	list = root;
}

//adds start- and end-points to a list so it can make lines
void addLine(int x1, int y1, int x2, int y2) {
	struct lines *temp = (lines *)malloc(sizeof(struct lines));
	temp->startX = x1;
	temp->endX = x2;
	temp->startY = y1;
	temp->endY = y2;
	if (listLines == NULL) { //if no entries exist
		listLines = temp; //first entry
	}
	else {
		//append to front
		temp->next = listLines;
		listLines = temp;
	}
}

//allows insertion only if a point is wholly unique
bool checkUnique(dataPoint *root, int x, int y) {
	struct dataPoint *ptr = root;
	while (ptr != NULL) {
		//if that same point already exists...
		if (ptr->x == x && ptr->y == y) {
			return false;
		}
		ptr = ptr->next;
	}
	//otherwise...
	return true;
}

//prints numlines, numtris, numpoints
void printStats(void) {
	printf("\n\n%d points, %d lines, %d triangles", getSize(list), numLines, numTris);
}

//displays the position of each point
void printList(dataPoint *root) {
	struct dataPoint *ptr = root;
	if (ptr != NULL) { //if list unempty
		printf("\n");
		while (ptr != NULL) {
			//shows x- and y-coordinates for each point
			printf("\nx:%d\ty:%d", ptr->x, ptr->y);
			ptr = ptr->next; //iterate to next
		}
	}
}

//displays the start- and end-points of each line
void printLines(lines *line) {
	struct lines *ptr = line;
	if (ptr != NULL) { //if list unempty
		printf("\n");
		int i = 0;
		while (i < numLines && ptr != NULL) {
			//shows start and end positions of all lines
			printf("\nLine from (%d,%d) to (%d,%d)", ptr->startX, ptr->startY, ptr->endX, ptr->endY);
			i++;
			ptr = ptr->next; //iterate to next
		}
	}
}

//resets points and lines
void reset() {
	free(list); //free up the memory
	free(listLines);
	free(tris);
	list = NULL; //reset to NULL
	listLines = NULL;
	tris = NULL;
	numLines = 0; //reset counters
	numTris = 0;
}

//draw points and lines
void display(void) {
	struct dataPoint *ptr = list;
	struct lines *btr = listLines;
	struct triangles *wtr = tris;
	glClear(GL_COLOR_BUFFER_BIT);
	glPointSize(3.0); //so it's more visible
	glColor3f(1.0, 0.0, 1.0); //purple
	glBegin(GL_POINTS); //start dot drawing
	while (ptr != NULL) //while points exist
	{
		glVertex2i(ptr->x, ptr->y); //draw the point
		ptr = ptr->next; //iterate to next
	}
	glEnd(); //stop dot drawing
	int i = 0;
	glColor3f(0.50, 0.55, 0.6); //silvery blue
	glBegin(GL_LINES); //start line drawing
	/* for some reason, the below wouldn't work without
	* i < numLines, even though it should. I've left it in
	* to stay safe */
	while (i < numLines && btr != NULL) {
		glVertex2i(btr->startX, btr->startY); //start point
		glVertex2i(btr->endX, btr->endY); //end point of line
		i++;
		btr = btr->next; //iterate to next
	}
	glEnd(); //stop line drawing
	i = 0;
	glColor3f(0.0, 0.5, 1.0);
	glBegin(GL_LINES);
	while (i < numTris && wtr != NULL) {
		glVertex2i(wtr->ax, wtr->ay);
		glVertex2i(wtr->bx, wtr->by);
		glVertex2i(wtr->bx, wtr->by);
		glVertex2i(wtr->cx, wtr->cy);
		glVertex2i(wtr->cx, wtr->cy);
		glVertex2i(wtr->ax, wtr->ay);
		wtr = wtr->next;
	}
	glEnd();
	glFlush();
}

/* refresh display every refreshrate (ms)
* this isn't quite necessary but saves me the trouble
* of redrawing the display each time a command is done */
void repeater(int val) {
	glutPostRedisplay();
	glutTimerFunc(refreshRate, repeater, 0);
}

int main(int argc, char** argv)
{
	srand(time(NULL));
	printf("1: 5 Random Points\t2: 50 Random Points\t3: 100 Random Points\n");
	printf("n: n Random Points\tm: Enable/disable Mouse Control\n");
	printf("c: Convex Hull\t\tt: Triangulate\t\td: Print Points & Lines\n");
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_RGB | GLUT_SINGLE);
	glutInitWindowSize(height, width);
	glutCreateWindow("Convex Hull Peel");
	glutKeyboardFunc(menu);
	glMatrixMode(GL_PROJECTION);
	gluOrtho2D(0.0, height, 0.0, width);
	glutDisplayFunc(display);
	glutMouseFunc(mausInput);
	glutTimerFunc(0, repeater, 0);
	glutMainLoop();
	return 0;
}