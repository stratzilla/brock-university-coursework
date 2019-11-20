; this program counts the instances of positive
; and negative numbers in a list of integers,
; as well as the sum of negatives and sum of positives
;
; r2, r3 are counters for instances of +/-
; r4, r5 are sums for +/-
; r6 is the address for the integers
; r1 is the index within r6 for which pos/neg is checked
;
	.orig 	x3000		; starting address
	and 	r2, r2, #0	; counter for positives
	and 	r3, r3, #0	; counter for negatives
	and 	r4, r4, #0	; sum of positives
	and 	r5, r5, #0	; sum of negatives
	lea 	r6, data	; load data into address
loop	ldr 	r1, r6, #0	; load r6 value into r1
	brz	end		; if zero, end
	brn	neg		; if negative, do neg loop
	brp	pos		; if positive, do pos loop
neg	add	r3, r3, #1	; increment neg count
	add	r5, r5, r1	; sum of sum and integer
	br	next		; go to next value
pos	add	r2, r2, #1	; increment pos count
	add	r4, r4, r1	; sum of sum and integer
	br	next		; go to next value
next	add	r6, r6, #1	; shift to next data	
	br	loop		; loop for next value
end	trap 	x25		; halt
data 	.fill 	#123		; fill list with integers
	.fill	#-23
	.fill	#17
	.fill	#-9
	.fill	#1
	.fill	#0
	.end			; end of program