#include <stdio.h>

int* read(int array[])
{	
	int test1,test2,test3;
	printf("\nWhat are the three test scores?\n\n");
	printf("Test Score #1... ");
	scanf("%d",&test1);
	printf("Test Score #2... ");
	scanf("%d",&test2);
	printf("Test Score #3... ");
	scanf("%d",&test3);
	array[0] = test1;
	array[1] = test2;
	array[2] = test3;
	return array;
}

char calculations(int array[])
{
	float avg = ((array[0]+array[1]+array[2])/3);
	int avgtwo = ((array[1]+array[2])/2);
	char grade;
		if (avg >= 90)
			grade = 'A';
		else if ((avg >= 70) && (avg < 90) && (array[2] >= 90))
			grade = 'A';
		else if ((avg >= 70) && (avg < 90) && (array[2] < 90))
			grade = 'B';
		else if ((avg >= 50) && (avg < 70) && (avgtwo >= 70))
			grade = 'C';
		else if ((avg >= 50) && (avg < 70) && (avgtwo < 70))
			grade = 'D';
		else {
			grade = 'F';
		}
}

void print(char grade)
{
	printf("\nThe grade is %s.",grade);
	printf("\nI've been trying to get this to work for four hours, and despite help from the TA and the COSC help office, none of us can get it to output properly. I've traced my code for literally hours, and it's getting to the point where my patience has run dry. I hope I will get at least some marks for my effort and source code, which admittedly doesn't work. Looks fine to me, no clue why it doesn't.");
}

int main(void)
{
	int array[3];
	char grade = 'A';
	read(array);
	grade = calculations(array);
	print(grade);
	return 0;
}