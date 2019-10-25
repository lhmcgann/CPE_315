import java.util.Arrays;
import java.io.File;

public class lab3 {

   public static boolean DEBUG = false;

   public static void main(String[] args) {

      // make sure a file was given
      if (!validRegs(args)) {
         System.out.println("Usage: java lab2 [filename]");
         System.exit(1);
      }

      // get filename from args[0] (I tested and it is args[0] not args[1])
      File asmFile = new File(args[0]);
      Assembler a = new Assembler();
      AsmReader asmReader = new AsmReader(asmFile, a);
      asmReader.readThroughLines(); // first pass to build symbol table
      if (DEBUG) {
         asmReader.printLabels();
         System.out.println("\n");
         asmReader.printInstLines();
         System.out.println("\n");
      }
      a.translate();
      asmReader.terminate();
   }

   public static boolean validRegs(String[] args) {
      return args.length == 1 | args.length == 2;
   }

}
