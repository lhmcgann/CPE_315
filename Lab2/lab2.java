import java.util.Arrays;

public class lab2 {

   public static boolean DEBUG = false;

   public static void main(String[] args) {

      // make sure a file was given
      if (!validArgs(args)) {
         System.out.println("Usage: java lab2 [filename]");
         System.exit(1);
      }

      // get filename from args[0] (I tested and it is args[0] not args[1])
      Reader r = new Reader(args[0]);
      r.firstPass(); // first pass to build symbol table
      r.a.printSymbolTable();
      System.out.println("\n");
      r.a.printInstMem();
      // r.iterateThroughLines(); // second pass to translate code
      r.terminate();
   }

   public static boolean validArgs(String[] args) {
      return args.length == 1;
   }

}
