				;
				; *******************************************
				;       	SKELETON: HEXDUMP.
				; *******************************************
				;



	.model  tiny            ; Data and code fit in one 64K segment
	.stack                  ; allocate a stack
	.code                   ; start of code segment



				;
				; MAIN PROGRAM PART
				;


	CR      equ     13      ; useful constant values
	NL      equ     10
	TAB     equ     9
	BIOS    equ     21h
	PutChar equ     02h
	Stop    equ     4c00h


	
	push    testdata2       ; example call to PutHexDump   
	push    after_end_of_testdata2 - 1
	call    PutHexDump
	add     sp,4

	mov     ax,Stop         ; return control to DOS with errorlevel 0
	int     BIOS




				;
				; PROCEDURES
				;



PutHexDump:

	push ax			;
	push bx			; saving the registers
	push cx			;
	push dx			;
	push si			;

	push bp			; save the base pointer
	mov bp,sp		; set base pointer to current stack pointer(i.e. beginning of next stack)
	mov cx,[bp+16]		; set cx to the start address

contents:


	cmp cx,[bp+14]		; test if the current address is greater than the finish address
	jg endcont		; if so, exit the method

	
	mov dl,ch		; copy high half of cx to dl
	push dx			; moves the high half of cx to stack
	call PutByteAsHex
	add sp,2

	push cx			; pushes ax onto the stack (only the lower half is passed as a parameter)
	call PutByteAsHex
	add sp,2


	mov dl,TAB		;
	mov ah,PutChar		; prints TAB
	int BIOS		;

	mov si,cx		; copy the address to si
	mov bl,[si]		; copying memory contents given by cx to register bl
	push bx			; suppy the memory contents to the printing method

	call PutByteAsHex	; print the contents of the given address
	add sp,2		; destroy parameter supplied to called method
	
	mov dl,CR		;
	mov ah,PutChar		; prints a carriage return
	int BIOS		;

	mov dl,NL		;
	mov ah,PutChar		; prints a newline
	int BIOS		;

	add cx,1		;
	

	jmp contents		;

endcont:

	pop bp			; 
	pop dx			;
	pop cx			; restore all registers
	pop bx			;
	pop ax			;
				
	ret




PutByteAsHex:
				;
				; procedure PutByteAsHex (b : byte)
				; pre:  the byte is on the stack, as the
				;       lower half of a pushed word
				; post: puts the byte on the standard output 
				;       in hex, as two hex digits.
				;

	push    ax              ; save registers used as scratchpads
	push    bp		
	push    cx		
	push    dx		

				;
				; copy the parameter into register dl,
				; for convenience in BIOS calls etc.
				;
	mov     bp,sp
	mov     dl,[bp+10]

	mov     dh,dl           ; take copy of dl in dh
	shr     dl,4            ; high nibble...
	cmp     dl,9
	jg      high_nibble_is_letter
high_nibble_is_number:        
	add     dl,'0'
	mov     ah,PutChar
	int     BIOS

	jmp     high_nibble_done
high_nibble_is_letter:
	add     dl,'A'-10
	mov     ah,PutChar
	int     BIOS
high_nibble_done:

	mov     cl,dh           ; ...and low nibble.
	shr     cl,4
	shl     cl,4     
	
	mov     dl,dh
	sub     dl,cl
	cmp     dl,9
	jg      low_nibble_is_letter
low_nibble_is_number:        
	add     dl,'0'
	mov     ah,PutChar
	int     BIOS

	jmp     low_nibble_done
low_nibble_is_letter:
	add     dl,'A'-10
	mov     ah,PutChar
	int     BIOS
low_nibble_done:
	pop     dx               ; restore registers and return.
	pop     cx
	pop     bp
	pop     ax
	ret




	.data
				;
				; A string of ASCII characters.
				;
testdata1:
	db      "The cat sat on the mat"
after_end_of_testdata1:

				;
				; Some typical data in hex.
				; The extra leading zeroes in the values
				; that begin with a letter are just a
				; feature of A86 syntax.
				; That is, "0DDh" means DD hex, etc.
				;
testdata2:
	db      9Ch,2Bh,0DDh,07h,0FEh,27h,42h
after_end_of_testdata2:

				;
				; Hex values of interest when considering
				; things like whether a hex digit should 
				; be a number (0-9) or a letter (A-F), etc.
testdata3:
	db      00h,09h,0Ah,0Fh,10h,9Fh,0A0h,0FFh
after_end_of_testdata3:




				;
				; You can invent scratchpad global variables
				; here if you need them: "unused1" below 
				; is an example of a global of type byte.
				; (You are unlikely to need any scratchpad
				; globals in this exercise.)
				;

unused1 db      ?               ; uninitialized byte variable "unused1"



	end
