# Name: Laura McGann
#  Discussed general algorithms with David Chau and Arun Shriram.
# Section: 01
# Description: Since we can only use addition, we must add a "working base" to
#  itself n-1 times, where n is the original base number. This essentially
#  multiplies the base by itself once. After each "multiplication", the "working
#  base" becomes (n-1)*(current working base) so then the bigger number can then be
#  added to itself n-1 times. This repeats for exponent-1 times, so the original
#  base is effectively multiplied by itself exp-1 times.
#     For example: take 2^3. The original base is 2, the exp is 3. First, the
#     working base is set to the previous sum (in this case only 2) and then added
#     to itself (original base - 1) times: 2+2=4. Then it repeats:
#     working base = previous sum (4); add to self n-1 times: 4+4=8. Since we have
#     done exp-1 repititions (3-1 = 2), we can stop with the answer: 2^3 = 8.

# int ans = base;
# int multiplier = base;
# // multiply the base by itself (exp - 1) times
# for (int i = 1; i < exp; i++) {
#    // multiply current answer by the base
#    for (int j = 1; j < multiplier; j++)
#       ans += base;
#    base = ans;
# }

# declare global so programmer can see actual addresses.
.globl welcome
.globl basePrompt
.globl expPrompt
.globl result

#  Data Area (this area contains strings to be displayed during the program)
.data

# d48 = x30 chars (including null)
welcome:
   .asciiz " This program raises a base number to a power\n\n"

# d24 = x18 chars w/ null
basePrompt:
   .asciiz " Enter a base integer: "

# d39 = x1d chars w/ null
expPrompt:
   .asciiz " Enter an exponent integer: "

result:
   .asciiz "\n Result = "

#Text Area (i.e. instructions)
.text
main:

   # Display the welcome message (load 4 into $v0 to display)
   ori     $v0, $0, 4
   # This generates the starting address for the welcome message.
   # (assumes the register first contains 0).
   lui     $a0, 0x1001
   syscall

   # Display the base number prompt
   ori     $v0, $0, 4
   # Load starting adr of basePrompt into $a0, then syscall
   lui     $a0, 0x1001
   ori     $a0, $a0, 0x30
   syscall
   # Read base number from the user (5 loaded int $v0, then syscall)
   ori     $v0, $0, 5
   syscall
   # Move base number to $s0
   add     $s0, $v0, $0

   # Display the exponent prompt
   ori     $v0, $0, 4
   # Load starting adr of expPrompt into $a0, then syscall
   lui     $a0, 0x1001
   ori     $a0, $a0, 0x30
   addi    $a0, $a0, 0x18
   syscall
   # Read exponent from the user (5 loaded int $v0, then syscall)
   ori     $v0, $0, 5
   syscall
   # Move exponent to $s1
   add     $s1, $v0, $0

   # KEY: base = s0, exponent = s1, answer = s2, multiplier = t0, i = t1, j = t2

   # Initilaize answer and multiplier to base, i to 1
   or      $s2, $0, $s0
   or      $t0, $0, $s0
   ori     $t1, $0, 1

loop:
   # Check loop condition: i < exponent
   slt     $t3, $t1, $s1
   beq     $t3, $0, end

   # Set j to 1
   ori     $t2, $0, 1

multByBase:
   # While j < multiplier
   slt     $t3, $t2, $t0
   beq     $t3, $0, next

   # Add "working base" to answer
   add     $s2, $s2, $s0

   # Increment j!!!
   addi    $t2, $t2, 1

   # Sub-loop again
   j       multByBase

next:
   # Set "working base" to new answer
   or      $s0, $0, $s2

   # Increment i!!!
   addi    $t1, $t1, 1

   # Loop again
   j      loop

end:
   # Print the result message
   ori     $v0, $0, 4
   lui     $a0, 0x1001
   ori     $a0, $a0, 0x30
   addi    $a0, $a0, 0x18
   addi    $a0, $a0, 0x1d
   syscall

   # Print the result itself
   ori     $v0, $0, 1
   or      $a0, $0, $s2
   syscall

   # Exit
   ori     $v0, $0, 10
   syscall
