# include <stdio.h>

int main(void)
{
int var1, var2, var3;
printf("\nPlease input three integers...\n\n");
printf("#1... ");
scanf("%d",&var1);
printf("#2... ");
scanf("%d",&var2);
printf("#3... ");
scanf("%d",&var3);
proper(var1,var2,var3);
reverse(var1,var2,var3);
}

int proper(var1,var2,var3)
{
printf("\nHere are the integers in proper order: %d, %d, %d.",var1,var2,var3);
}

int reverse(var1,var2,var3)
{
printf("\nHere are the integers in reverse order: %d, %d, %d.\n\n",var3,var2,var1);
}