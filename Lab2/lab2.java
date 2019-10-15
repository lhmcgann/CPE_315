import java.util.Arrays;

public class lab2 {

   public static boolean DEBUG = false;

   public static void main(String[] args) {

      // make sure a file was given
      if (!validRegs(args)) {
         System.out.println("Usage: java lab2 [filename]");
         System.exit(1);
      }

      // get filename from args[0] (I tested and it is args[0] not args[1])
      Reader r = new Reader(args[0]);
      r.firstPass(); // first pass to build symbol table
      if (DEBUG) {
         r.printLabels();
         System.out.println("\n");
         r.printInstLines();
         System.out.println("\n");
      }
      r.a.translate();
      r.terminate();
   }

   public static boolean validRegs(String[] args) {
      return args.length == 1;
   }

}
