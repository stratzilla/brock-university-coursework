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

/* initialize red, green, blue channels
* for pixel */
typedef struct {
	GLubyte r, g, b;
} pixel;

/* image characteristics including
* pixel data and dimensions */
typedef struct {
	pixel *data;
	int width, height;
} glob;

int checkCloser(int col[3], int rgbVals[9][3]);

/* displayImg is work buffer,
* originalImg is save buffer */
glob displayImg, originalImg;

/* loads image into originalImg
* to be worked on by filters, etc
* components of assignment. Much
* of this is taken from the sln template */
pixel *imageLoader(char *name, int *width, int *height) {
	FIBITMAP *image;
	RGBQUAD pixelData;
	pixel *data;
	int k = 0;
	image = FreeImage_Load(FIF_TIFF, name, 0); //attempts to load image
	if (!image) { return NULL; } //if bad/no image
	*width = FreeImage_GetWidth(image); //init width
	*height = FreeImage_GetHeight(image); //init height
	data = (pixel*)malloc((*height)*(*width)*sizeof(pixel));
	for (int i = 0; i < *height; i++) {
		for (int j = 0; j < *width; j++, k++) {
			FreeImage_GetPixelColor(image, j, i, &pixelData);
			//loads pixel color data
			data[k].r = pixelData.rgbRed;
			data[k].g = pixelData.rgbGreen;
			data[k].b = pixelData.rgbBlue;
		}
	}
	FreeImage_Unload(image); //no longer needed in mem
	return data;
}

/* saves a copy of image
* either processed or not */
void saveImage(char *name, pixel *data, int width, int height) {
	FIBITMAP *image;
	RGBQUAD pixelData;
	int k = 0;
	image = FreeImage_Allocate(width, height, 24, 0, 0, 0);
	if (!image) { return; } //if bad/no image
	for (int i = 0; i < height; i++) {
		for (int j = 0; j < width; j++, k++) {
			//loads pixel color data
			pixelData.rgbRed = data[k].r;
			pixelData.rgbGreen = data[k].g;
			pixelData.rgbBlue = data[k].b;
			//puts it in image
			FreeImage_SetPixelColor(image, j, i, &pixelData);
		}
	}
	FreeImage_Save(FIF_TIFF, image, name, 0); //save image
	FreeImage_Unload(image); //no longer needed in mem
}

/* averages color channels to create a
* grey which is then applied to all channels
* making the image greyscale */
pixel *makeGrey(pixel *img, int width, int height) {
	int k = 0;
	int avgLum = 0;
	for (int i = 0; i < width; i++) {
		for (int j = 0; j < height; j++, k++) {
			avgLum = (img[k].r + img[k].g + img[k].b) / 3; //finds mean lum
			img[k].r = avgLum; //init each channel as mean lum
			img[k].g = avgLum;
			img[k].b = avgLum;
		}
	}
	return img; //make displayImg the processed image
}

/* similar to the above, but a type
* of greyscale meant to accentuate greens */
pixel *makeNTSC(pixel *img, int width, int height) {
	int k = 0;
	double lumNTSC = 0.00;
	for (int i = 0; i < width; i++) {
		for (int j = 0; j < height; j++, k++) {
			//formula from assignment sheet
			lumNTSC = (img[k].r*0.299) + (img[k].g*0.587) + (img[k].b*0.114);
			img[k].r = lumNTSC;
			img[k].g = lumNTSC;
			img[k].b = lumNTSC;
		}
	}
	return img; //make displayImg the processed image
}

/* changes image to strictly black
* or white, depending on the grey value of each pixel */
pixel *makeMonochrome(pixel *img, int width, int height) {
	int k = 0;
	int avgLum;
	for (int i = 0; i < width; i++) {
		for (int j = 0; j < height; j++, k++) {
			avgLum = (img[k].r + img[k].g + img[k].b) / 3;
			//if the value is closer to black (0), then make it black; white otherwise
			img[k].r = (avgLum > 128) ? 255 : 0;
			img[k].g = (avgLum > 128) ? 255 : 0;
			img[k].b = (avgLum > 128) ? 255 : 0;
		}
	}
	return img; //make displayImg the processed image
}

