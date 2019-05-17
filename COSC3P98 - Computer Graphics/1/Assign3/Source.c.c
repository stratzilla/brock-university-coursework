#include <stdlib.h>
#include <stdio.h>
#include <malloc.h>
#include <freeglut.h>
#include <math.h>
#include <stdbool.h>
#include <time.h>

int refreshRate = 20; //how fast the scene is redrawn
int height = 800;
int width = 800;
int numCubes = 0; //number of cubes on screen
int numFloors = 0; //number of floors
float scale = 0.25; //biggest a cube can get (radius)
double grav = 0.1; //gravity
double fric = 0.2; //friction
int objType = 3; //appearance of the cubes
double rNess = 0.2; //randomness of stream
int pan = 25; //pan top/bottom
int zoom = 50; //zoom in/out
bool removeDead = true; //remove dead cubes?
bool constFire = false; //constant fire?
bool randSpeed = false; //random velocity of cube?
bool shadeMode = true; //flat or smooth?
bool useLight = true; //light on/off?
bool useCull = false; //use culling?
bool paused = false; //pause animation?
bool collide = false; //collision on/off?
bool drawPaths = false; //draw paths?
double yRotate = 212.50; //for rotating camera along y-axis
float lowestFloor = 0.0; //depth on y-axis of lowest floor
//light is positioned in a corner of the environment
GLfloat lightSource[] = { 150.0, 150.0, 150.0, 0.0 }; //light position
GLfloat light[] = { 1.0, 1.0, 1.0, 1.0 }; //light color

void addCube(void);
void animateStep(struct cubes *);
void killCubes(int);
void collideCheck(struct cubes *);
float floorColl(struct cubes *);

struct cubes {
	float pos[3]; //position of the cube
	double dir[3]; //direction of the cube
	int col[3]; //color of the cube
	double life; //cubes have finite life
	float scale; //scale of the cube
	double speed; //how fast is it moving
	int lineAlias; //spacing between line segments for paths
	int numLines; //number of points to make lines
	struct cubes *next; //subsequent cube in list
	struct lines *lines; //the path the particle takes
};

struct floors {
	float pos; //y-position of floor
	float dim; //dimensions of floor
	struct floors *next; //subsequent floor in list
};

struct lines {
	float pos[3]; //position of the line point
	struct lines *prev; //previous points
	struct lines *next; //next point
};

struct cubes *listCubes; //list of cubes
struct floors *listFloors; //list of floors
struct lines *listLines; //list of points to make lines

void initGlut(void) {
	glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGBA | GLUT_DEPTH);
	glutInitWindowSize(height, width);
	glutCreateWindow("3P98 Particle Simulation");
	glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	glViewport(0, 0, height, width);
}

void initDisplay(void) {
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); //clear buffers
	glClearDepth(1.0f); //set depth clear
	glEnable(GL_DEPTH_TEST); //enable depth test
	if (!useLight) { //if lights off
		glDisable(GL_LIGHTING); //turn 'em off
	}
	else { //otherwise they're on
		glEnable(GL_LIGHTING); //keep/turn them on
	}
	glEnable(GL_LIGHT0); //single light source
	glLightfv(GL_LIGHT0, GL_POSITION, lightSource); //init light source
	glEnable(GL_COLOR_MATERIAL); //enable material color tracking
	glColorMaterial(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE); //set it
	glEnable(GL_BLEND); //enable blending
	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA); //set it
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	if (!useCull) { //using backface culling or not
		glDisable(GL_CULL_FACE);
	}
	else { //yes
		glEnable(GL_CULL_FACE);
	}
	glCullFace(GL_BACK); //only cull back
	glDepthFunc(GL_LEQUAL); //depth buffer compares =<
	if (shadeMode) { //flat or smooth
		glShadeModel(GL_SMOOTH);
	}
	else {
		glShadeModel(GL_FLAT);
	}
	//nicest perspective correction
	glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
	GLint viewport[4];
	glGetIntegerv(GL_VIEWPORT, viewport);
	double aspect = (double)viewport[2] / (double)viewport[3];
	gluPerspective(74, aspect, 2, 300); //taken online for square aspect (n*n)
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
	glEnable(GL_NORMALIZE);
	double xCam = zoom * cos(yRotate); //for y-rot
	double zCam = zoom * sin(yRotate); //for y-rot
	gluLookAt(xCam, pan, zCam, 0, -20, 0, 0, 1, 0);
}

