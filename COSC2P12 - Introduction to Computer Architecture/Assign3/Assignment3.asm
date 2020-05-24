		.orig	x3000				; origininating address
		and		r0, r0, #0			; clear r0
		and		r1, r1, #0			; result in r1
		ld		r2, mulpnd			; multiplicand in r2
		ld		r3, mulplr			; multiplier in r3
		add		r4, r1, #1			; bit mask to test r3 bit
		add		r5, r1, #-1			; mask to determine end of r3
loop	and		r3, r3, r5			; test if end of r3
		brz		print				; if zero bits left
		and 	r6, r3, r4			; else, test the bit
		brz		shift				; if bit is zero, shift
		add 	r1, r1, r2			; add r2 to result
shift	add		r2, r2, r2			; shift r2 bits
		add		r5, r5, r5			; shift mask
		add		r4, r4, r4			; shift mask
		brnzp	loop				; redo algorithm
print	lea		r0, mess			; load message in r0
		puts						; then print it
		and		r0, r0, #0			; clear r0
		lea		r0, ascii			; ascii offset
		add		r0, r0, r1			; put result in r0
		out							; output result
		br		end					; then end program
end		trap	x25					; end algorithm
mulpnd	.fill	#2					; multiplicand
mulplr	.fill	#3					; multiplier
mess	.stringz "Product: "		; message
ascii	.fill	x30					; ascii offset
		.end						; halt