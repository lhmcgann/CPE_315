# Name: Laura McGann
#  Discussed general algorithms with David Chau and Arun Shriram.
# Section: 01
# Description: Store the rightmost bit of the given number, leftshift a new
#  answer variable, add the singular bit to the answer, and right shift the 
#  given number. Repeat this process until you've gone through all of the bits 
#  in the given number. This essentially takes the next bit from the right and 
#  puts it as the next bit from the left.

# int ans = 0;
# int bit = 0;
# for (int i = 0; i < 32; i++) {
#    bit = num & 1;
#    ans <<= 1;
#    ans += bit;
#    num >>= 1;
# }

# declare global so programmer can see actual addresses.
.globl welcome
.globl numPrompt
.globl revText

#  Data Area (this area contains strings to be displayed during the program)
.data

# d46 = x2e chars (including null)
welcome:
   .asciiz " This program reverses the bits of a number\n\n"

# d20 = x14 chars w/ null
numPrompt:
   .asciiz " Enter an integer: "

revText:
   .asciiz " \n Reverse = "

#Text Area (i.e. instructions)
.text
main:
   
   # Display the welcome message (load 4 into $v0 to display)
   ori     $v0, $0, 4

   # This generates the starting address for the welcome message.
   # (assumes the register first contains 0).
   lui     $a0, 0x1001
   syscall
   
   # Display the number prompt
   ori     $v0, $0, 4

   # This is the starting address of the prompt (notice the
   # different address from the welcome message)
   lui     $a0, 0x1001
   ori     $a0, $a0, 0x2e
   syscall

   # Read number from the user (5 loaded int $v0, then syscall)
   ori     $v0, $0, 5
   syscall

   # Move number to $s0
   add     $s0, $v0, $0

   # KEY: number = $s0, answer = $s1, bit = $t0, i = $t1

   # Initilaize answer, bit, i, and comparison value (i.e. 32)
   ori     $s1, $0, 0
   ori     $t0, $0, 0
   ori     $t1, $0, 0
   ori     $t2, $0, 32

loop:
   # Check loop condition: i < 32
   slt     $t3, $t1, $t2
   beq     $t3, $0, end

   # Get the rightmost bit of the given number
   andi    $t0, $s0, 1

   # Left shift the answer variably to prepare for a new bit
   sll     $s1, $s1, 1

   # Add the bit
   add     $s1, $s1, $t0

   # Right shift the given number
   srl     $s0, $s0, 1

   # Increment i!!!
   addi    $t1, $t1, 1

   # Loop again
   j      loop

end:
   # Print the reverse message
   ori     $v0, $0, 4
   lui     $a0, 0x1001
   ori     $a0, $a0, 0x2e
   addi    $a0, $a0, 0x14
   syscall

   # Print the reversed number itself
   ori     $v0, $0, 1
   or      $a0, $0, $s1
   syscall

   # Exit
   ori     $v0, $0, 10
   syscall