//renders the scene
void display(void) {
	initDisplay();
	if (constFire && !paused) { //if rapid fire enabled
		addCube();
	}
	int j = 0;
	struct floors *btr = listFloors; //temp list
	//floors
	while (j < numFloors && btr != NULL) { //for every floor in pyramid
		double blend = ((double)(j + 1) / (double)(numFloors)) * 255;
		glColor4ub(186, 126, 207, blend); //purple of varying alpha
		glPushMatrix();
		glTranslatef(0, btr->pos, 0); //move into position
		glScalef(btr->dim, 0.1, btr->dim); //make floor of specified dimensions
		glutSolidCube(2); //just an elongated cube
		glPopMatrix();
		j++;
		btr = btr->next;
	}
	glTranslatef(0, 0, 0); //go back to origin
	struct cubes *ptr = listCubes; //temp list
	int i = 0;
	while (i < numCubes && ptr != NULL) {
		if (ptr->life > 0) { //if selected cube has nonzero speed
			glPushMatrix(); //ready to make cube
			double blend = (ptr->life / 100) * 255; //alpha/opacity
			/* color is cyan for unbounced cube, yellow for bounced cube
			* magenta for cube without speed (dying) */
			glColor4ub(ptr->col[0], ptr->col[1], ptr->col[2], blend);
			if (objType != 2) { //solid or wireframe cubes
				glTranslatef(ptr->pos[0], ptr->pos[1], ptr->pos[2]); //go to position of cube
				if (objType == 0) { //solid cube
					glutSolidCube(ptr->scale * 5); //cubes are 5 units large or a fraction of this
				}
				else if (objType == 1) { //wire cube
					glutWireCube(ptr->scale * 5);
				}
				else if (objType == 3) { //sphere to test shading
					glutSolidSphere(ptr->scale * 5, 10, 15);
				}
				else { //wire sphere
					glutWireSphere(ptr->scale * 5, 10, 15);
				}
			}
			else { //single point in 3-space
				glPointSize(3); //so they're at least a little visible
				glBegin(GL_POINTS);
				glVertex3f(ptr->pos[0], ptr->pos[1], ptr->pos[2]);
				glEnd();
			}
			glPopMatrix(); //go back to position 0,0,-35
			if (drawPaths) { //if pathdrawing enabled
				int j = 0;
				struct lines *pts = ptr->lines;
				//foreach point a part. has traveled
				while (j < ptr->numLines && pts->next != NULL) {
					if (pts->prev != NULL) { //only 250 positions
						glColor4ub(255, 255, 255, blend); //white
						glBegin(GL_LINES);
						//draw line from point n-1 to point n
						glVertex3f(pts->prev->pos[0], pts->prev->pos[1], pts->prev->pos[2]);
						glVertex3f(pts->pos[0], pts->pos[1], pts->pos[2]);
						glEnd(); //maps the entire path a particle took
					}
					j++;
					pts = pts->prev;
				}
			}
			// do an animation step
			if (!paused) {
				animateStep(ptr);
			}
		}
		else { //if magenta and life is 0 or below killplane
			killCubes(i); //remove it
		}
		i++;
		ptr = ptr->next;
	}
	glFlush();
	glutSwapBuffers();
}

