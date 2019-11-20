import java.util.Arrays;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.FileInputStream;

public class lab5 {

   public static boolean DEBUG = false;

   public static void main(String[] args) {

      // make sure a file was given
      if (!validArgs(args)) {
         System.out.println("Usage: java lab5 <filename> <scriptFilename> [GHR size]");
         System.exit(1);
      }

      // get filename from args[0] (I tested and it is args[0] not args[1])
      InputStream asmFile = openStream(args[0]);
      Assembler a = new Assembler();
      AsmReader asmReader = new AsmReader(asmFile, a);
      asmReader.readThroughLines(); // first pass to build symbol table

      // emulator needed for second pass of Assembler and for script reading
      char ghrSize = getGHRSize(args);
      if (DEBUG)
         System.out.println(ghrSize);
      Emulator e = new Emulator(ghrSize);

      a.secondPass(e); // store processed lines at line nums; compute labelAdrs
      asmReader.terminate();

      // set up script reader: script file if given, else user input
      InputStream script;
      boolean isScript = isScript(args);
      script = ((isScript) ? openStream(args[1]) : System.in);
      //    script = openStream(args[1]);
      //    isScript = true;
      // }
      // else {
      //
      //    isScript = false;
      // }
      ScriptReader scriptReader = new ScriptReader(script, e, isScript); // isScript

      // actually execute all the cmds in the script (or user input)
      scriptReader.readThroughLines(); // won't end until q() exits

      scriptReader.terminate();
   }

   private static boolean validArgs(String[] args) {
      // correct w/ or w/o GHR size; for turning in, guaranteed to have script
      // return args.length == 3 || args.length == 2;
      return args.length > 1 && args.length < 4;
   }

   private static FileInputStream openStream(String filename) {
      try {
         return new FileInputStream(filename);
      }
      catch (FileNotFoundException e) {
         System.out.println("The file " + filename + " could not be opened.");
         System.exit(1);
      }
      return null;
   }

   private static boolean isScript(String[] args) {
      return args[1].contains(".script");
   }

   private static char getGHRSize(String[] args) {
      if (args.length == 3)
         return (char) Integer.parseInt(args[2]); // GHR size is last of 3 args
      return 2; // default GHR size
   }

}