/* swaps red channel with green,
* green with blue, blue with red */
pixel *makeSwapped(pixel *img, int width, int height) {
	int k = 0;
	int placeHolder = 0;
	for (int i = 0; i < width; i++) {
		for (int j = 0; j < height; j++, k++) {
			placeHolder = img[k].r; //so r isnt clobbered
			img[k].r = img[k].g; //swap red for green
			img[k].g = img[k].b; //g for b
			img[k].b = placeHolder; //b for r
		}
	}
	return img; //make displayImg the processed image
}

/* displays only colors of a given channel,
* black otherwise */
pixel *makeSingleChannel(pixel *img, int width, int height, char val) {
	int k = 0;
	for (int i = 0; i < width; i++) {
		for (int j = 0; j < height; j++, k++) {
			if (val == 'R') { //if red
				img[k].g = 0; //black out other channels
				img[k].b = 0;
			}
			else if (val == 'G') { //if green
				img[k].r = 0;
				img[k].b = 0;
			}
			else { //if blue
				img[k].r = 0;
				img[k].g = 0;
			}
		}
	}
	return img; //make displayImg the processed image
}

pixel *makeMax(pixel *img, int width, int height) {
	glob tempImg;
	int k = 0;
	int rgbTemp[3] = { 0, 0, 0 };
	tempImg.height = height;
	tempImg.width = width;
	tempImg.data = (pixel*)malloc(sizeof(pixel)*width*height);
	memcpy(tempImg.data, img, sizeof(pixel)*width*height);
	//checks maximum channel values for surrounding 8 pixels
	for (int i = 0; i < width; i++) {
		for (int j = 0; j < height; j++, k++) {
			/* I wrote comments from bottom to top, so see the convolution filter
			* for explanation of what I'm doing here, I'll comment differences */
			rgbTemp[0] = tempImg.data[k].r;
			rgbTemp[1] = tempImg.data[k].g;
			rgbTemp[2] = tempImg.data[k].b;
			if (k - width - 1 >= 0) {
				//finds maximum values
				rgbTemp[0] = max(rgbTemp[0], tempImg.data[k - width - 1].r);
				rgbTemp[1] = max(rgbTemp[1], tempImg.data[k - width - 1].g);
				rgbTemp[2] = max(rgbTemp[2], tempImg.data[k - width - 1].b);
			}
			if (k - width >= 0) {
				rgbTemp[0] = max(rgbTemp[0], tempImg.data[k - width].r);
				rgbTemp[1] = max(rgbTemp[1], tempImg.data[k - width].g);
				rgbTemp[2] = max(rgbTemp[2], tempImg.data[k - width].b);
			}
			if (k - width + 1 >= 0) {
				rgbTemp[0] = max(rgbTemp[0], tempImg.data[k - width + 1].r);
				rgbTemp[1] = max(rgbTemp[1], tempImg.data[k - width + 1].g);
				rgbTemp[2] = max(rgbTemp[2], tempImg.data[k - width + 1].b);
			}
			if ((k - 1) % width != 0) {
				rgbTemp[0] = max(rgbTemp[0], tempImg.data[k - 1].r);
				rgbTemp[1] = max(rgbTemp[1], tempImg.data[k - 1].g);
				rgbTemp[2] = max(rgbTemp[2], tempImg.data[k - 1].b);
			}
			if ((k + 1) % (width) != 0) {
				rgbTemp[0] = max(rgbTemp[0], tempImg.data[k + 1].r);
				rgbTemp[1] = max(rgbTemp[1], tempImg.data[k + 1].g);
				rgbTemp[2] = max(rgbTemp[2], tempImg.data[k + 1].b);
			}
			if (k + width - 1 < height*width) {
				rgbTemp[0] = max(rgbTemp[0], tempImg.data[k + width - 1].r);
				rgbTemp[1] = max(rgbTemp[1], tempImg.data[k + width - 1].g);
				rgbTemp[2] = max(rgbTemp[2], tempImg.data[k + width - 1].b);
			}
			if (k + width < height*width) {
				rgbTemp[0] = max(rgbTemp[0], tempImg.data[k + width].r);
				rgbTemp[1] = max(rgbTemp[1], tempImg.data[k + width].g);
				rgbTemp[2] = max(rgbTemp[2], tempImg.data[k + width].b);
			}
			if (k + width + 1 < height*width) {
				rgbTemp[0] = max(rgbTemp[0], tempImg.data[k + width + 1].r);
				rgbTemp[1] = max(rgbTemp[1], tempImg.data[k + width + 1].g);
				rgbTemp[2] = max(rgbTemp[2], tempImg.data[k + width + 1].b);
			}
			img[k].r = rgbTemp[0];
			img[k].g = rgbTemp[1];
			img[k].b = rgbTemp[2];
			memset(rgbTemp, 0, sizeof(rgbTemp)); //reset temp
		}
	}
	return img; //make displayImg the processed image
}

