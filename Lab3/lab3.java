import java.util.Arrays;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.FileInputStream;

public class lab3 {

   public static boolean DEBUG = false;

   public static void main(String[] args) {

      // make sure a file was given
      if (!validArgs(args)) {
         System.out.println("Usage: java lab2 <filename> [scriptFilename]");
         System.exit(1);
      }

      // get filename from args[0] (I tested and it is args[0] not args[1])
      InputStream asmFile = openStream(args[0]);
      Assembler a = new Assembler();
      AsmReader asmReader = new AsmReader(asmFile, a);
      asmReader.readThroughLines(); // first pass to build symbol table
      if (DEBUG) {
         asmReader.printLabels();
         System.out.println("\n");
         asmReader.printInstLines();
         System.out.println("\n");
      }
      a.secondPass(); // store processed lines at line nums; compute labelAdrs
      asmReader.terminate();

      // set up script and emulator system
      Emulator e = new Emulator();
      InputStream script;
      if (args.length == 2)
         script = openStream(args[1]);
      else
         script = System.in;
      ScriptReader scriptReader = new ScriptReader(script, e);

      // actually execute all the cmds in the script (or user input)
      scriptReader.readThroughLines(); // won't end until q() exits

      scriptReader.terminate();
   }

   public static boolean validArgs(String[] args) {
      return args.length == 1 || args.length == 2;
   }

   public static FileInputStream openStream(String filename) {
      try {
         return new FileInputStream(filename);
      }
      catch (FileNotFoundException e) {
         System.out.println("The file " + filename + " could not be opened.");
         System.exit(1);
      }
      return null;
   }

}
