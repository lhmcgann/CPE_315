#!/bin/bash
for testA in *.run; do
   # Strip off file extension, i.e., the ".run"
   name=${testA%.run}
   
   # Run the test
   bash ./$testA

   diff -q $name.out $name.expect
   diff -q $name.err $name.e_err
   diff -q $name.exit $name.e_exit

done
