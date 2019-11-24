# Draw a figure made of lines and circles using Bresenham drawing algorithms.

main:
   # Circle(30,100,20) #head
	# Line(30,80,30,30) #body
   addi $a0, $0, 30
   addi $a1, $0, 80
   addi $a2, $0, 30
   addi $a3, $0, 30
   jal Line

	# Line(20,1,30,30) #left leg
   addi $a0, $0, 20
   addi $a1, $0, 1
   addi $a2, $0, 30
   addi $a3, $0, 30
   jal Line

	# Line(40,1,30,30) #right leg
   addi $a0, $0, 40
   addi $a1, $0, 1
   addi $a2, $0, 30
   addi $a3, $0, 30
   jal Line

	# Line(15,60,30,50) #left arm
   addi $a0, $0, 15
   addi $a1, $0, 60
   addi $a2, $0, 30
   addi $a3, $0, 50
   jal Line

	# Line(30,50,45,60) #right arm
   addi $a0, $0, 30
   addi $a1, $0, 50
   addi $a2, $0, 45
   addi $a3, $0, 60
   jal Line

	# Circle(24,105,3) #left eye
	# Circle(36,105,3) #right eye
	# Line(25,90,35,90) #mouth center
   addi $a0, $0, 25
   addi $a1, $0, 90
   addi $a2, $0, 35
   addi $a3, $0, 50
   jal Line

	# Line(25,90,20,95) #mouth left
   addi $a0, $0, 30
   addi $a1, $0, 80
   addi $a2, $0, 30
   addi $a3, $0, 30
   jal Line

	# Line(35,90,40,95) #mouth right
   addi $a0, $0, 30
   addi $a1, $0, 80
   addi $a2, $0, 30
   addi $a3, $0, 30
   jal Line

   j veryEnd               # we're done :)



plot: # store x ($a0) and y ($a1) in data memory, y after x
   addi $sp, $sp, 8        # TODO: check if +- sp first...
   sw $a0, -4($sp)         # store x (lower mem)
   sw $a1, 0($sp)          # store y (higher mem)
   jr $ra

swap: # swap two values; a0 -> a1, a1 -> a0
   addi $t0, $a0, 0
   addi $a0, $a1, 0
   addi $a1, $t0, 0
   jr $ra

absDiff: # return (in $v0) the pos difference of two numbers (in $a0, $a1)
   sub $t0, $a0, $a1
   sub $t1, $a1, $a0
   slt $t2, $t0, $t1          # check: $t0 < $t1? (one will be neg, or both 0)
   beq $t2, $0, secondPos
   add $v0, $t1, $0           # if t0 is < t1, return t1
   jr $ra
secondPos: add $v0, $t0, $0   # else (t0>t1), return t0
   jr $ra



Line: # a0->s0 = x0, a1->s1 = y0, a2->s2 = x1, a3->s3 = y1
   # store all params in saved registers
   add $s0, $a0, $0        # x0
   add $s1, $a1, $0        # y0
   add $s2, $a2, $0        # x1
   add $s3, $a3, $0        # y1

   # save $ra NOT on in DM bc plot() will use DM later; restore at very end of this fnxn
   add $s7, $ra, $0

   # abs(y1 - y0)
   add $a0, $s3, $0
   add $a1, $s1, $0
   jal absDiff
   add $s4, $v0, $0        # save result: abs(y1 - y0) -> $s4

   # abs(x1 - x0)
   add $a0, $s2, $0
   add $a1, $s0, $0
   jal absDiff
   add $s5, $v0, $0        # save result: abs(x1 - x0) -> $s5

   # ST IN $s4 --> DON'T OVERWRITE
   slt $s4, $s5, $s4       # if abs(x1 - x0) < abs(y1 - y0) {st = 1; else st = 0;}

   # if st == 0 (i.e. != 1), skip, else (i.e. st == 1) 2 swaps
   beq $s4, $0, skip1
   add $a0, $s0, $0        # load x0, y0 into 1st 2 arg regs
   add $a1, $s1, $0
   jal swap
   add $a0, $s2, $0        # load x1, y1 into 1st 2 arg regs
   add $a1, $s3, $0
   jal swap

# if x1 < x0, 2 swaps, else skip
skip1: slt $t0, $s2, $s0   # if x1 >= x0 -> $t0 = 0
   beq $t0, $0, skip2      # if false ($t0 = 0), skip
   add $a0, $s0, $0        # load x0, x1 into 1st 2 arg regs
   add $a1, $s2, $0
   jal swap
   add $a0, $s1, $0        # load y0, y1 into 1st 2 arg regs
   add $a1, $s3, $0
   jal swap

skip2: # delta stuff
   sub $s5, $s2, $s0       # DELTAX NOW IN $S5
   add $a0, $s3, $0        # deltay = abs(y1 - y0)
   add $a1, $s1, $0
   jal absDiff
   add $s6, $v0, $0        # save result: DELTAY NOW IN $S6

   # NOTE: can now use $t0, $t1, $t2 freely bc done calling absDiff and swap

   add $t0, $0, $0         # clear $t0 = ERROR -> DON'T OVERWRITE
   add $t1, $s1, $0        # Y = y0 -> DON'T OVERWRITE

   # if y0 < y1 {ystep = 1; else ystep = -1;}
   slt $t2, $s1, $s3       # if true, $t2 = YSTEP = 1, so done -> skip set ystep to -1
   bne $t2, $0, skip3
   addi $t2, $0, -1        # set YSTEP ($t2) to -1 if not y0 < y1

skip3: add $t3, $s0, $0    # X = x0 -> DON'T OVERWRITE
loop: slt $t5, $t3, $t4    # check loop condition: (x <= x1)  -->  !(x > x1) --> !(x1 < x)
   bne $t5, $0, done       # checked (x1 < x); keep looping if !(x1 < x), i.e. $t5 = 0; done if true, i.e. $t5 == 1 != 0

   # if st == 1 {plot(y,x); else plot(x,y);}
   beq $s4, $0, not1       # only 2 options for st: 0, 1; so if 0, then !=1 -> skip ==1 stuff
   add $a0, $t1, $0        # put Y in $a0
   add $a1, $t3, $0        # put X in $a1
   jal plot                # plot (y,x)
   j next                  # so don't plot st==1 stuff too
not1: add $a0, $t3, $0     # put X in $a0
   add $a1, $t1, $0        # put Y in $a1
   jal plot                # plot (x,y)

next: add $t0, $t0, $s6    # error = error + deltay
   # if (deltax <= 2*error)  -->  !(deltax > 2*error)  -->  !(2*error < deltax)
   add $t5, $t0, $t0       # error + error = 2*error
   slt $t5, $t5, $s5       # 2*err < dx?
   bne $t5, $0, loop       # do next if NOT (2*err < dx), skip if true (==1) --> skip if !=0
   add $t1, $t1, $t2       # y += ystep
   sub $t0, $t0, $s5       # error -= deltax
   j loop

done: add $ra, $s7, $0        # restore $ra
   jr $ra



Circle:
   jr $ra



veryEnd: add $0, $0, $0