pixel *makeMin(pixel *img, int width, int height) {
	glob tempImg;
	int k = 0;
	int rgbTemp[3] = { 0, 0, 0 };
	tempImg.height = height;
	tempImg.width = width;
	tempImg.data = (pixel*)malloc(sizeof(pixel)*width*height);
	memcpy(tempImg.data, img, sizeof(pixel)*width*height);
	//checks minimum channel values for surrounding 8 pixels
	for (int i = 0; i < width; i++) {
		for (int j = 0; j < height; j++, k++) {
			/* I wrote comments from bottom to top, so see the convolution filter
			* for explanation of what I'm doing here, I'll comment differences */
			rgbTemp[0] = tempImg.data[k].r;
			rgbTemp[1] = tempImg.data[k].g;
			rgbTemp[2] = tempImg.data[k].b;
			if (k - width - 1 >= 0) {
				//finds minimum values
				rgbTemp[0] = min(rgbTemp[0], tempImg.data[k - width - 1].r);
				rgbTemp[1] = min(rgbTemp[1], tempImg.data[k - width - 1].g);
				rgbTemp[2] = min(rgbTemp[2], tempImg.data[k - width - 1].b);
			}
			if (k - width >= 0) {
				rgbTemp[0] = min(rgbTemp[0], tempImg.data[k - width].r);
				rgbTemp[1] = min(rgbTemp[1], tempImg.data[k - width].g);
				rgbTemp[2] = min(rgbTemp[2], tempImg.data[k - width].b);
			}
			if (k - width + 1 >= 0) {
				rgbTemp[0] = min(rgbTemp[0], tempImg.data[k - width + 1].r);
				rgbTemp[1] = min(rgbTemp[1], tempImg.data[k - width + 1].g);
				rgbTemp[2] = min(rgbTemp[2], tempImg.data[k - width + 1].b);
			}
			if ((k - 1) % width != 0) {
				rgbTemp[0] = min(rgbTemp[0], tempImg.data[k - 1].r);
				rgbTemp[1] = min(rgbTemp[1], tempImg.data[k - 1].g);
				rgbTemp[2] = min(rgbTemp[2], tempImg.data[k - 1].b);
			}
			if ((k + 1) % (width) != 0) {
				rgbTemp[0] = min(rgbTemp[0], tempImg.data[k + 1].r);
				rgbTemp[1] = min(rgbTemp[1], tempImg.data[k + 1].g);
				rgbTemp[2] = min(rgbTemp[2], tempImg.data[k + 1].b);
			}
			if (k + width - 1 < height*width) {
				rgbTemp[0] = min(rgbTemp[0], tempImg.data[k + width - 1].r);
				rgbTemp[1] = min(rgbTemp[1], tempImg.data[k + width - 1].g);
				rgbTemp[2] = min(rgbTemp[2], tempImg.data[k + width - 1].b);
			}
			if (k + width < height*width) {
				rgbTemp[0] = min(rgbTemp[0], tempImg.data[k + width].r);
				rgbTemp[1] = min(rgbTemp[1], tempImg.data[k + width].g);
				rgbTemp[2] = min(rgbTemp[2], tempImg.data[k + width].b);
			}
			if (k + width + 1 < height*width) {
				rgbTemp[0] = min(rgbTemp[0], tempImg.data[k + width + 1].r);
				rgbTemp[1] = min(rgbTemp[1], tempImg.data[k + width + 1].g);
				rgbTemp[2] = min(rgbTemp[2], tempImg.data[k + width + 1].b);
			}
			img[k].r = rgbTemp[0];
			img[k].g = rgbTemp[1];
			img[k].b = rgbTemp[2];
			memset(rgbTemp, 0, sizeof(rgbTemp)); //reset temp
		}
	}
	return img; //make displayImg the processed image
}

