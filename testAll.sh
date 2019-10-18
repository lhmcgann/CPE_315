#!/bin/bash

# Compile all the needed code (in case haven't already)
make

for testInput in *.asm; do
   # Strip off the file extension, i.e., the ".asm"
   name=${testInput%.asm}

   # Run the test
   java $1 $testInput > $name.myout

   # diff the results
   diff -w -B $name.myout $name.output
done
