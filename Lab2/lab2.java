import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class lab2 {
   public static void main(String[] args) throws FileNotFoundException {

      // make sure a file was given
      if (!validArgs(args)) {
         System.out.println("Usage: java lab2 [filename]");
         System.exit(1);
      }

      // get filename from args[1] (args[0] is this program itself)
      File file = new File(args[0]);
      Assembler a = new Assembler(file);
      // FUNCTION: passThrough(file, actionFunction)
      // axnFnxn1: findLabel(line)
      // axnFnxn2: processCmd(line)
      a.terminate();
   }

   public static boolean validArgs(String[] args) {
      return args.length == 1;
   }
}
