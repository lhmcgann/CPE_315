main:
   # init $sp to highest data mem (actually the length of DM bc this $sp points to top of stack (i.e. item))
   addi $sp, $0, 8192

   # init pt plotter (i.e. index into DM for plotted pts)
   add $t8, $0, $0

   # Circle(30,100,20) #head
   addi $a0, $0, 30
   addi $a1, $0, 100
   addi $a2, $0, 20
   jal Circle

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
   addi $a0, $0, 24
   addi $a1, $0, 105
   addi $a2, $0, 3
   jal Circle

	# Circle(36,105,3) #right eye
   addi $a0, $0, 36
   addi $a1, $0, 105
   addi $a2, $0, 3
   jal Circle

	# Line(25,90,35,90) #mouth center
   addi $a0, $0, 25
   addi $a1, $0, 90
   addi $a2, $0, 35
   addi $a3, $0, 90
   jal Line

	# Line(25,90,20,95) #mouth left
   addi $a0, $0, 25
   addi $a1, $0, 90
   addi $a2, $0, 20
   addi $a3, $0, 95
   jal Line

	# Line(35,90,40,95) #mouth right
   addi $a0, $0, 35
   addi $a1, $0, 90
   addi $a2, $0, 40
   addi $a3, $0, 95
   jal Line

   j veryEnd               # we're done :)
