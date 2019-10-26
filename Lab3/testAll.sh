#!/bin/bash

# usage message
if [$# != 1]
then
   echo "Usage: testAll.sh <compiled main to test>\n"
fi

# Compile all the needed code (in case haven't already)
make

for testInput in *.asm; do
   # Strip off the file extension, i.e., the ".asm"
   name=${testInput%.asm}

   # Run the test
   java $1 $testInput $name.script > $name.myout

   # diff the results
   diff -w -B $name.myout $name.output
done
