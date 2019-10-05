# Name: Laura McGann
#  Discussed general algorithms with David Chau and Arun Shriram.
# Seciton: 01
# Description: Store the rightmost bit of of the upper half. Right shift (aka
#  divide by 2) both the upper and lower halves. Shift the stored bit so it can
#  be added into the leftmost bit of the lower half. Do this process n times
#  where 2^n = divisor.

# int bit = 0;
# while (divisor > 1) {
#    bit = divHigh & 1; // get rightmost bit of divH
#
#    // divide high and low by 2
#    divHigh >>= 1;
#    divLow >>= 1;
#
#    // shift divisor bc "used one of the 2's"
#    divisor >>= 1;
#
#    // IMPORTANT (bc Java): clear new leftmost bit of low (bc will shift w/ new 1's)
#    divLow &= 2147483647; // (max pos int)
#
#    // get prev rightmost bit of high into now open leftmost bit of low
#    bit <<= 31;
#    divLow += bit;
# }

# declare global so programmer can see actual addresses.
.globl welcome
.globl divHPrompt
.globl divLPrompt
.globl divrPrompt
.globl ansText
.globl comma

#  Data Area (this area contains strings to be displayed during the program)
.data

# d59 = x3b chars (including null)
welcome:
   .asciiz " This program divides a 64-bit number by a 31-but number\n\n"

# d33 = x21 chars w/ null
divHPrompt:
   .asciiz " Enter the upper 32-bit number: "

# d33 = x21 chars w/ null
divLPrompt:
   .asciiz " Enter the lower 32-bit number: "

# d26 = x1a w/ null
divrPrompt:
   .asciiz " Enter a 31-bit divisor: "

# d22 = x16 w/ null
ansText:
   .asciiz " \n Quotient (H, L) = "

comma:
   .asciiz ", "

#Text Area (i.e. instructions)
.text
main:

   # Display the welcome message (load 4 into $v0 to display)
   ori     $v0, $0, 4
   # This generates the starting address for the welcome message.
   # (assumes the register first contains 0).
   lui     $a0, 0x1001
   syscall

   # Display the divH prompt
   ori     $v0, $0, 4
   # Load starting adr of divH prompt into $a0, then syscall
   lui     $a0, 0x1001
   ori     $a0, $a0, 0x3b
   syscall
   # Read divH from the user (5 loaded int $v0, then syscall)
   ori     $v0, $0, 5
   syscall
   # Move divH to $s0
   add     $s0, $v0, $0

   # Display the divL prompt
   ori     $v0, $0, 4
   # Load starting adr of divL prompt into $a0, then syscall
   lui     $a0, 0x1001
   ori     $a0, $a0, 0x3b
   addi    $a0, $a0, 0x21
   syscall
   # Read divL from user
   ori     $v0, $0, 5
   syscall
   # Move divL to $s1
   add     $s1, $v0, $0

   # Display the divisor prompt
   ori     $v0, $0, 4
   # Load starting adr of divisor prompt into $a0, then syscall
   lui     $a0, 0x1001
   ori     $a0, $a0, 0x3b
   addi    $a0, $a0, 0x21
   addi    $a0, $a0, 0x21
   syscall
   # Read divisor from user
   ori     $v0, $0, 5
   syscall
   # Move divisor to $s2
   add     $s2, $v0, $0

   # KEY: divH = s0, divL = s1, divisor = s2, bit = t0, cdtn/i = t1

loop:
   # Check to see if divisor == 1 (end condition)
   addi    $t1, $s2, -1
   beq     $t1, $0, end

   # Get the rightmost bit of divH
   andi    $t0, $s0, 1

   # Right shift divH, divL, and the divisor
   srl     $s0, $s0, 1
   srl     $s1, $s1, 1
   srl     $s2, $s2, 1

   # Get the stored bit of divH into the now open leftmost bit of divL
   sll     $t0, $t0, 31
   add     $s1, $s1, $t0

   # Loop again
   j       loop

end:
   # Print the end result
   ori     $v0, $0, 4
   lui     $a0, 0x1001
   ori     $a0, $a0, 0x3b
   addi    $a0, $a0, 0x21
   addi    $a0, $a0, 0x21
   addi    $a0, $a0, 0x1a
   syscall

   # Print the new divH
   ori     $v0, $0, 1
   or      $a0, $0, $s0
   syscall

   # Print the ", "
   ori     $v0, $0, 4
   lui     $a0, 0x1001
   ori     $a0, $a0, 0x3b
   addi    $a0, $a0, 0x21
   addi    $a0, $a0, 0x21
   addi    $a0, $a0, 0x1a
   addi    $a0, $a0, 0x16
   syscall

   # Print the new divL
   ori     $v0, $0, 1
   or      $a0, $0, $s1
   syscall

   # Exit
   ori     $v0, $0, 10
   syscall