/* adds a cube to the list of cubes
* each cube is then rendered from variables
* defined at initialization */
void addCube(void) {
	struct cubes *temp = (struct cubes *)malloc(sizeof(struct cubes));
	//initial position is 0,0,0
	temp->pos[0] = 0;
	temp->pos[1] = 15;
	temp->pos[2] = 0;
	//no initial direction
	temp->dir[0] = (((double)(rand() % 100) / 100) - 0.5) * rNess;
	temp->dir[1] = 0;
	temp->dir[2] = (((double)(rand() % 100) / 100) - 0.5) * rNess;
	//initial color of a cube is cyan
	temp->col[0] = 0;
	temp->col[1] = 162;
	temp->col[2] = 211;
	temp->lineAlias = 0;
	//cube begins alive
	temp->life = 100; //100 life to start
	temp->scale = scale; //how large the cube is
	//initial speed shot, can be random from 1x to 30x or 1x static
	struct lines *pts = (struct lines *)malloc(sizeof(struct lines));
	pts->prev = NULL;
	pts->next = NULL;
	pts->pos[0] = temp->pos[0]; //first point to draw lines from is init. pos.
	pts->pos[1] = temp->pos[1];
	pts->pos[2] = temp->pos[2];
	temp->lines = pts;
	temp->numLines = 1; //1 point initially
	temp->speed = (randSpeed) ? ((double)(rand() % 30) / -10) - 0.01 : -0.01;
	if (numCubes == 0) { //if no cubes exist yet
		listCubes = temp; //make temp 1st cube
	}
	else { //if there's >= 1 cubes already
		temp->next = listCubes; //append to front of list
		listCubes = temp;
	}
	numCubes++; //increment number of cubes in list
}

/* creates multiple floors for which
* the objects can bounce off of */
void constructFloors(float pos, float dim) {
	lowestFloor = min(lowestFloor, pos); //find new lowest floor position
	struct floors *temp = (struct floors *)malloc(sizeof(struct floors));
	temp->pos = pos;
	temp->dim = dim;
	if (numFloors == 0) { //no floors yet
		listFloors = temp;
	}
	else { //a floor or floors exist already
		temp->next = listFloors;
		listFloors = temp;
	}
	numFloors++; //inc num floors
}

/* removes cubes if their life is 0
* i.e. they're stationary on the ground
* or if they're below the killplane (lowestFloor) */
void killCubes(int i) {
	struct cubes *ptr = listCubes;
	int j = 0;
	if (i == 0 && numCubes == 1) { //if only element
		listCubes = NULL;
		free(listCubes);
	}
	else if (i == 0 && numCubes != 1) { //if first element
		listCubes = listCubes->next;
	}
	else {
		while (j < numCubes && ptr->next->next != NULL) {
			if (j + 1 == i) { //if next (middle) element
				ptr->next = ptr->next->next;
				break;
			}
			else if (ptr->next->next == NULL) { //if last element
				ptr->next = NULL;
				break;
			}
			j++;
			ptr = ptr->next;
		}
	}
	numCubes--; //dec number of cubes to iterate
}