/* intensifies a given channel as
* determined by a modifier */
pixel *makeIntensify(pixel *img, int width, int height, char val) {
	int k = 0;
	for (int i = 0; i < width; i++) {
		for (int j = 0; j < height; j++, k++) {
			if (val == 'R') { //if red selected
				img[k].r = min(255, img[k].r*1.15); //min to avoid exceeding the 255 limit
			}
			else if (val == 'G') { //if green
				img[k].g = min(255, img[k].g * 1.15);
			}
			else { //if blue
				img[k].b = min(255, img[k].b * 1.15);
			}
		}
	}
	return img; //make displayImg the processed image
}

/* applies a matrix/mask onto each pixel
* I'm not sure why but edge detection does nothing, the
* image remains the same despite the mask being applied */
pixel *makeConvolute(pixel *img, int width, int height, char type[]) {
	static int matrices[5][9] = {
		{ 1, 2, 1, 0, 0, 0, -1, -2, -1 },
		{ 1, 0, -1, 2, 0, -2, 1, 0, -1 },
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1 },
		{ 1, 2, 1, 2, 4, 2, 1, 2, 1 },
		{ 0, -1, 0, -1, 5, -1, 0, -1, 0 }
	};
	int *matrix;
	if (type == "hor") { //if Sobel Horizontal
		img = makeGrey(img, width, height);
		matrix = matrices[0];
	}
	else if (type == "vert") { //if Sobel Vertical
		img = makeGrey(img, width, height);
		matrix = matrices[1];
	}
	else if (type == "gauss") { //custom, guassian blurrer
		matrix = matrices[3];
	}
	else if (type == "sharp") { //custom, sharpener
		matrix = matrices[4];
	}
	else { //else just blur
		matrix = matrices[2];
	}
	glob tempImg; //work buffer
	int k = 0;
	int l = 1;
	int rgbTemp[3] = { 0, 0, 0 };
	//below copies values into work buffer
	tempImg.height = height;
	tempImg.width = width;
	tempImg.data = (pixel*)malloc(sizeof(pixel)*width*height);
	memcpy(tempImg.data, img, sizeof(pixel)*width*height);
	for (int i = 0; i < width; i++) {
		for (int j = 0; j < height; j++, k++, l = 0) {
			/* there's a lot of repeated code, but it multiplies
			* the RGB values by the value in the kernal, incrementing
			* l by that value. It's organized in directions */
			l += matrix[4]; //initial position k needs to be counted, too
			rgbTemp[0] += tempImg.data[k].r*matrix[4];
			rgbTemp[1] += tempImg.data[k].g*matrix[4];
			rgbTemp[2] += tempImg.data[k].b*matrix[4];
			if (k - width - 1 >= 0) { //top-left
				l += matrix[0];
				rgbTemp[0] += tempImg.data[k - width - 1].r*matrix[0];
				rgbTemp[1] += tempImg.data[k - width - 1].g*matrix[0];
				rgbTemp[2] += tempImg.data[k - width - 1].b*matrix[0];
			}
			if (k - width >= 0) { //above
				l += matrix[1];
				rgbTemp[0] += tempImg.data[k - width].r*matrix[1];
				rgbTemp[1] += tempImg.data[k - width].g*matrix[1];
				rgbTemp[2] += tempImg.data[k - width].b*matrix[1];
			}
			if (k - width + 1 >= 0) { //top-right
				l += matrix[2];
				rgbTemp[0] += tempImg.data[k - width + 1].r*matrix[2];
				rgbTemp[1] += tempImg.data[k - width + 1].g*matrix[2];
				rgbTemp[2] += tempImg.data[k - width + 1].b*matrix[2];
			}
			if ((k - 1) % width != 0) { //left
				l += matrix[3];
				rgbTemp[0] += tempImg.data[k - 1].r*matrix[3];
				rgbTemp[1] += tempImg.data[k - 1].g*matrix[3];
				rgbTemp[2] += tempImg.data[k - 1].b*matrix[3];
			}
			if ((k + 1) % (width) != 0) { //right
				l += matrix[5];
				rgbTemp[0] += tempImg.data[k + 1].r*matrix[5];
				rgbTemp[1] += tempImg.data[k + 1].g*matrix[5];
				rgbTemp[2] += tempImg.data[k + 1].b*matrix[5];
			}
			if (k + width - 1 < height*width) { //bottom-left
				l += matrix[6];
				rgbTemp[0] += tempImg.data[k + width - 1].r*matrix[6];
				rgbTemp[1] += tempImg.data[k + width - 1].g*matrix[6];
				rgbTemp[2] += tempImg.data[k + width - 1].b*matrix[6];
			}
			if (k + width < height*width) { //below
				l += matrix[7];
				rgbTemp[0] += tempImg.data[k + width].r*matrix[7];
				rgbTemp[1] += tempImg.data[k + width].g*matrix[7];
				rgbTemp[2] += tempImg.data[k + width].b*matrix[7];
			}
			if (k + width + 1 < height*width) { //bottom-right
				l += matrix[8];
				rgbTemp[0] += tempImg.data[k + width + 1].r*matrix[8];
				rgbTemp[1] += tempImg.data[k + width + 1].g*matrix[8];
				rgbTemp[2] += tempImg.data[k + width + 1].b*matrix[8];
			}
			img[k].r = max(0, min(255, rgbTemp[0] / l)); //then divide by sum of matrix values
			img[k].g = max(0, min(255, rgbTemp[1] / l)); //both min and max used to remain
			img[k].b = max(0, min(255, rgbTemp[2] / l)); //between 0-255
			memset(rgbTemp, 0, sizeof(rgbTemp)); //reset temp
		}
	}
	return img; //make displayImg the processed image
}

