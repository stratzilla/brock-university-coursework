#include <stdio.h>
#include <math.h>

int main() {
	int pr,ny,nm;
	float mp,iy,im,p,q;
	printf("\nWhat is the amount of the loan?\n");
	scanf("%d",&pr);
	printf("\nWhat is the interest rate per year? (exclude percent symbol)\n");
	scanf("%f",&iy);
	printf("\nWhat is the number of years to pay back the loan?\n");
	scanf("%d",&ny);
	nm = ny*12;
	im = iy/12;
	p = pow(1+im,nm);
	q = p/(p-1);
	mp = pr*im*q;
	printf("\nThe amount of the loan: %d",pr);
	printf("\nInterest rate per year: %.2f",iy);
	printf("\nInterest rate per month: %f",im);
	printf("\nNumber of months: %d\tMonthly payment: %.2f",nm,mp);
	printf("\n\n%f,%f",p,q); }