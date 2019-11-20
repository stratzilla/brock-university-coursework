		.orig		x3000				; starting address
main	lea			r1, sadd			; load string address
		and 		r2, r2, 0			; counter
		lea			r0, pr				; prompt user for string
		puts							; print prompt
		; continually takes characters from the user, only
		; stopping once a newline character has been returned.
		; if a newline has been found, go to endl and print
		; the necessary information
loop	getc							; take string
		out								; print string char-by-char
		add			r2, r2, 1			; increment counter
		str			r0, r1, #0			; store char into stringspace
		add			r1, r1, #1			; next address
		add			r0, r0, #-10		; confirm newline
		brnp		loop				; if nonzero, restart loop
		brz			endl				; if zero, end loop
		; prints the length of the string, a colon,
		; and then the string itself. If a string of zero length,
		; print "end of program" instead
endl	add			r2, r2, -1			; decrement counter
		add			r1, r1, #-1			; get rid of newline
		and			r0, r0, 0			; make r0 0
		add			r0, r0, r2			; add r2
		brz			term				; if zero then quit
		TRAP 		xFC					; print r2 counter
		ld			r0, ascol			; load ascii colon
		out								; print it
		and			r0, r0, #0			; reset r0
		str			r0, r1, #0			; store string in r0
		lea			r0, sadd			; load string address
		puts							; print string
		br			main				; repeat program
term	lea			r0, ep				; load ep into r0
		puts							; print ep
		halt							; end program
pr		.stringz	"\nEnter a string: "	; prompt for string
ep		.stringz	"End of Program"	; end of program
sadd	.blkw		100					; address space
asoff	.fill		48					; ascii offset +48
ascol	.fill		58					; ascii for colon is 58
		.end