/* goes over image comparing each pixel to nine
* other colors, mapping that color to the pixel if
* it's closest to that (using Euclidean Distance */
pixel *makeQuantize(pixel* img, int width, int height, char type[]) {
	srand(time(NULL)); //seed for random
	int k = 0; //kth pixel
	int highestIndex = 0; //the index of the highest value
	int col[3] = { 0, 0, 0 }; //placeholder to put temp color values
	int vals[9][3] = {
		{ 255, 0, 0 }, { 0, 255, 0 }, { 0, 0, 255 }, //red, green, blue
		{ 255, 255, 255 }, { 0, 0, 0 }, { 128, 128, 128 }, //black, white, grey
		{ 128, 128, 0 }, { 0, 128, 128 }, { 128, 0, 128 } //combos of RGB
	};
	if (type == "rand") { //if random modifier
		for (int i = 0; i < 9; i++) { //9 colors
			for (int j = 0; j < 3; j++) { //3 channels per color
				vals[i][j] = rand() % 255; //random value 0-255 per channel
			}
		}
	}
	for (int i = 0; i < width; i++) {
		for (int j = 0; j < height; j++, k++) {
			col[0] = img[k].r;
			col[1] = img[k].g;
			col[2] = img[k].b;
			highestIndex = checkCloser(col, vals); //find index of closest color
			img[k].r = vals[highestIndex][0]; //replace channel with highest of palette
			img[k].g = vals[highestIndex][1];
			img[k].b = vals[highestIndex][2];
		}
	}
	return img; //make displayImg the processed image
}

/* used in makeQuantize to find whichever
* color in the palette is closest to the inspected
* pixel. It returns the index of the closest color */
int checkCloser(int col[3], int rgbVals[9][3]) {
	int highMatch[9] = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	int r, g, b;
	int euDist;
	float squareA, squareB;
	int tempHighest = 0; //temporary highest
	int highestIndex = 0; //index of highest
	for (int i = 0; i < 9; i++) {
		r = rgbVals[i][0];
		g = rgbVals[i][1];
		b = rgbVals[i][2];
		//distance in RGB-space
		squareA = (col[0] - r) * (col[0]-r);
		squareB = (col[2] - b) * (col[2] - b);
		euDist = (int)sqrt(squareA + squareB);
		highMatch[i] = euDist; //insert potential highest values into array
	}
	//finds the highest value and the index of the highest value
	for (int i = 0; i < 9; i++) {
		if (tempHighest < highMatch[i]) {
			tempHighest = highMatch[i];
			highestIndex = i;
		}
	}
	return highestIndex; //return the index of the closest match
}

