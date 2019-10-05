import java.util.Scanner;

public class Divide {
   public static void main(String[] args) {
      // get user input for the values of the highDividend, lowDiv, and divisor
      Scanner input = new Scanner(System.in);
      System.out.print("Please enter the high 32-bits: ");
      int divHigh = input.nextInt();
      System.out.print("Please enter the low 32-bits: ");
      int divLow = input.nextInt();
      int divisor = 1;
      // make sure divisor is a multiple of 2
      while (divisor % 2 == 1) {
         System.out.print("Please enter a 31-bit divisor (must be divisible by 2): ");
         divisor = input.nextInt();
      }

      System.out.print(divHigh + ", " + divLow + " / " + divisor + " = ");

      int bit = 0;
      while (divisor > 1) {
         bit = divHigh & 1; // get rightmost bit of divH
         // divide high and low by 2
         divHigh >>= 1;
         divLow >>= 1;
         // shift divisor bc "used one of the 2's"
         divisor >>= 1;

         // IMPORTANT (bc Java): clear new leftmost bit of low (bc will shift w/ new 1's)
         divLow &= 2147483647; // (max pos int)

         // get prev rightmost bit of high into now open leftmost bit of low
         bit <<= 31;
         divLow += bit;
      }

      System.out.println(divHigh + ", " + divLow);
   }
}