/* moved to its own function so as to not clutter
* the display function. Animates a step by calculating
* new positions from variables and updates them */
void animateStep(struct cubes *ptr) {
	if (ptr->speed != 0) { //if cube has momentum
		ptr->speed -= grav; //let gravity affect speed
		ptr->pos[0] += (float)ptr->dir[0]; //change in x
		//gravity affects y-pos only (?)
		ptr->pos[1] += (float)ptr->dir[1] + (float)ptr->speed; //change in y
		ptr->pos[2] += (float)ptr->dir[2]; //change in z
	}
	float collY = floorColl(ptr); //check if particle coll. with a floor
	if (collY != 0) { //if it hits a floor
		if (ptr->col[0] == 0) { //if cyan
			ptr->col[0] = 250; //change to yellow after first bounce
			ptr->col[1] = 224;
			ptr->col[2] = 20;
		}
		//if radius of cube goes below floor, instead set it to collide
		ptr->pos[1] = collY + 5 * ptr->scale;
		//apply friction/momentum lost after bounce
		ptr->speed = round(10000 * (-ptr->speed / (1 + fric))) / 10000; //lose fric% momentum
		//since speed will never hit zero, an arbitrary speed close to 0 is chosen instead
		if (ptr->speed <= (grav - 0.01) && ptr->speed != 0) {
			ptr->speed = 0.0; //set it to zero after this threshold
		}
	}
	if (collide) {
		collideCheck(ptr);
	}
	//if speed is zero or outside collision range of floor
	if (ptr->speed == 0.0 || ptr->pos[1] < lowestFloor) {
		if (ptr->col[0] == 250 || ptr->col[0] == 0) { //and cube is yellow or cyan
			ptr->col[0] = 224; //change to magenta
			ptr->col[1] = 8;
			ptr->col[2] = 133;
		}
		if (removeDead || ptr->pos[1] < lowestFloor) { //only if toggled on
			ptr->life--; //cube begins to die when magenta
		}
	}
	else { //otherwise, make a new point to draw lines from
		/* making lines for every single position of a particle is taxing
		* so instead only make a line every 2 positions. Paths are now
		* aliased, but not so much it's ugly. Performance greatly improved. */
		if (ptr->lineAlias % 2 == 0 && ptr->speed != 0.00) {
			struct lines *pts = (struct lines *)malloc(sizeof(struct lines));
			pts->pos[0] = ptr->pos[0];
			pts->pos[1] = ptr->pos[1];
			pts->pos[2] = ptr->pos[2];
			pts->prev = ptr->lines; //inserted in front
			ptr->lines = pts;
			ptr->numLines++;
		}
		ptr->lineAlias++;
	}
}

// checks collision between particles
void collideCheck(struct cubes *ptr) {
	struct cubes *btr = listCubes;
	int i = 0;
	while (i < numCubes && btr != NULL) {
		if ((btr != ptr)
			&& (btr->col[0] == 224 && ptr->col[0] != 224)) { //only collide yellow-on-magenta
			double xDiff = pow((btr->pos[0] - ptr->pos[0]), 2);
			double yDiff = pow((btr->pos[1] - ptr->pos[1]), 2);
			double zDiff = pow((btr->pos[2] - ptr->pos[2]), 2);
			double distance = sqrt(xDiff + yDiff + zDiff); //checks distance between particles
			//keep in mind radius
			if (distance <= (ptr->scale * 5 + btr->scale * 5) && ptr->col[0] != 0) {
				if (ptr->pos[0] > btr->pos[0]) {
					ptr->dir[0] = -ptr->dir[0]; //reverse x-dir
				}
				if (ptr->pos[1] > btr->pos[1]) {
					ptr->speed = -ptr->speed; //reverse downward speed to bounce off
				}
				if (ptr->pos[2] > btr->pos[2]) {
					ptr->dir[2] = -ptr->dir[2]; //reverse z-dir
				}
			}
		}
		i++;
		btr = btr->next;
	}
}

/* returns a valid value if cube hits floor */
float floorColl(struct cubes *ptr) {
	int i = 0;
	struct floors *btr = listFloors;
	while (i < numFloors && btr != NULL) { //checks each floor
		if ((ptr->pos[1] < (btr->pos + 5 * ptr->scale)) //if hit y-pos of floor
			&& (ptr->pos[0] > (-btr->dim - (5 * ptr->scale))) //and within x-pos of floor
			&& (ptr->pos[0] < (btr->dim + (5 * ptr->scale)))
			&& (ptr->pos[2] > (-btr->dim - (5 * ptr->scale))) //and z-pos, too
			&& (ptr->pos[2] < (btr->dim + (5 * ptr->scale)))) {
			return btr->pos; //return colliding floor y-pos
		}
		i++;
		btr = btr->next;
	}
	return 0; //return 0 so it'll catch no collision
}

