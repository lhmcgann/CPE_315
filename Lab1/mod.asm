# Name: Laura McGann
#  Discussed general algorithms with David Chau and Arun Shriram.
# Section: 01
# Description: Find the remainder of a given dividend and divisor by 
#  left shifting a mask until the mask equals the divisor. Before each 
#  shift, add the AND of the dividend and the mask to the remainder 
#  variable. KEY: The remainder is the bits of the dividend right of 
#  "divisor bit" since the divisor is guaranteed to be divisible by 2.
#  (e.g. divisor = 8 (1000) so the remainder is the 3 rightmost bits of the 
#  dividend)

# int mask = 1;
# int remainder = 0;
# while (mask < divisor) {
#    remainder += dividend & mask;
#    mask <<= 1;
# }

# declare global so programmer can see actual addresses.
.globl welcome
.globl divdPrompt
.globl divrPrompt
.globl modText

#  Data Area (this area contains strings to be displayed during the program)
.data

# d64 = x40 chars (including null)
welcome:
   .asciiz " This program computes the remainder (modulo) of two numbers \n\n"

# d29 = x1d chars w/ null
divdPrompt:
   .asciiz " Enter an integer dividend: "

# d33 = x21 chars w/ null
divrPrompt:
   .asciiz " Enter divisor (multiple of 2): "

# d16 = x10 chars w/ null
modText:
   .asciiz " \n Remainder = "

#Text Area (i.e. instructions)
.text
main:
   
   # Display the welcome message (load 4 into $v0 to display)
   ori     $v0, $0, 4

   # This generates the starting address for the welcome message.
   # (assumes the register first contains 0).
   lui     $a0, 0x1001
   syscall
   
   # Display the dividend prompt
   ori     $v0, $0, 4

   # This is the starting address of the prompt (notice the
   # different address from the welcome message)
   lui     $a0, 0x1001
   ori     $a0, $a0, 0x40
   syscall

   # Read dividend from the user (5 loaded int $v0, then syscall)
   ori     $v0, $0, 5
   syscall

   # Move dividend to $s0
   add     $s0, $v0, $0

   # Display the divisor prompt
   ori     $v0, $0, 4

   # Load starting adr of divisor prompt into $a0, then syscall
   lui     $a0, 0x1001
   ori     $a0, $a0, 0x40
   addi    $a0, $a0, 0x1d
   syscall

   # Read divisor from user
   ori     $v0, $0, 5
   syscall

   # Move divisor to $s1
   add     $s1, $v0, $0

   # KEY: mask = $t0, dividend = $s0, divisor = $s1, remainder = $s2
   
   # Initialize mask and remainder
   ori     $t0, $0, 1
   and     $s2, $0, $0

loop:

   # Loop condition (mask < divisor)
   slt     $t1, $t0, $s1
   beq     $0, $t1, end

   # And dividend w/ mask; add to remainder
   and     $t1, $s0, $t0
   add     $s2, $s2, $t1

   # Leftshift mask
   sll     $t0, $t0, 1

   # Loop again
   j       loop

end:
   
   # Print the remainder prompt
   ori     $v0, $0, 4
   lui     $a0, 0x1001
   ori     $a0, $a0, 0x40
   addi    $a0, $a0, 0x1d
   addi    $a0, $a0, 0x21
   syscall

   # Print the remainder itself
   ori     $v0, $0, 1
   or      $a0, $0, $s2
   syscall

   # Exit (load 10 into $v0)
   ori     $v0, $0, 10
   syscall
