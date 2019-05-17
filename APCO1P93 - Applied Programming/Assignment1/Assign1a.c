#include <stdio.h>

int main (void)
{
	int myInt;
	int d;
	printf("\nWhat is the temperature in degrees Fahrenheit?\n\n");
	scanf("%d",&d);
	myInt = (d - 32) * 0.55555556;
	printf("\n (d - 32) * (5 / 9) = %d", myInt);
	printf(" degrees Celsius\n\n");	

	return 0;
}