/* refresh display every refreshrate */
void repeater(int val) {
	glutPostRedisplay();
	glutTimerFunc(refreshRate, repeater, 0);
}

//gravity menu
void gravMenu(int choice) {
	switch (choice) {
	case 1: { //zero gravity
				grav = 0;
				break;
	}
	case 2: { //1/4x gravity
				grav = 0.025;
				break;
	}
	case 3: { //1x gravity
				grav = 0.1;
				break;
	}
	case 4: { //4x gravity
				grav = 0.4;
				break;
	}
	case 5: { //16x gravity
				grav = 1.6;
	}
	}
}

//friction menu
void fricMenu(int choice) {
	switch (choice) {
	case 1: { //zero friction
				fric = 0;
				break;
	}
	case 2: { //1/4x friction
				fric = 0.05;
				break;
	}
	case 3: { //1x friction
				fric = 0.2;
				break;
	}
	case 4: { //4x friction
				fric = 0.8;
				break;
	}
	case 5: { //16x friction
				fric = 3.2;
	}
	}
}

//menu for appearance of particle
void appMenu(int choice) {
	switch (choice) {
	case 1: { //solid cubes
				objType = 0;
				break;
	}
	case 2: { //wireframe cubes
				objType = 1;
				break;
	}
	case 3: { //3D points
				objType = 2;
				break;
	}
	case 4: { //solid sphere
				objType = 3;
	}
	case 5: { //wire sphere
				objType = 4;
	}
	}
}

//menu for size of particle (only cubes)
void sizMenu(int choice) {
	switch (choice) {
	case 1: { //1/4x size
				scale = 0.075f;
				break;
	}
	case 2: { //1/2x size
				scale = 0.125f;
				break;
	}
	case 3: { //1x size
				scale = 0.25f;
				break;
	}
	case 4: { //2x size
				scale = 0.50f;
				break;
	}
	}
}

//particle randomness menu
void ranMenu(int choice) {
	switch (choice) {
	case 1: { //not random (ie straight down)
				rNess = 0.00;
				break;
	}
	case 2: { //low randomness
				rNess = 0.10;
				break;
	}
	case 3: { //normal randomness
				rNess = 0.20;
				break;
	}
	case 4: { //high randomness
				rNess = 0.30;
				break;
	}
	case 5: { //v.high randomness
				rNess = 0.40;
	}
	}
}

//firing options menu
void fiMenu(int choice) {
	switch (choice) {
	case 1: { //turn constant fire on/off
				constFire = !constFire;
				break;
	}
	case 2: { //random initial velocity toggle
				randSpeed = !randSpeed;
				break;
	}
	}
}

//shading/lighting/culling menu
void shMenu(int choice) {
	switch (choice) {
	case 1: { //smooth/flat
				shadeMode = !shadeMode;
				break;
	}
	case 2: { //light
				useLight = !useLight;
				break;
	}
	case 3: { //backface culling
				useCull = !useCull;
				break;
	}
	}
}

//menu for pause toggle and anim options
void anMenu(int choice) {
	switch (choice) {
	case 1: { //pause
				paused = !paused;
				break;
	}
	}
}

//menu for animation speed
void spMenu(int choice) {
	switch (choice) {
	case 1: { //slow
				refreshRate = 100;
				break;
	}
	case 2: { //normal
				refreshRate = 20;
				break;
	}
	case 3: { //high
				refreshRate = 5;
				break;
	}
	}
}

