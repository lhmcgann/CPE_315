import java.util.Scanner;

public class Mod {
   public static void main(String[] args) {
      // get user input for the values of the dividend and divisor
      Scanner input = new Scanner(System.in);
      System.out.print("Please enter a 32-bit dividend: ");
      int dividend = input.nextInt();
      int divisor = 1;
      // make sure divisor is a multiple of 2
      while (divisor % 2 == 1) {
         System.out.print("Please enter a 32-bit divisor (must be divisible by 2): ");
         divisor = input.nextInt();
      }

      // algorithm for modulus
      int mask = 1;
      int remainder = 0;
      while (mask < divisor) {
         remainder += dividend & mask;
         mask <<= 1;
      }

      System.out.println(dividend + " % " + divisor + " = " + remainder);
   }
}
