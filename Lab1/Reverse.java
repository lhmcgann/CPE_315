import java.util.Scanner;

public class Reverse {
   public static void main(String[] arg) {
      // get user input for the value to reverse
      Scanner input = new Scanner(System.in);
      System.out.print("Please enter a number to be reversed: ");
      int num = input.nextInt();

      System.out.print("The bitwise reverse of " + num + " (" + Integer.toBinaryString(num) + ") is ");

      // reverse the bits in num
      int ans = 0;
      int bit = 0;
      for (int i = 0; i < 32; i++) {
         bit = num & 1;
         ans <<= 1;
         ans += bit;
         num >>= 1;
      }

      System.out.println(ans + " (" + Integer.toBinaryString(ans) + ")");
   }
}