//main menu for RMB
void topMenu(int choice) {
	switch (choice) {
	case 'r': { //reset all environment variables
				  free(listCubes);
				  listCubes = NULL;
				  numCubes = 0;
				  refreshRate = 20;
				  numCubes = 0;
				  scale = 0.25;
				  grav = 0.1;
				  fric = 0.2;
				  objType = 3;
				  rNess = 0.2;
				  pan = 25;
				  zoom = 50;
				  removeDead = true;
				  constFire = false;
				  randSpeed = false;
				  shadeMode = true;
				  useLight = true;
				  useCull = false;
				  paused = false;
				  collide = false;
				  drawPaths = false;
				  yRotate = 212.50;
				  break;
	}
	case 'p': { //pause animation
				  paused = !paused;
				  break;
	}
	case 'q': { //quit program
				  exit(0);
	}
	case 1: { //toggles if cubes disappear when dying
				removeDead = !removeDead;
				break;
	}
	case 2: { //draw lines showing path of particle
				drawPaths = !drawPaths;
				break;
	}
	}
}

//for collision
void objMenu(int choice) {
	switch (choice) {
	case 1: { //turn collision on/off
				collide = !collide;
	}
	}
}

//keyboard controls
void menu(unsigned char key, int x, int y) {
	switch (key) {
	case 'f': { //manual firing mode
				  addCube();
				  break;
	}
	case 'g': { //show environment variables
				  printf("\nNumber of Cubes: %d\n", numCubes);
				  printf("Current Gravity: %.2f\n", grav);
				  printf("Current Friction: %.2f\n", fric);
				  break;
	}
	case '1': {
				  yRotate = (yRotate == 359.9) ? 0.0 : yRotate + 0.1;
				  break;
	}
	case '2': {
				  yRotate = (yRotate == 0.0) ? 359.9 : yRotate - 0.1;
				  break;
	}
	case '3': {
				  pan += 1;
				  break;
	}
	case '4': {
				  pan -= 1;
				  break;
	}
	case '5': {
				  zoom++;
				  break;
	}
	case '6': {
				  zoom--;
				  break;
	}
	}
}

