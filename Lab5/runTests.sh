#!/bin/bash
for testA in *.run; do
   # Strip off file extension, i.e., the ".run"
   name=${testA%.run}

   # Run the test
   bash ./$testA

   diff -w -B $name.myout $name.output

done
