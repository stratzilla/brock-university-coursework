#include <stdio.h>

int main (void)

{
	int myInt;
	float myFloat;
	int clipboards,whistles,pylons,soccerballs;
	float clip_price = 3.99;
	float whis_price = 6.99;
	float pylo_price = 1.99;
	float socc_price = 17.99;
	printf("\nHow many clipboards? ");
	scanf("%d",&clipboards);
	printf("How many whistles? ");
	scanf("%d",&whistles);
	printf("How many pylons? ");
	scanf("%d",&pylons);
	printf("How many soccerballs? ");
	scanf("%d",&soccerballs);
	float cliptotal = clip_price*clipboards;
	float whistotal = whis_price*whistles;
	float pylototal = pylo_price*pylons;
	float socctotal = socc_price*soccerballs;
	float subtotal = cliptotal+whistotal+pylototal+socctotal;
	float gst = subtotal*0.05;
	float pst = subtotal*0.08;
	float grand_total = subtotal+gst+pst;
	printf("\nitem		qty	unit price			total price\n");
	printf("====================================================================\n");
	printf("clipboards	%d	$%.2f				$%.2f", clipboards,clip_price,cliptotal);
	printf("\nwhistles	%d	$%.2f				$%.2f", whistles,whis_price,whistotal);
	printf("\npylons		%d	$%.2f				$%.2f", pylons,pylo_price,pylototal);
	printf("\nsoccer balls	%d	$%.2f				$%.2f", soccerballs,socc_price,socctotal);
	printf("\n====================================================================");
	printf("\n			subtotal			$%.2f",subtotal);
	printf("\n			5 perc pst			$%.2f",gst);
	printf("\n			8 perc gst			$%.2f",pst);
	printf("\n			grand total			$%.2f\n\n",grand_total);

	return 0;
}