#include <stdio.h>
#include <time.h>
#include <stdlib.h>

int compare (const void*a,const void*b) {
	return (*(int*)a-*(int*)b); }
int grid[10][10];

int main() {
	int t,i,j,m,n,rnd;
	float per,suc,ser;
	n = 0;
	suc = 0;
	rnd = 0;
	int count =0;
	printf("\nHere is the non-sorted array:\n");
	srand(time(NULL));
		for (i=0;i<10;i++) {
			for (j=0;j<10;j++) {
				grid[i][j] = (rand()%100+1);
				printf("%4d",grid[i][j]); } 
			printf("\n"); }
	printf("\nHere is the sorted array:\n");	
	qsort(grid,100,sizeof(int),compare);
		for (i=0;i<10;i++) {
			for (j=0;j<10;j++) {
				printf("%4d",grid[i][j]); } 
			printf("\n"); }
	printf("\n");
	for (t=0;t<200;t++) {
		rnd=(rand()%100+1);
		for (i=0;i<10;i++) {
			for (j=0;j<10;j++) {
			if (grid[i][j]==rnd) {
				suc++; }
			ser++; } } }
	per = (suc/ser);
	printf("Number of searches completed: %.0f\n",ser);
	printf("Number of successful searches: %.0f\n",suc);
	printf("Percentage of successful searches: %.2f %%\n",100*per); }