int main(int argc, char** argv)
{
	printf("Right-click menu allows you to change:\n");
	printf(" - Gravity Settings\n");
	printf(" - Friction Settings\n");
	printf(" - Particle Settings\n");
	printf("   - Size of particle\n");
	printf("   - Appearance of particle\n");
	printf("   - Inter-particle collision\n");
	printf(" - Firing Settings\n");
	printf("   - Particle spread\n");
	printf("   - Speed particles are fired\n");
	printf("   - Constant firing mode\n");
	printf(" - Light/Shade/Render Settings\n");
	printf("   - Smooth/Flat shading toggle\n");
	printf("   - Turn lights on/off\n");
	printf("   - Turn backface culling on/off\n");
	printf(" - Animation Settings\n");
	printf("   - Animation speed\n");
	printf("   - Pause animation\n");
	printf(" - Particle Mortality\n");
	printf(" - Path drawing toggle\n\n");
	printf("Press 'F' to manually fire one particle, or hold to fire a stream.\n");
	printf("Press 'G' to see diagnostic information on environment.\n");
	printf("Press '1' or '2' to rotate along y-axis.\n");
	printf("Press '3' or '4' to swivel towards/away scene.\n");
	printf("Press '5' or '6' to pan towards/away scene.\n");
	srand((unsigned int)time(NULL));
	glutInit(&argc, argv);
	//construct a simple 5-layer pyramid
	constructFloors(-15.0, 25);
	constructFloors(-12.5, 20);
	constructFloors(-10.0, 15);
	constructFloors(-7.5, 10);
	constructFloors(-5.0, 5);
	initGlut();
	glutDisplayFunc(display);
	glutKeyboardFunc(menu);
	glutTimerFunc(0, repeater, 0);
	//menu for adjusting gravity settings
	int gravityMenu = glutCreateMenu(gravMenu);
	glutAddMenuEntry("No Gravity (0x)", 1);
	glutAddMenuEntry("Low Gravity (1/4x)", 2);
	glutAddMenuEntry("Normal Gravity (1x)", 3);
	glutAddMenuEntry("High Gravity (4x)", 4);
	glutAddMenuEntry("V. High Gravity (16x)", 5);
	//menu for adjusting friction settings
	int frictionMenu = glutCreateMenu(fricMenu);
	glutAddMenuEntry("No Friction (0x)", 1);
	glutAddMenuEntry("Low Friction (1/4x)", 2);
	glutAddMenuEntry("Normal Friction (1x)", 3);
	glutAddMenuEntry("High Friction (4x)", 4);
	glutAddMenuEntry("V. High Friction (16x)", 5);
	//menu for adjusting cube size
	int sizeMenu = glutCreateMenu(sizMenu);
	glutAddMenuEntry("Very Small (1/4x)", 1);
	glutAddMenuEntry("Small (1/2x)", 2);
	glutAddMenuEntry("Normal (1x)", 3);
	glutAddMenuEntry("Big (2x)", 4);
	//menu for adjusting cube appearance
	int appearMenu = glutCreateMenu(appMenu);
	glutAddMenuEntry("Solid Cubes", 1);
	glutAddMenuEntry("Wire Cubes", 2);
	glutAddMenuEntry("Solid Sphere", 4);
	glutAddMenuEntry("Wire Sphere", 5);
	glutAddMenuEntry("3D Points", 3);
	//leads to two above menus
	int objectMenu = glutCreateMenu(objMenu);
	glutAddSubMenu("Particle Appearance", appearMenu);
	glutAddSubMenu("Particle Size", sizeMenu);
	glutAddMenuEntry("Interparticle Collision toggle", 1);
	//menu for spread randomness
	int randomMenu = glutCreateMenu(ranMenu);
	glutAddMenuEntry("Not Random (0x)", 1);
	glutAddMenuEntry("Low Randomness (1/2x)", 2);
	glutAddMenuEntry("Normal Randomness (1x)", 3);
	glutAddMenuEntry("High Randomness (3/2x)", 4);
	glutAddMenuEntry("V. High Randomness (2x)", 5);
	//menu for firing options
	int fireMenu = glutCreateMenu(fiMenu);
	glutAddSubMenu("Spread Randomness", randomMenu);
	glutAddMenuEntry("Toggle Constant Firing", 1);
	glutAddMenuEntry("Toggle Random Initial Speed", 2);
	//menu for light/shading
	int shadeMenu = glutCreateMenu(shMenu);
	glutAddMenuEntry("Toggle Smooth/Flat Shading", 1);
	glutAddMenuEntry("Turn Light On/Off", 2);
	glutAddMenuEntry("Turn Culling On/Off", 3);
	//menu for animation speed
	int spdMenu = glutCreateMenu(spMenu);
	glutAddMenuEntry("Slow (1/15x)", 1);
	glutAddMenuEntry("Normal (1x)", 2);
	glutAddMenuEntry("Fast (15x)", 3);
	//menu for animation
	int animMenu = glutCreateMenu(anMenu);
	glutAddSubMenu("Animation Speed", spdMenu);
	glutAddMenuEntry("Pause Animation", 1);
	//main menu to change settings
	int mainMenu = glutCreateMenu(topMenu);
	glutAddSubMenu("Gravity Settings", gravityMenu);
	glutAddSubMenu("Friction Settings", frictionMenu);
	glutAddSubMenu("Particle Settings", objectMenu);
	glutAddSubMenu("Firing Settings", fireMenu);
	glutAddSubMenu("Light/Shading Settings", shadeMenu);
	glutAddSubMenu("Animation Settings", animMenu);
	glutAddMenuEntry("Toggle Immortality", 1);
	glutAddMenuEntry("Toggle Path Drawing", 2);
	glutAddMenuEntry("Pause Animation", 'p');
	glutAddMenuEntry("Reset", 'r');
	glutAddMenuEntry("Quit", 'q');
	glutAttachMenu(GLUT_RIGHT_BUTTON);
	glutMainLoop();
	return 0;
}