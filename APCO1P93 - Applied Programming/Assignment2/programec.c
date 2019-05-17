#include <stdio.h>

int main(void)
{
	char time,vehicle;
	int hours;
	int money = 0;
	printf("What time is it?\na)...between midnight and 9.\nb)...between 9am and 5pm.\nc)...between 5pm and midnight.\n");
	scanf("%s",&time);
	printf("\nHow long will you be parked?\n");
	scanf("%d",&hours);
	printf("\nDo you drive a truck? (Y or N)\n");
	scanf("%s",&vehicle);
		if (hours == 2)
			money+=8;
		else if (hours > 2)
			money+=11;
		else { 
			money+=3;
		}
		if (time == 'b')
			money+=2;
		else if (time == 'c')
			money-=1;
		else {
			money+=0;
		}
		if (vehicle == 'Y')
			money+=2;
		else {
			money+=0;
		}
	printf("\nYour total amount due is $%d.00",money);
}
