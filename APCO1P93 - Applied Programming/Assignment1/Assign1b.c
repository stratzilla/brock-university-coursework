#include <stdio.h>

int main (void)
{
	int myInt;
	int x,y,z;
	printf("\nWhat is the first number? ");
	scanf("%d",&x);
	printf("What is the second number? ");
	scanf("%d",&y);
	printf("What is your estimate? ");
	scanf("%d",&z);
	if (z==x*y)
	{
	myInt = x * y;
	printf("\n x * y = %d", myInt);
	printf("\n\nYou guessed correctly!\n");
	}
	else
	{
	myInt = x * y;
	printf("\n x * y = %d", myInt);
	printf("\n\nYou guessed incorrectly.\n");
	}	

	return 0;
}