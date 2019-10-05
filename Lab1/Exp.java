import java.util.Scanner;

public class Exp {
   public static void main(String[] args) {
      // get user input for the values of the base and exponent
      Scanner input = new Scanner(System.in);
      System.out.print("Please enter a base: ");
      int base = input.nextInt();
      System.out.print("Please enter an exponent: ");
      int exp = input.nextInt();

      System.out.print(base + "^" + exp + " = ");

      int ans = base;
      int multiplier = base;
      // multiply the base by itself (exp - 1) times
      for (int i = 1; i < exp; i++) {
         // multiply current answer by the base
         for (int j = 1; j < multiplier; j++)
            ans += base;
         base = ans;
      }

      System.out.println(ans);
   }
}
