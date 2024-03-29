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
      int ghrSize = getGHRSize(args);
      Emulator e = new Emulator(ghrSize);

      a.secondPass(e); // store processed lines at line nums; compute labelAdrs
      asmReader.terminate();

      // set up script reader: script file if given, else user input
      InputStream script;
      boolean isScript = isScript(args);
      script = ((isScript) ? openStream(args[1]) : System.in);
      ScriptReader scriptReader = new ScriptReader(script, e, isScript); // isScript

      // actually execute all the cmds in the script (or user input)
      scriptReader.readThroughLines(); // won't end until q() exits

      scriptReader.terminate();
   }

   private static boolean validArgs(String[] args) {
      // correct w/ or w/o GHR size; for turning in, guaranteed to have script
      // return args.length == 3 || args.length == 2 || args.length == 1;
      // 3 (script and size) or 2 (script or size) or 1 (neither script or size)
      return args.length > 0 && args.length < 4;
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
      return args.length > 1 && args[1].contains(".script");
   }

   private static int getGHRSize(String[] args) {
      // no script yes ghrSize (len == 2) --> look at args[len-1]
      // no script no ghr (len == 1) --> 2
      // yes script yes ghr (len == 3) --> look at args[len-1]
      // yes script no ghrSize (len == 2) --> 2
      if (!isScript(args)) {
         if (args.length == 2)
            return  Integer.parseInt(args[args.length - 1]);
         else
            return 2;
      }
      // if is script and is given ghr size, look at 3rd
      else if (args.length == 3)
         return  Integer.parseInt(args[args.length - 1]); // GHR size is last of 3 args
      return 2; // default GHR size
   }

}
