import java.util.Arrays;

public class lab3 {

   public static boolean DEBUG = false;

   public static void main(String[] args) {

      // make sure a file was given
      if (!validRegs(args)) {
         System.out.println("Usage: java lab2 [filename]");
         System.exit(1);
      }

      // get filename from args[0] (I tested and it is args[0] not args[1])
      Reader r = new Reader(args[0]);
      Assembler a = new Assembler();
      r.firstPass(a); // first pass to build symbol table
      if (DEBUG) {
         r.printLabels(a);
         System.out.println("\n");
         r.printInstLines(a);
         System.out.println("\n");
      }
      a.translate();
      r.terminate();
   }

   public static boolean validRegs(String[] args) {
      return args.length == 1;
   }

}