/* inverts each pixel to the opposite color
* opposite meaning |rgb-255| */
pixel *makeNegative(pixel *img, int width, int height) {
	int k = 0;
	for (int i = 0; i < width; i++) {
		for (int j = 0; j < height; j++, k++) {
			//find absolute value of the negative of ea. channel
			img[k].r = abs(img[k].r - 255);
			img[k].g = abs(img[k].g - 255);
			img[k].b = abs(img[k].b - 255);
		}
	}
	return img; //make displayImg the processed image
}

/* applies a sepia filter to the image */
pixel *makeSepia(pixel *img, int width, int height) {
	int k = 0;
	for (int i = 0; i < width; i++) {
		for (int j = 0; j < height; j++, k++) {
			//Sepia values suggested by Microsoft, looks a bit bright however
			img[k].r = min(255, (img[k].r*0.393) + (img[k].g*0.769) + (img[k].b*0.189));
			img[k].g = min(255, (img[k].r*0.349) + (img[k].g*0.686) + (img[k].b*0.168));
			img[k].b = min(255, (img[k].r*0.272) + (img[k].g*0.534) + (img[k].b*0.131));
		}
	}
	return img; //make displayImg the processed image
}

/* menu with keyboard controls */
void menu(unsigned char key, int x, int y) {
	switch (key) {
	case 'q': { //quit
				  free(displayImg.data);
				  free(originalImg.data);
				  exit(0);
				  break;
	}
	case 'r': { //Reset
				  free(displayImg.data);
				  size_t size = sizeof(pixel)*displayImg.height*displayImg.width;
				  displayImg.data = (pixel*)malloc(size);
				  memcpy(displayImg.data, originalImg.data, size);
				  glutPostRedisplay();
				  break;
	}
	case 's': {
				  saveImage("backup.tif", displayImg.data, displayImg.width, displayImg.height);
				  break;
	}
	case '1': { //Greyscale Filter
				  displayImg.data = makeGrey(displayImg.data, displayImg.width, displayImg.height);
				  glutPostRedisplay();
				  break;
	}
	case '2': { //NTSC Filter
				  displayImg.data = makeNTSC(displayImg.data, displayImg.width, displayImg.height);
				  glutPostRedisplay();
				  break;
	}
	case '3': { //Monochrome Filter
				  displayImg.data = makeMonochrome(displayImg.data, displayImg.width, displayImg.height);
				  glutPostRedisplay();
				  break;
	}
	case '4': { //Channel Swap Filter
				  displayImg.data = makeSwapped(displayImg.data, displayImg.width, displayImg.height);
				  glutPostRedisplay();
				  break;
	}
	case '5': { //Red Channel
				  displayImg.data = makeSingleChannel(displayImg.data, displayImg.width, displayImg.height, 'R');
				  glutPostRedisplay();
				  break;
	}
	case '6': { //Green Channel
				  displayImg.data = makeSingleChannel(displayImg.data, displayImg.width, displayImg.height, 'G');
				  glutPostRedisplay();
				  break;
	}
	case '7': { //Blue Channel
				  displayImg.data = makeSingleChannel(displayImg.data, displayImg.width, displayImg.height, 'B');
				  glutPostRedisplay();
				  break;
	}
	case '8': { //Max
				  displayImg.data = makeMax(displayImg.data, displayImg.width, displayImg.height);
				  glutPostRedisplay();
				  break;
	}
	case '9': { //Min
				  displayImg.data = makeMin(displayImg.data, displayImg.width, displayImg.height);
				  glutPostRedisplay();
				  break;
	}
	case '0': { //Intensify R channel
				  displayImg.data = makeIntensify(displayImg.data, displayImg.width, displayImg.height, 'R');
				  glutPostRedisplay();
				  break;
	}
	case 'a': { //Intensify G channel
				  displayImg.data = makeIntensify(displayImg.data, displayImg.width, displayImg.height, 'G');
				  glutPostRedisplay();
				  break;
	}
	case 'b': { //Intensify B channel
				  displayImg.data = makeIntensify(displayImg.data, displayImg.width, displayImg.height, 'B');
				  glutPostRedisplay();
				  break;
	}
	case 'c': { //Average (Blur)
				  displayImg.data = makeConvolute(displayImg.data, displayImg.width, displayImg.height, "");
				  glutPostRedisplay();
				  break;
	}
	case 'd': { //Sobel Horizontal
				  displayImg.data = makeConvolute(displayImg.data, displayImg.width, displayImg.height, "hor");
				  glutPostRedisplay();
				  break;
	}
	case 'e': { //Sobel Vertical
				  displayImg.data = makeConvolute(displayImg.data, displayImg.width, displayImg.height, "vert");
				  glutPostRedisplay();
				  break;
	}
	case 'f': { //Gaussian Blur
				  displayImg.data = makeConvolute(displayImg.data, displayImg.width, displayImg.height, "gauss");
				  glutPostRedisplay();
				  break;
	}
	case 'g': { //Sharpen
				  displayImg.data = makeConvolute(displayImg.data, displayImg.width, displayImg.height, "sharp");
				  glutPostRedisplay();
				  break;
	}
	case 'h': { //Quantize (Fixed)
				  displayImg.data = makeQuantize(displayImg.data, displayImg.width, displayImg.height, "");
				  glutPostRedisplay();
				  break;
	}
	case 'i': { //Quantize (Random)
				  displayImg.data = makeQuantize(displayImg.data, displayImg.width, displayImg.height, "rand");
				  glutPostRedisplay();
				  break;
	}
	case 'j': { //Invert Colors
				  displayImg.data = makeNegative(displayImg.data, displayImg.width, displayImg.height);
				  glutPostRedisplay();
				  break;
	}
	case 'k': { //Sepia Filter
				  displayImg.data = makeSepia(displayImg.data, displayImg.width, displayImg.height);
				  glutPostRedisplay();
				  break;
	}
	}
}

/* puts displayImg on canvas for initial conditions */
void display_image(void) {
	glDrawPixels(displayImg.width, displayImg.height, GL_RGB, GL_UNSIGNED_BYTE, (GLubyte*)displayImg.data);
	glFlush();
}

/* method copies originalImg into displayImg */
glob copyDisplay(glob img) {
	glob tempImg;
	tempImg.height = img.height;
	tempImg.width = img.width;
	tempImg.data = (pixel*)malloc(sizeof(pixel)*img.width*img.height);
	memcpy(tempImg.data, img.data, sizeof(pixel)*img.width*img.height);
	return tempImg;
}

int main(int argc, char** argv) {
	originalImg.data = imageLoader("image.tif", &originalImg.width, &originalImg.height);
	displayImg = copyDisplay(originalImg);
	printf("Q: Quit\tR: Reset\t S: Save\n\n");
	printf("Display\n");
	printf("1: Greyscale Filter\t2: NTSC Filter\t3: Monochrome Filter\n");
	printf("4: Channel Swap Filter\t5: Pure R Filter\t6: Pure G Filter\n");
	printf("7: Pure B Filter\n\n");
	printf("Basic Channel Filters\n");
	printf("8: Max Filter\t9: Min Filter\t0: Intensify R\n");
	printf("a: Intensify G\tb: Intensify B\n\n");
	printf("Convolution Filters\n");
	printf("c: Average\td: Sobel horizontal\te: Sobel vertcial\n");
	printf("f: Gaussian Blur (Custom 1)\tg: Sharpen (Custom 2)\n\n");
	printf("Quantize Filters\n");
	printf("h: Fixed RGB\ti: Random RGB\n\n");
	printf("Custom Filters\n");
	printf("j: Image Negative\tk: Sepia Filter\n\n");
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_RGB | GLUT_SINGLE);
	glutInitWindowSize(displayImg.width, displayImg.height);
	glutCreateWindow("3P98 Assign1");
	glutKeyboardFunc(menu);
	glutDisplayFunc(display_image);
	glutMainLoop();
	return 0;
}